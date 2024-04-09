package util;

import org.openqa.selenium.devtools.v85.runtime.Runtime;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MouseProxy implements InvocationHandler {

    private MouseInterface obj;

    public static MouseInterface newInstance(MouseInterface obj){
        return (MouseInterface) java.lang.reflect.Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new MouseProxy(obj));
    }

    private MouseProxy(MouseInterface obj){
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try{
            if(method.getName().startsWith("mouse")){
                obj.initMouseCoord();
            }
            result = method.invoke(obj, args);
        } catch (InvocationTargetException e){
            throw e.getTargetException();
        } catch (Exception e){
            throw new RuntimeException("Unexpected invocation Exception" + e.getMessage());
        }
        return result;
    }
}
