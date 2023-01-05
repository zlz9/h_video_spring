package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.pojo.Role;
import com.zlz9.springbootmanager.service.RoleService;
import com.zlz9.springbootmanager.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 23340
* @description 针对表【h_role】的数据库操作Service实现
* @createDate 2023-01-01 14:20:42
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




