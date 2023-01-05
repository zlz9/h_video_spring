package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.pojo.Menu;
import com.zlz9.springbootmanager.service.MenuService;
import com.zlz9.springbootmanager.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
* @author 23340
* @description 针对表【h_menu(菜单表)】的数据库操作Service实现
* @createDate 2023-01-01 14:20:42
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

}




