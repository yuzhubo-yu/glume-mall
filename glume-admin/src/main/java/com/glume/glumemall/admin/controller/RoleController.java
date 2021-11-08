package com.glume.glumemall.admin.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.utils.*;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.service.RoleService;

import javax.servlet.http.HttpServletRequest;


/**
 * 角色
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@RestController
@RequestMapping("admin/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Value("${jwt.header}")
    private String headerToken;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "角色列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = roleService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
     * 角色菜单
     */
    @GetMapping("/menuTree")
    @ApiOperation(value = "角色下拉菜单权限列表")
    public R menuTree(@RequestParam(value = "roleId",required = false) Long roleId) {
        HashMap<String, Object> data = roleService.menuTreeService(roleId);
        return R.ok().put("data",data);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{roleId}")
    public R info(@PathVariable("roleId") Long roleId){
		RoleEntity role = roleService.getById(roleId);

        return R.ok().put("role", role);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName",value = "角色名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "roleTag",value = "角色标签",required = true,dataType = "String"),
            @ApiImplicitParam(name = "remark",value = "备注",dataType = "String"),
    })
    public R save(@Validated(AddGroup.class) RoleEntity roleEntity, HttpServletRequest httpServletRequest){
        // 获取当前操作用户Id
        String token = httpServletRequest.getHeader(headerToken);
        String username = SpringUtils.getBean(JwtUtils.class).getUserNameFromToken(token);
        UserEntity userDetail = SpringUtils.getBean(UserService.class).getByUserDetail(username);
        // 设置创建时间
        roleEntity.setCreateUserId(userDetail.getUserId());
        roleEntity.setCreateTime(new Date(DateUtils.getSysDateTime()));
        roleService.saveMyRole(roleEntity);
        return R.ok("添加成功！");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RoleEntity role){
		roleService.updateById(role);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] roleIds){
		roleService.removeByIds(Arrays.asList(roleIds));

        return R.ok();
    }

}
