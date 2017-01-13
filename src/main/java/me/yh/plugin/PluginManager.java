package me.yh.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouyanhui on 16-6-1.
 */
public class PluginManager {
      private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);

      private Map<String, PluginClassLoader> pluginMap = new HashMap<String, PluginClassLoader>();

      public PluginManager() {

      }

      private void addLoader(String pluginName, PluginClassLoader loader) {
            this.pluginMap.put(pluginName, loader);
      }

      public PluginClassLoader getLoader(String pluginName) {
            return this.pluginMap.get(pluginName);
      }

      /**
       * @param pluginurl  e.g.: "jar:file:/D:/testclassloader/" + pluginName + ".jar!/"
       * @param pluginName
       */
      public void loadPlugin(String pluginurl, String pluginName) {
            this.pluginMap.remove(pluginName);
            PluginClassLoader loader = new PluginClassLoader();
            URL url = null;
            try {
                  url = new URL(pluginurl);
            } catch (MalformedURLException e) {
                  logger.error("load plugin {} url exception, pluginurl: {}", new Object[]{pluginName, pluginurl, e});
                  throw new PluginException("load plugin " + pluginName + " failed", e);
            }
            try {
                  loader.addURLFile(url);
                  addLoader(pluginName, loader);
                  logger.info("load plugin {} success", pluginName);
            } catch (Exception e) {
                  logger.error("load plugin {} url exception, pluginurl: {}", new Object[]{pluginName, pluginurl, e});
                  throw new PluginException("load plugin " + pluginName + " failed", e);
            }
      }

      /**
       * @param pluginName
       */
      public void unloadPlugin(String pluginName) {
            try {
                  this.pluginMap.get(pluginName).unloadJarFiles();
                  this.pluginMap.remove(pluginName);
                  logger.info("unload plugin {} success", pluginName);
            } catch (Exception e) {
                  logger.error("unload plugin {} url exception", pluginName, e);
                  throw new PluginException("unload plugin " + pluginName + " failed", e);
            }
      }
}

