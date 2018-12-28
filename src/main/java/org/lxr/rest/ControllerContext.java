package org.lxr.rest;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存Controller注解信息
 */
public class ControllerContext {

    /**
     * <请求方法,<请求路径,Controller对应的方法>>
     */
    public static Map<RequestMethodEnum,Map<String, ControllerInfo>> controllerCacheMap = new HashMap<>();
}
