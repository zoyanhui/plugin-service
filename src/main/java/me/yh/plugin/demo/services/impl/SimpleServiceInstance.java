package me.yh.plugin.demo.services.impl;

import me.yh.plugin.demo.services.ServiceInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhouyanhui on 16-6-1.
 */
public class SimpleServiceInstance implements ServiceInstance {

      private Class<?> aClass;

      public SimpleServiceInstance(Class<?> clazz, Object o){
            aClass = clazz;
      }

      @Override
      public Object invoke(String method, Object[] parameters) {
            Class[] paramTypes = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                  paramTypes[i] = parameters[i].getClass();
            }
            try {
                  Method invokeMethod = aClass.getMethod(method, paramTypes);
                  return invokeMethod.invoke(aClass.newInstance(), parameters);
            } catch (NoSuchMethodException e) {
                  e.printStackTrace();
            } catch (IllegalAccessException e) {
                  e.printStackTrace();
            } catch (InstantiationException e) {
                  e.printStackTrace();
            } catch (InvocationTargetException e) {
                  e.printStackTrace();
            }
            return null;
      }
}
