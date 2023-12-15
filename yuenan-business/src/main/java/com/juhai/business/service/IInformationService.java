package com.juhai.business.service;

import java.util.List;
import com.juhai.business.domain.Information;

/**
 * 收集用户列表Service接口
 * 
 * @author zhaotiezhu
 * @date 2023-12-15
 */
public interface IInformationService 
{
    /**
     * 查询收集用户列表
     * 
     * @param id 收集用户列表主键
     * @return 收集用户列表
     */
    public Information selectInformationById(Long id);

    /**
     * 查询收集用户列表列表
     * 
     * @param information 收集用户列表
     * @return 收集用户列表集合
     */
    public List<Information> selectInformationList(Information information);

    /**
     * 新增收集用户列表
     * 
     * @param information 收集用户列表
     * @return 结果
     */
    public int insertInformation(Information information);

    /**
     * 修改收集用户列表
     * 
     * @param information 收集用户列表
     * @return 结果
     */
    public int updateInformation(Information information);

    /**
     * 批量删除收集用户列表
     * 
     * @param ids 需要删除的收集用户列表主键集合
     * @return 结果
     */
    public int deleteInformationByIds(Long[] ids);

    /**
     * 删除收集用户列表信息
     * 
     * @param id 收集用户列表主键
     * @return 结果
     */
    public int deleteInformationById(Long id);
}
