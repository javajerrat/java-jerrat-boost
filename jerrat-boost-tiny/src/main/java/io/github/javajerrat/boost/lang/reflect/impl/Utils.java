package io.github.javajerrat.boost.lang.reflect.impl;

import com.esotericsoftware.reflectasm.MethodAccess;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/24
 */
public class Utils {

    @AllArgsConstructor
    public static class Property {

        public Method getter;

        public Method setter;
    }

    @AllArgsConstructor
    public static class PropertyIndexTuple {
        public int getterIndex;
        public int setterIndex;
    }

    public static Map<String, PropertyIndexTuple> scanAllProperties(MethodAccess methodAccess) {
        int count = methodAccess.getMethodNames().length;
        Map<String, PropertyIndexTuple> properties = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            String methodName = methodAccess.getMethodNames()[i];
            Class returnType = methodAccess.getReturnTypes()[i];
            int parameterCount = methodAccess.getParameterTypes()[i].length;
            int kind = isGetterOrSetter(methodName, returnType, parameterCount);
            if (kind != NO) {
                String propertyName = propertyName(methodName);
                if (!properties.containsKey(propertyName)) {
                    properties.put(propertyName, new PropertyIndexTuple(-1, -1));
                }
                if (kind == GETTER) {
                    properties.get(propertyName).getterIndex = i;
                } else {
                    properties.get(propertyName).setterIndex = i;
                }
            }
        }
        properties.values().removeIf(v -> v.setterIndex < 0 || v.getterIndex < 0);
        return properties;
    }

    public static Map<String, Property> scanAllProperties(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        int initSize = methods.length >= 8 ? methods.length / 2 : methods.length;
        Map<String, Property> properties = new LinkedHashMap<>(initSize);
        for (Method method : methods) {
            int kind = isGetterOrSetter(method.getName(), method.getReturnType(), method.getParameterCount());
            if (kind != NO) {
                String propertyName = propertyName(method.getName());
                if (!properties.containsKey(propertyName)) {
                    properties.put(propertyName, new Property(null, null));
                }
                if (kind == GETTER) {
                    properties.get(propertyName).getter = method;
                } else {
                    properties.get(propertyName).setter = method;
                }
            }
        }
        properties.values().removeIf(v -> v.setter == null || v.getter == null);
        return properties;
    }

    private static final int NO = 0;
    private static final int GETTER = 1;
    private static final int SETTER = 2;
    private static int isGetterOrSetter(String name, Class<?> returnType, int parameterCount) {
        String prefix = StringUtils.substring(name, 0, 3);
        boolean isReturnVoid = returnType.equals(Void.TYPE);
        boolean isGetter = !isReturnVoid && parameterCount == 0 && Objects.equals(prefix, "get");
        boolean isSetter = isReturnVoid && parameterCount == 1 && Objects.equals(prefix, "set");
        if (!isGetter && !isSetter) {
            return NO;
        } else {
            return isGetter ? GETTER : SETTER;
        }
    }

    private static String propertyName(String getterOrSetterName) {
        String name = StringUtils.substring(getterOrSetterName, 3);
        return StringUtils.substring(name, 0, 1).toLowerCase() + StringUtils.substring(name, 1);
    }

}
