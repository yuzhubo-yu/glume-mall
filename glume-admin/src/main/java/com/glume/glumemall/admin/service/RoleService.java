package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.admin.entity.RoleEntity;

import java.util.Map;

/**
 * 角色
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
public interface RoleService extends IService<RoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    RoleEntity getRoleDetail(Long role_id);
}
