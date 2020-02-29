package com.atnanjing.demo.test.controller;

import com.atguigu.demo.dto.UserDTO;
import com.atguigu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserControllerTest {

    @Autowired
    private UserService userService;

    /*@RequestMapping(value = "{id}",method = RequestMethod.GET)
    public UserDTO getUser(@PathVariable("id") Long id){
        UserDTO userById = userService.findUserById(id);
        return userById;
    }*/
}
