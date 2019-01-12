# netty-rest-httpserver
使用netty实现restful接口的http服务器。
模仿JAX-RS，使用注解来定义rest接口访问的路径和回调处理方法，实现效果如下：
```
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
```
