package org.lxr.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.lxr.controllerregister.ControllerInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {

    public HttpResponse handleRequest(FullHttpRequest request, ControllerInfo controllerInfo) {
        Class<?> clazz = controllerInfo.getClazz();
        Class<?> requestBodyClazz = controllerInfo.getRequestBody();
        ByteBuf jsonBuf = request.content();
        String requestContent = jsonBuf.toString(CharsetUtil.UTF_8);
        Method method = controllerInfo.getMethod();
        try {
            Object instance = clazz.newInstance();
            Object returnObj = null;
            if(requestBodyClazz==null){
                returnObj = method.invoke(instance);
            }
            else {
                returnObj = method.invoke(instance, JSONObject.parseObject(requestContent,requestBodyClazz));
            }
            //如果方法返回值是void，则returnObj为null
            String returnMsg = JSON.toJSONString(returnObj);
            System.out.println("return:"+returnMsg);
            return makeResponse(request,returnMsg);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpResponse makeResponse(FullHttpRequest request, String returnMsg) {
        ByteBuf rspBuf = Unpooled.copiedBuffer(returnMsg.getBytes());
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, rspBuf);
        response.headers().set("Content-Type","text/json");
        response.headers().set("Content-Lenght",rspBuf.readableBytes());
        return response;
    }
}
