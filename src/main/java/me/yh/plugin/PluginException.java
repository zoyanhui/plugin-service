package me.yh.plugin;

/**
 * Created by zhouyanhui on 16-6-1.
 */
public class PluginException extends RuntimeException {
      private static final long serialVersionUID = 1L;

      public PluginException(){
            super();
      }

      public PluginException(String msg){
            super(msg);
      }

      public PluginException(Throwable e){
            super(e);
      }

      public PluginException(String msg, Throwable e){
            super(msg,e);
      }
}
