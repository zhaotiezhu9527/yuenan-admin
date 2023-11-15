package com.juhai.web.controller.business;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import com.juhai.business.domain.*;
import com.juhai.business.service.*;
import com.juhai.common.annotation.Log;
import com.juhai.common.core.controller.BaseController;
import com.juhai.common.core.domain.AjaxResult;
import com.juhai.common.core.page.TableDataInfo;
import com.juhai.common.enums.BusinessType;
import com.juhai.common.utils.RedisKeyUtil;
import com.juhai.common.utils.poi.ExcelUtil;
import com.juhai.web.controller.business.request.OptUserMoneyRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Controller
 * 
 * @author zhaotiezhu
 * @date 2023-05-21
 */
@RestController
@RequestMapping("/business/user")
public class UserController extends BaseController
{
    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IDepositService depositService;

    @Autowired
    private IWithdrawService withdrawService;

    @Autowired
    private IParamterService paramterService;

    @Autowired
    private IUserReportService userReportService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IUserLogService userLogService;

    @Autowired
    private IOrderService orderService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(User user)
    {
        startPage();
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/getAgentUser")
    public TableDataInfo getAgentUser(User user)
    {
        User temp = userService.selectUserById(user.getId());
        if (temp == null || StringUtils.isBlank(temp.getUserAgent())) {
            return getDataTable(new ArrayList<>());
        }

        User user1 = new User();
        user1.setUserName(temp.getUserAgent());

        startPage();
        List<User> list = userService.selectUserList(user1);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/getSubUser")
    public TableDataInfo getSubUser(User user)
    {
        User temp = userService.selectUserById(user.getId());

        User user1 = new User();
        user1.setUserAgent(temp.getUserName());

        startPage();
        List<User> list = userService.selectUserList(user1);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('business:user:export')")
    @Log(title = "【用户导出】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, User user)
    {
        List<User> list = userService.selectUserList(user);
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userService.selectUserById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('business:user:add')")
    @Log(title = "【用户新增】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody User user)
    {
        // 校验用户名 密码 支付密码
        boolean matchUserName = ReUtil.isMatch("^[a-zA-Z0-9]{4,20}$", user.getUserName());
        if (!matchUserName) {
            return AjaxResult.error("请输入4-20位用户名");
        }
        boolean matchLoginPwd = ReUtil.isMatch("^[a-zA-Z0-9]{6,20}$", user.getLoginPwd());
        if (!matchLoginPwd) {
            return AjaxResult.error("请输入6-20位登录密码");
        }
        boolean matchPayPwd = ReUtil.isMatch("^\\d{6}$", user.getPayPwd());
        if (!matchPayPwd) {
            return AjaxResult.error("请输入6位支付密码");
        }
        // 查询用户名是否存在
        User existUser = userService.getUserByName(user.getUserName());
        if (existUser != null) {
            return AjaxResult.error("用户名[" + user.getUserName() + "]已存在");
        }

        Map<String, String> params = paramterService.getAllParamByMap();
        String defaultAgent = params.get("default_agent");
        // 查询代理信息
        if (StringUtils.isBlank(user.getUserAgent())) {
            user.setUserAgent(defaultAgent);
        } else {
            User agent = userService.getUserByName(user.getUserAgent());
            user.setUserAgent(agent == null ? defaultAgent : agent.getUserName());
        }

        Date now = new Date();
        User temp = new User();
        temp.setUserName(user.getUserName());
        temp.setBalance(new BigDecimal("0"));
        temp.setRealName(null);
        temp.setIdCard(null);
        temp.setLoginPwd(SecureUtil.md5(user.getLoginPwd()));
        temp.setPayPwd(SecureUtil.md5(user.getPayPwd()));
        temp.setInviteCode(RandomUtil.randomNumbers(6));
        temp.setWalletAddr(null);
        temp.setBankCardNum(null);
        temp.setBankName(null);
        temp.setBankAddr(null);
        temp.setUserStatus(0L);
        temp.setUserLevelId(0L);
        temp.setUserAgent(user.getUserAgent());
        temp.setRegisterTime(now);
        temp.setRegisterIp("0.0.0.1");
        temp.setLastTime(null);
        temp.setLastIp(null);
        temp.setModifyTime(now);

        return toAjax(userService.insertUser(temp));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('business:user:edit')")
    @Log(title = "【用户修改】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody User user)
    {
        if (StringUtils.isNotBlank(user.getLoginPwd())) {
            boolean matchLoginPwd = ReUtil.isMatch("^[a-zA-Z0-9]{6,12}$", user.getLoginPwd());
            if (!matchLoginPwd) {
                return AjaxResult.error("请输入6-12位登录密码");
            }
            user.setLoginPwd(SecureUtil.md5(user.getLoginPwd()));
        } else {
            user.setLoginPwd(null);
        }

        if (StringUtils.isNotBlank(user.getPayPwd())) {
            boolean matchPayPwd = ReUtil.isMatch("^\\d{6}$", user.getPayPwd());
            if (!matchPayPwd) {
                return AjaxResult.error("请输入6位支付密码");
            }
            user.setPayPwd(SecureUtil.md5(user.getPayPwd()));
        } else {
            user.setPayPwd(null);
        }
        user.setBalance(null);

        User redisUser = userService.selectUserById(user.getId());
        // 删除密码限制
        String incKey = RedisKeyUtil.PayPwdErrorKey(redisUser.getUserName());
        redisTemplate.delete(incKey);
        return toAjax(userService.updateUser(user));
    }


    @PreAuthorize("@ss.hasPermi('business:user:optMoney')")
    @Log(title = "【重置用户余额】", businessType = BusinessType.UPDATE)
    @PostMapping("/resetBalance")
    public AjaxResult resetBalance(@RequestBody User user)
    {
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Transactional
    @PreAuthorize("@ss.hasPermi('business:user:remove')")
    @Log(title = "【用户删除】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        if (ids != null && ids.length > 1) {
            return AjaxResult.error("请选择一个用户进行删除");
        }
        User user = userService.selectUserById(ids[0]);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        // 删除用户
        userService.deleteUserById(ids[0]);
        // 提现
        withdrawService.deleteByUserName(user.getUserName());
        // 充值
        depositService.deleteByUserName(user.getUserName());
        // 报表
        userReportService.deleteByUserName(user.getUserName());
        // 登录日志
        userLogService.deleteByUserName(user.getUserName());
        // 订单
        orderService.deleteByUserName(user.getUserName());
        // 流水记录
        accountService.deleteByUserName(user.getUserName());
        return toAjax(true);
    }

    @Transactional
    @PreAuthorize("@ss.hasPermi('business:user:optMoney')")
    @Log(title = "【用户上下分】", businessType = BusinessType.UPDATE)
    @PostMapping("/optMoney")
    public AjaxResult optMoney(@RequestBody OptUserMoneyRequest request) throws Exception {
//        if (StringUtils.isBlank(request.getRemark())) {
//            return AjaxResult.error("请输入备注");
//        }
//        User user = userService.selectUserById(NumberUtils.toLong(request.getUserName()));
        User temp = new User();
        temp.setInviteCode(request.getInviteCode());
        List<User> users = userService.selectUserList(temp);
//        User user = userService.getUserByName(request.getUserName());
        if (CollUtil.isEmpty(users)) {
            return AjaxResult.error("用户不存在.");
        }
        User user = users.get(0);
        Date now = new Date();
        BigDecimal money = new BigDecimal(request.getMoney());
        if (money.doubleValue() <= 0) {
            return AjaxResult.error("请正确输入金额.");
        }

        // 是U的话需要换算
        Map<String, String> params = paramterService.getAllParamByMap();
        if (StringUtils.equals("2", request.getAmountType())) {
            Double usdtRate = MapUtil.getDouble(params, "usdt_rate");
            money = NumberUtil.mul(money, usdtRate);
        }

        if (StringUtils.equals("1", request.getType())) {
            // 加钱
            userService.updateUserBalance(user.getUserName(), money);
            // 充值记录
            String orderNo = IdUtil.getSnowflakeNextIdStr();
            Deposit deposit = new Deposit();
            deposit.setOrderNo(orderNo);
            deposit.setUserName(user.getUserName());
            deposit.setRealName(user.getRealName());
            deposit.setOptAmount(money);
            deposit.setBeforeAmount(user.getBalance());
            deposit.setAfterAmount(NumberUtil.add(user.getBalance(), money));
            deposit.setOptType(1L);
            deposit.setOptTime(now);
            deposit.setStatus(1L);
            deposit.setOptUser(getUsername());
            deposit.setUserAgent(user.getUserAgent());
            deposit.setRemark(request.getRemark());
            depositService.insertDeposit(deposit);

            // 流水记录
            Account account = new Account();
            account.setUserName(user.getUserName());
            account.setOptAmount(money);
            account.setBeforeAmount(user.getBalance());
            account.setAfterAmount(NumberUtil.add(user.getBalance(), money));
            account.setType(1L);
            account.setOptType(1L);
            account.setOptTime(new Date());
            account.setUserAgent(user.getUserAgent());
            account.setRefNo(orderNo);
            account.setAccountNo(IdUtil.getSnowflakeNextIdStr());
            account.setRemark("Hệ thống nạp tiền");
            accountService.insertAccount(account);

            // 记录报表
            UserReport report = new UserReport();
            report.setUserName(user.getUserName());
            report.setToday(DateUtil.format(now, "yyyyMMdd"));
            report.setDepositAmount(money);
            report.setWithdrawAmount(new BigDecimal("0"));
            report.setInvestmentAmount(new BigDecimal("0"));
            report.setIncomeAmount(new BigDecimal("0"));
            userReportService.insertOrUpdate(report);

            return toAjax(true);
        } else if (StringUtils.equals("2", request.getType())) {
            if (money.doubleValue() > user.getBalance().doubleValue()) {
                return AjaxResult.error("用户账户余额为:" + user.getBalance() + "元");
            }
            // 加钱
            userService.updateUserBalance(user.getUserName(), money.negate());
            // 提现记录
            String orderNo = IdUtil.getSnowflakeNextIdStr();
            Withdraw withdraw = new Withdraw();
            withdraw.setOrderNo(orderNo);
            withdraw.setUserName(user.getUserName());
            withdraw.setOptAmount(money);
            withdraw.setUsdtAmount(StringUtils.equals("2", request.getAmountType()) ? new BigDecimal(request.getMoney()) : null);
            withdraw.setBeforeAmount(user.getBalance());
            withdraw.setAfterAmount(NumberUtil.sub(user.getBalance(), money));
            withdraw.setWalletAddr(null);
            withdraw.setBankCardNum(null);
            withdraw.setBankName(null);
            withdraw.setBankAddr(null);
            withdraw.setOptType(0L);
            withdraw.setStatus(1L);
            withdraw.setOptTime(now);
            withdraw.setCheckTime(now);
            withdraw.setOperator(getUsername());
            withdraw.setUserAgent(user.getUserAgent());
            withdraw.setRealName(user.getRealName());
            withdraw.setRemark(request.getRemark());
            withdraw.setInviteCode(user.getInviteCode());
            withdrawService.insertWithdraw(withdraw);

            // 流水记录
            Account account = new Account();
            account.setUserName(user.getUserName());
            account.setOptAmount(money);
            account.setBeforeAmount(user.getBalance());
            account.setAfterAmount(NumberUtil.sub(user.getBalance(), money));
            account.setType(2L);
            account.setOptType(2L);
            account.setOptTime(now);
            account.setUserAgent(user.getUserAgent());
            account.setRefNo(orderNo);
            account.setAccountNo(IdUtil.getSnowflakeNextIdStr());
            account.setRemark("Hệ thống trừ tiền");
            accountService.insertAccount(account);
            // 下分
            return toAjax(true);
        }
        return toAjax(false);
    }
}
