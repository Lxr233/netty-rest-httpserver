package org.lxr.rest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.lang.reflect.Method;
import java.util.Map;

public class RequestDispatcher {

    public void doDispatcher(ChannelHandlerContext ctx, FullHttpRequest request) {
        RequestMethodEnum requestMethodEnum = RequestMethodEnum.getRequestMethodEnum(request.method().name());
        if(requestMethodEnum==null){
            /**
             * 不支持RequestMethodEnum以外类型的请求
             */
            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            ctx.writeAndFlush(response);
            return;
        }
        String uri = request.uri();
        Map<RequestMethodEnum, Map<String, Method>> controllerCacheMap = ControllerContext.controllerCacheMap;
        /**
         * 如果请求是Controller中设置过的则回调相应的方法，否则直接返回
         */
        if(controllerCacheMap.containsKey(requestMethodEnum)&&controllerCacheMap.get(requestMethodEnum).containsKey(uri)){
            Method method = controllerCacheMap.get(requestMethodEnum).get(uri);
        }
        else{
            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            ctx.writeAndFlush(response);
        }

    }
}
