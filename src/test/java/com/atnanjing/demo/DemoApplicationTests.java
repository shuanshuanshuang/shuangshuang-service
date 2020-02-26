package com.atnanjing.demo;

import com.atguigu.demo.dto.UserDTO;
import com.atguigu.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    void contextLoads() {

        UserDTO userById = userService.findUserById(1L);
        System.out.println(userById);
    }

}
