package com.glume.glumemall.admin.dao;

import com.glume.glumemall.admin.entity.ConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置信息表
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:34
 */
@Mapper
public interface ConfigDao extends BaseMapper<ConfigEntity> {
	
}