package org.lxr.rest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存Controller注解信息
 */
public class ControllerContext {

    /**
     * <请求方法,<请求路径,Controller对应的方法>>
     */
    Map<RequestMethodEnum,Map<String, Method>> controllerCacheMap = new HashMap<>();
}
