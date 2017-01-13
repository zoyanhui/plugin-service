package me.yh.plugin.demo;

import me.yh.plugin.demo.services.PluginServiceManager;
import me.yh.plugin.demo.services.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyanhui on 16-5-28.
 */
public class Test {
      private static final Logger logger = LoggerFactory.getLogger(Test.class);

      public static void main(String[] args) {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
            PluginServiceManager pluginServiceManager = (PluginServiceManager) ctx.getBean("pluginServiceManager");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String cmd = null;
            try {
                  cmd = br.readLine();
            } catch (IOException e) {
                  e.printStackTrace();
            }

            while (!cmd.equals("bye")) {
                  String[] cmds = cmd.split(" ");
                  switch (cmds[0]) {
                        case "do":
                              ServiceInstance service =
                                          pluginServiceManager.getService(cmds[1]);
                              List<String> paramter = new ArrayList<String>();
                              for (int i = 3; i < cmds.length; i++) {
                                    paramter.add(cmds[i]);
                              }
                              if (service == null) {
                                    logger.info("no such service {}", cmds[1]);
                              } else {
                                    service.invoke(cmds[2], paramter.toArray());
                              }
                              break;
                        case "load":
                              pluginServiceManager.onNew(cmds[1], cmds[2], cmds[3]);
                              break;
                        case "reload":
                              pluginServiceManager.onChange(cmds[1], cmds[2], cmds[3]);
                              break;
                        case "unload":
                              pluginServiceManager.onRemove(cmds[1], cmds[2]);
                              break;
                  }
                  try {
                        cmd = br.readLine();
                  } catch (IOException e) {
                        e.printStackTrace();
                  }
            }


//            ClassLoader classLoader = Test.class.getClassLoader();
//            URL url = null;
//            try {
//                  url = new URL();
//            } catch (MalformedURLException e) {
//                  e.printStackTrace();
//            }
//            while(classLoader.getParent() != null){
//                  classLoader = classLoader.getParent();
//            }
//            URLClassLoader myClassLoader = URLClassLoader.newInstance(new URL[]{url}, URLClassLoader.getSystemClassLoader());
//            try{
//                  Class<?> aClass1 = Class.forName("DEMO_CLASS_NAME", true, myClassLoader);
//                  System.out.println("aClass.getName() = " + aClass1.getName());
//                  Method start = aClass1.getMethod("start");
//                  Constructor<?> constructor = aClass1.getConstructor(List.class, String.class);
//                  Object o = constructor.newInstance(new ArrayList<String>(){{
//                        add("bg-list-action");
//                        add("bg-action");
//                  }}, "minegroup");
//
//                  start.invoke(o);
//
//            } catch (ClassNotFoundException e) {
//                  System.out.println("class not found");
//                  e.printStackTrace();
//            } catch (InstantiationException e) {
//                  e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                  e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                  e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                  e.printStackTrace();
//            }
      }
}
