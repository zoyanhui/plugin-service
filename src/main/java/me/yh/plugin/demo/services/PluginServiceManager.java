package me.yh.plugin.demo.services;

/**
 * Created by zhouyanhui on 16-5-28.
 */
public interface PluginServiceManager {
      void onNew(String jarUri, String pluginName, String service);

      void onChange(String jarUri, String pluginName, String service);

      void onRemove(String pluginName, String service);

      public ServiceInstance getService(String serviceName);

}
