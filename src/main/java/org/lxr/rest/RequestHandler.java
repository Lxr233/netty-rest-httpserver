package org.lxr.rest;

import io.netty.handler.codec.http.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {

    public HttpResponse handleRequest(FullHttpRequest request, ControllerInfo controllerInfo) {
        Class<?> clazz = controllerInfo.getClazz();
        Method method = controllerInfo.getMethod();
        try {
            Object instance = clazz.newInstance();
            method.invoke(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    }
}
