package com.juhai.business.mapper;

import java.util.List;
import com.juhai.business.domain.Withdraw;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author zhaotiezhu
 * @date 2023-05-21
 */
public interface WithdrawMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Withdraw selectWithdrawById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param withdraw 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Withdraw> selectWithdrawList(Withdraw withdraw);

    /**
     * 新增【请填写功能名称】
     * 
     * @param withdraw 【请填写功能名称】
     * @return 结果
     */
    public int insertWithdraw(Withdraw withdraw);

    /**
     * 修改【请填写功能名称】
     * 
     * @param withdraw 【请填写功能名称】
     * @return 结果
     */
    public int updateWithdraw(Withdraw withdraw);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteWithdrawById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWithdrawByIds(Long[] ids);

    public void deleteByUserName(String userName);
}
