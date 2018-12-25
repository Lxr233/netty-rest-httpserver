package org.lxr.rest;

import org.lxr.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 用于注册Controller映射信息
 */
public class ControllerRegister {

    public void registerController(String basePackage){
        Set<Class<?>> controllerClasses = findClassesByPackage(basePackage);
        for(Class<?> clazz : controllerClasses){
            registerClasses(clazz);
        }
    }

    private void registerClasses(Class<?> clazz) {
        StringBuilder path = new StringBuilder("");
        if (clazz.getAnnotation(RequestPath.class) != null) {
            RequestPath requestPath = clazz.getAnnotation(RequestPath.class);
            path.append(requestPath.value());
        }
        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            RequestMethodEnum requestMethodEnum = null;
            if(method.getAnnotation(Get.class)!=null){
                requestMethodEnum = RequestMethodEnum.GET;
            }
            else if(method.getAnnotation(Post.class)!=null){
                requestMethodEnum = RequestMethodEnum.POST;
            }
            
            if(method.getAnnotation(Path.class)!=null){
                path.append(method.getAnnotation(Path.class).value());
            }
            
            registerContext(requestMethodEnum,path,method);
        }
    }

    private void registerContext(RequestMethodEnum requestMethodEnum, StringBuilder path, Method method) {
    }

    /**
     * 扫描包路径下所有的class文件
     *
     * @param packageName
     * @return
     */
    private Set<Class<?>> findClassesByPackage(String packageName) {
        Set<Class<?>> classes = new LinkedHashSet<>(64);
        String pkgDirName = packageName.replace('.', '/');
        try {
            Enumeration<URL> urls = ControllerRegister.class.getClassLoader().getResources(pkgDirName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    // 如果是以文件的形式保存在服务器上
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 获取包的物理路径
                    findClassesByFile(filePath, packageName, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    findClassesByJar(packageName, jar, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 扫描包下的所有class文件
     *
     * @param path
     * @param packageName
     * @param classes
     */
    private void findClassesByFile(String path, String packageName, Set<Class<?>> classes) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(filter -> filter.isDirectory() || filter.getName().endsWith("class"));
        for (File f : files) {
            if (f.isDirectory()) {
                findClassesByFile(packageName + "." + f.getName(), path + "/" + f.getName(), classes);
                continue;
            }

            // 获取类名，去掉 ".class" 后缀
            String className = f.getName();
            className = packageName + "." + className.substring(0, className.length() - 6);

            // 加载类
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found"+ className);
            }
            if (clazz != null && clazz.getAnnotation(RestController.class) != null) {
                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }
    }

    /**
     * 扫描包路径下的所有class文件
     *
     * @param packageName
     *            包名
     * @param jar
     *            jar文件
     * @param classes
     *            保存包路径下class的集合
     */
    private static void findClassesByJar(String packageName, JarFile jar, Set<Class<?>> classes) {
        String pkgDir = packageName.replace(".", "/");
        Enumeration<JarEntry> entry = jar.entries();
        while (entry.hasMoreElements()) {
            JarEntry jarEntry = entry.nextElement();

            String jarName = jarEntry.getName();
            if (jarName.charAt(0) == '/') {
                jarName = jarName.substring(1);
            }
            if (jarEntry.isDirectory() || !jarName.startsWith(pkgDir) || !jarName.endsWith(".class")) {
                // 非指定包路径， 非class文件
                continue;
            }

            // 获取类名，去掉 ".class" 后缀
            String[] classNameSplit = jarName.split("/");
            String className = packageName + "." + classNameSplit[classNameSplit.length - 1];
            if(className.endsWith(".class")) {
                className = className.substring(0, className.length() - 6);
            }

            // 加载类
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found"+ className);
            }
            if (clazz != null && clazz.getAnnotation(RestController.class) != null) {
                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }
    }

}
