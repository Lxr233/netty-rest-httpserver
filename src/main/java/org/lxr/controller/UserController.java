package org.lxr.controller;

import org.lxr.annotation.*;
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

    @Post
    @Path("/userinfo/post")
    public UserInfo setUserInfo(@RequestBody UserInfo userInfo){
        System.out.println("invoke setUserInfo()");
        userInfo.setId(userInfo.getId()+1);
        return userInfo;
    }
}
