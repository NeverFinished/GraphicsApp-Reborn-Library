package de.ur.mi.oop.launcher;

import de.ur.mi.oop.app.AppManager;
import de.ur.mi.oop.app.GraphicsApp;
import de.ur.mi.oop.app.Config;
import de.ur.mi.oop.app.SimpleGraphicsApp;

import java.lang.reflect.InvocationTargetException;

/**
 * Der GraphicsAppLauncher startet eine GraphicsApp-Anwendung
 */
public class GraphicsAppLauncher {

    public static final String PACKAGE_DELIMITER = ".";

    public static void launch() {
        String launcherName = getCallingClassNameFromStacktrace();
        launch(launcherName);
    }

    private static String getCallingClassNameFromStacktrace() {
        StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
        String launcherName = stackTraceElements[stackTraceElements.length - 1].getClassName();
        if(launcherName.contains(PACKAGE_DELIMITER)) {
            launcherName = launcherName.substring(launcherName.lastIndexOf(PACKAGE_DELIMITER)+1);
        }
        return launcherName;
    }

    public static void launch(String appName) {
        Config config = new Config();
        launch(appName, config);
    }

    public static void launch(String appName, Config config) {
        // Try to enable GPU rendering
        System.setProperty("sun.java2d.opengl", "true");
        try {
            config.setTitle(appName);
            GraphicsApp app = getGraphicsAppInstance(appName);
            AppManager appManager = new AppManager(app, config);
            config.setListener(appManager);
            app.setConfig(config);
            app.initialize(); // calls GraphicsApp.init once
            appManager.start(); // starts loop to call GraphicsApp.draw repeatedly
            if (app instanceof SimpleGraphicsApp sapp) {
                sapp.run();
                if (sapp.isRunning() && sapp.overridesRun()) {
                    System.err.println("Warning: SimpleGraphicsApp.run() returned before window close, intentional?");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static GraphicsApp getGraphicsAppInstance(String appName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
        String launcherName = stackTraceElements[stackTraceElements.length - 1].getClassName();
        String className;
        int packageNameCutOffIndex = launcherName.lastIndexOf(PACKAGE_DELIMITER);
        // If Launcher was called from GraphicsApp
        if(packageNameCutOffIndex == -1) {
            className = appName;
        // If Launcher was called from outside GraphicsApp
        } else {
            String packageName = launcherName.substring(0, launcherName.lastIndexOf(PACKAGE_DELIMITER));
            className = packageName + PACKAGE_DELIMITER + appName;
        }
        Class<?> appClass = Class.forName(className);
        return (GraphicsApp) appClass.getConstructor().newInstance();
    }
}
