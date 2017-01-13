package me.yh.plugin.demo.services.impl;

import me.yh.plugin.PluginClassLoader;
import me.yh.plugin.PluginManager;
import me.yh.plugin.demo.services.PluginServiceManager;
import me.yh.plugin.demo.services.ServiceInstance;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhouyanhui on 16-5-28.
 */
@Service("pluginServiceManager")
public class PluginServiceManagerImpl implements PluginServiceManager {

      private PluginManager pluginManager = new PluginManager();
      private ConcurrentHashMap<String, ServiceInstance> serviceMap = new ConcurrentHashMap<>();

      private ConcurrentHashMap<String, Object> serviceManipulatorLock = new ConcurrentHashMap();
      private Object lockObj = new Object();

      private boolean tryLock(String pluginName) {
            return null == this.serviceManipulatorLock.putIfAbsent(pluginName, this.lockObj);
      }

      private void unlock(String pluginName) {
            this.serviceManipulatorLock.remove(pluginName);
      }

      @Override
      public void onNew(String jarUri, String pluginName, String service) {
            while (!tryLock(service)) {
                  try {
                        Thread.sleep(1000);
                  } catch (InterruptedException e) {
                        e.printStackTrace();
                  }
            }
            try {
                  rebuildService(jarUri, pluginName, service);
            } finally {
                  unlock(service);
            }

      }

      private void rebuildService(String jarUri, String pluginName, String service) {
            PluginClassLoader classLoader = null;
            try {
                  pluginManager.loadPlugin(jarUri, pluginName);
                  classLoader = pluginManager.getLoader(pluginName);
            }catch (Exception e){
                  // TODO logger
                  System.out.println("load plugin failed");
                  return;
            }
            try {
                  Class<?> aClass = classLoader.loadClass(service);
                  serviceMap.put(service, new SimpleServiceInstance(aClass, aClass.newInstance()));
            } catch (ClassNotFoundException e) {
                  // TODO error
            } catch (InstantiationException e) {
                  e.printStackTrace();
            } catch (IllegalAccessException e) {
                  e.printStackTrace();
            }
      }

      private void removeService(String pluginName, String service) {
            serviceMap.remove(service);
            pluginManager.unloadPlugin(pluginName);
      }

      @Override
      public void onChange(String jarUri, String pluginName, String service) {
            while (!tryLock(service)) {
                  try {
                        Thread.sleep(1000);
                  } catch (InterruptedException e) {
                        e.printStackTrace();
                  }
            }
            try {
                  removeService(pluginName, service);
                  rebuildService(jarUri, pluginName, service);
            } finally {
                  unlock(service);
            }
      }

      @Override
      public void onRemove(String pluginName, String service) {
            while (!tryLock(service)) {
                  try {
                        Thread.sleep(1000);
                  } catch (InterruptedException e) {
                        e.printStackTrace();
                  }
            }
            try {
                  removeService(pluginName, service);
            } finally {
                  unlock(service);
            }
      }

      @Override
      public ServiceInstance getService(String serviceName) {
            return serviceMap.get(serviceName);
      }

}

