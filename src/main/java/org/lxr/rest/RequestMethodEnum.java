package org.lxr.rest;

/**
 * Request请求的枚举
 */
public enum RequestMethodEnum {
    GET,POST,DELETE,PUT;

    public static RequestMethodEnum getRequestMethodEnum(String method){
        method = method.toLowerCase();
        switch (method){
            case "get":
                return GET;
            case "post":
                return POST;
            case "delete":
                return DELETE;
            case "put":
                return PUT;
            default:
                return null;
        }
    }
}
