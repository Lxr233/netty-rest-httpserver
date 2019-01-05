package org.lxr.controllerregister;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ControllerInfo {
    private Method method;
    private Class<?> clazz;
    private Type returnType;

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

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
