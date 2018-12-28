package org.lxr.rest;

import java.lang.reflect.Method;

public class ControllerInfo {
    private Method method;
    private Class<?> clazz;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
