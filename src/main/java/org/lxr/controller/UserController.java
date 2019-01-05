package org.lxr.controller;

import org.lxr.annotation.Get;
import org.lxr.annotation.Path;
import org.lxr.annotation.RequestPath;
import org.lxr.annotation.RestController;
import org.lxr.pojo.UserInfo;

@RestController
@RequestPath("/user")
public class UserController {

    @Get
    @Path("/healthcheck")
    public void healthCheck(){
    }


    @Get
    @Path("/userinfo/example")
    public UserInfo getUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(1);
        userInfo.setId(1);
        userInfo.setName("example");
        System.out.println("invoke getUserInfo()");
        return userInfo;
    }
}