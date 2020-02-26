package com.atnanjing.demo.service.impl;

import com.atguigu.demo.dto.UserDTO;
import com.atguigu.demo.service.UserService;
import com.atnanjing.demo.dao.UserDO;
import com.atnanjing.demo.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO findUserById(Long id) {
        UserDO userDO = userMapper.selectByPrimaryKey(id);
        UserDTO userDTO=new UserDTO();
        BeanUtils.copyProperties(userDO,userDTO);
        return userDTO;
    }
}
