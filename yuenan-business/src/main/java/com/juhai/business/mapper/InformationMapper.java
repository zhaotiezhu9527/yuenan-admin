package com.juhai.business.mapper;

import java.util.List;
import com.juhai.business.domain.Information;

/**
 * 收集用户列表Mapper接口
 * 
 * @author zhaotiezhu
 * @date 2023-12-15
 */
public interface InformationMapper 
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
     * 删除收集用户列表
     * 
     * @param id 收集用户列表主键
     * @return 结果
     */
    public int deleteInformationById(Long id);

    /**
     * 批量删除收集用户列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInformationByIds(Long[] ids);
}
