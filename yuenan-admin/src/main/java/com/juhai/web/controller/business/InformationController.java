package com.juhai.web.controller.business;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.juhai.common.annotation.Log;
import com.juhai.common.core.controller.BaseController;
import com.juhai.common.core.domain.AjaxResult;
import com.juhai.common.enums.BusinessType;
import com.juhai.business.domain.Information;
import com.juhai.business.service.IInformationService;
import com.juhai.common.utils.poi.ExcelUtil;
import com.juhai.common.core.page.TableDataInfo;

/**
 * 收集用户列表Controller
 * 
 * @author zhaotiezhu
 * @date 2023-12-15
 */
@RestController
@RequestMapping("/business/information")
public class InformationController extends BaseController
{
    @Autowired
    private IInformationService informationService;

    /**
     * 查询收集用户列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:information:list')")
    @GetMapping("/list")
    public TableDataInfo list(Information information)
    {
        System.out.println(JSON.toJSONString(information));
        startPage();
        List<Information> list = informationService.selectInformationList(information);
        return getDataTable(list);
    }

    /**
     * 导出收集用户列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:information:export')")
    @Log(title = "收集用户列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Information information)
    {
        List<Information> list = informationService.selectInformationList(information);
        ExcelUtil<Information> util = new ExcelUtil<Information>(Information.class);
        util.exportExcel(response, list, "收集用户列表数据");
    }

    /**
     * 获取收集用户列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:information:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(informationService.selectInformationById(id));
    }

    /**
     * 新增收集用户列表
     */
    @PreAuthorize("@ss.hasPermi('business:information:add')")
    @Log(title = "收集用户列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Information information)
    {
        return toAjax(informationService.insertInformation(information));
    }

    /**
     * 修改收集用户列表
     */
    @PreAuthorize("@ss.hasPermi('business:information:edit')")
    @Log(title = "收集用户列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Information information)
    {
        return toAjax(informationService.updateInformation(information));
    }

    /**
     * 删除收集用户列表
     */
    @PreAuthorize("@ss.hasPermi('business:information:remove')")
    @Log(title = "收集用户列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(informationService.deleteInformationByIds(ids));
    }
}
