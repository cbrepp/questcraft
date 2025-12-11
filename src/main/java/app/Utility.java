package app;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Utility {
    
    public static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0 && index < fileName.length() - 1) {
            return fileName.substring(index + 1).toLowerCase();
        }
        return "";
    }
    
    public static Object instance(String className) {
        return instance(className, null);
    }
    
    public static Object instance(String className, Object parameter) {
        Object instance = null;
        try {
            Class<?> appClass = Class.forName(className);
            try {
                try {
                    if (parameter == null) {
                        Constructor<?> constructor = appClass.getConstructor();
                        instance = constructor.newInstance();
                    } else {
                        Constructor<?> constructor = appClass.getConstructor(parameter.getClass());
                        instance = constructor.newInstance(parameter);
                    }
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    System.err.println("ERROR: " + e.toString());
                }
            } catch (NoSuchMethodException | SecurityException e) {
                System.err.println("ERROR: " + e.toString());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: " + e.toString());
        }
        return instance;
    }
    
    public static Object getValue(Object object, Field field) {
        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            System.err.println("ERROR: " + e.toString());
        }
        return value;
    }
    
    public static String toPascalCase(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        String newString = Character.toUpperCase(string.charAt(0)) + string.substring(1);
        return newString;
    }
    
}
