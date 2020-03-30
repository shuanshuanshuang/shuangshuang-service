package com.atnanjing.demo.test.controller;

import com.atguigu.demo.service.UserService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RocketMqController {

    private static final Logger logger = LoggerFactory.getLogger(RocketMqController.class);

    /**使用RocketMq的生产者*/
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private UserService userService;


    @RequestMapping("/index")
    public void tsestRocketMq()throws MQClientException, RemotingException, MQBrokerException, InterruptedException{
        String mes="demo msg test";
        Message sendMsg=new Message("DemoTopic","DemoTag",mes.getBytes());
        //默认3秒超时
        SendResult sendResult=defaultMQProducer.send(sendMsg);
        logger.info("消息发送响应信息："+sendResult.toString());

    }

    @RequestMapping("/addUser")
    public void addUser(){
        userService.addBatchUsers();
    }

    @RequestMapping("/sendRocketMqUser")
    public void sendRocketMqUser(){
        userService.sendUserRocketMq();
    }




}
