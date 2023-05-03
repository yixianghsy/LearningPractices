package cn.bugstack.springframework.util;

public class ClassUtils {
    public  static  ClassLoader getDefaultClassLoader(){
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex){
            // Cannot access thread context ClassLoader - falling back to system class loader...

        }
        if (cl == null){
            cl = ClassUtils.class.getClassLoader();
        }
        return  cl;
    }
}
