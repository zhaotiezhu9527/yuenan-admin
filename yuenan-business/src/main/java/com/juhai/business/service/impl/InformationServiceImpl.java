package com.juhai.business.service.impl;

import java.util.List;
import com.juhai.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.juhai.business.mapper.InformationMapper;
import com.juhai.business.domain.Information;
import com.juhai.business.service.IInformationService;

/**
 * 收集用户列表Service业务层处理
 * 
 * @author zhaotiezhu
 * @date 2023-12-15
 */
@Service
public class InformationServiceImpl implements IInformationService 
{
    @Autowired
    private InformationMapper informationMapper;

    /**
     * 查询收集用户列表
     * 
     * @param id 收集用户列表主键
     * @return 收集用户列表
     */
    @Override
    public Information selectInformationById(Long id)
    {
        return informationMapper.selectInformationById(id);
    }

    /**
     * 查询收集用户列表列表
     * 
     * @param information 收集用户列表
     * @return 收集用户列表
     */
    @Override
    public List<Information> selectInformationList(Information information)
    {
        return informationMapper.selectInformationList(information);
    }

    /**
     * 新增收集用户列表
     * 
     * @param information 收集用户列表
     * @return 结果
     */
    @Override
    public int insertInformation(Information information)
    {
        information.setCreateTime(DateUtils.getNowDate());
        return informationMapper.insertInformation(information);
    }

    /**
     * 修改收集用户列表
     * 
     * @param information 收集用户列表
     * @return 结果
     */
    @Override
    public int updateInformation(Information information)
    {
        return informationMapper.updateInformation(information);
    }

    /**
     * 批量删除收集用户列表
     * 
     * @param ids 需要删除的收集用户列表主键
     * @return 结果
     */
    @Override
    public int deleteInformationByIds(Long[] ids)
    {
        return informationMapper.deleteInformationByIds(ids);
    }

    /**
     * 删除收集用户列表信息
     * 
     * @param id 收集用户列表主键
     * @return 结果
     */
    @Override
    public int deleteInformationById(Long id)
    {
        return informationMapper.deleteInformationById(id);
    }
}
