package jvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * @ClassName : HelloClassLoader  //类名
 * @Description : 自定义ClassLoader  //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-10-17 15:13  //时间
 */
public class HelloClassLoader extends ClassLoader{
    public static final String CLASS_SUFFIX = ".xlass";

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 加载xlass文件
        HelloClassLoader classLoader = new HelloClassLoader();
        Class<?> hello = classLoader.loadClass("Hello");
        // 实例化Hello类，调用hello方法
        Object instance = hello.newInstance();
        hello.getMethod("hello").invoke(instance);


    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File(getAbsolutePath(name+CLASS_SUFFIX));
        int length = (int) f.length();
        byte[] bytes = new byte[length];

        try {
            new FileInputStream(f).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return super.findClass(name);
        }

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(255 - bytes[i]);
        }

        return defineClass(name, bytes, 0, bytes.length);
    }

    /**
     * 根据相对于resources的路径，获取绝对路径
     * @param relativePath
     * @return
     */
    public String getAbsolutePath(String relativePath){
        ClassLoader parentClassLoader = this.getClass().getClassLoader();
        return parentClassLoader.getResource(relativePath).getPath();
    }
}
