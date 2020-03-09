/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.github.javajerrat.boost.lang.reflect;

import io.github.javajerrat.boost.lang.string.Strings;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/11
 * @see FieldUtils
 */
public class Reflects extends ClassUtils {

    /**
     * Create jdk dynamic proxy object
     * @param interfaceType Proxy interface
     * @param handler Invocation handler
     * @return A jdk dynamic proxy object
     */
    public static <T> T newProxy(InvocationHandler handler, Class<T> interfaceType) {
        Object object = Proxy.newProxyInstance(
            interfaceType.getClassLoader(), new Class<?>[] {interfaceType}, handler);
        return interfaceType.cast(object);
    }

    /**
     * @see FieldUtils#getFieldsListWithAnnotation(Class, Class)
     */
    public static List<Field> getFieldsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
        return FieldUtils.getFieldsListWithAnnotation(cls, annotationCls);
    }

    /**
     * @see FieldUtils#getAllFieldsList(Class) 
     */
    public static List<Field> getAllFieldsList(Class<?> clazz) {
        return FieldUtils.getAllFieldsList(clazz);
    }

    /**
     * Get the reflection of all fields of the bean.
     * If the same field appears multiple times on the inheritance chain,
     * they will be put into a List with the child class first and the parent class later.
     */
    public static Map<String, List<Field>> getAllFields(Class<?> clazz) {
        Map<String, List<Field>> allFields = new LinkedHashMap<>();
        for (Class<?> c = clazz; c!= null; c = c.getSuperclass()) {
            for (Field f : c.getDeclaredFields()) {
                List<Field> fields = allFields.getOrDefault(f.getName(), new ArrayList<>());
                fields.add(f);
                allFields.put(f.getName(), fields);
            }
        }
        return allFields;
    }
    /**
     * Get the reflection of those fields with given annotations.
     * If the same field appears multiple times on the inheritance chain,
     * they will be put into a List with the child class first and the parent class later.
     */
    @SafeVarargs
    public static Map<String, List<Field>> getAllFieldsWithAnnotationAllMatch(Class<?> clazz, Class<? extends Annotation>... annotations) {
        Map<String, List<Field>> allFields = new LinkedHashMap<>();
        for (Class<?> c = clazz; c!= null; c = c.getSuperclass()) {
            for (Field f : c.getDeclaredFields()) {
                boolean matched = true;
                for (Class<? extends Annotation> annotation : annotations) {
                    if (!f.isAnnotationPresent(annotation)) {
                        matched = false;
                        break;
                    }
                }
                if (matched) {
                    List<Field> fields = allFields.getOrDefault(f.getName(), new ArrayList<>());
                    fields.add(f);
                    allFields.put(f.getName(), fields);
                }
            }
        }
        return allFields;
    }

    /**
     * Get the reflection of those fields with given annotations.
     * If the same field appears multiple times on the inheritance chain,
     * they will be put into a List with the child class first and the parent class later.
     */
    @SafeVarargs
    public static Map<String, List<Field>> getAllFieldsWithAnnotationAnyMatch(Class<?> clazz, Class<? extends Annotation>... annotations) {
        Map<String, List<Field>> allFields = new LinkedHashMap<>();
        for (Class<?> c = clazz; c!= null; c = c.getSuperclass()) {
            for (Field f : c.getDeclaredFields()) {
                boolean matched = false;
                for (Class<? extends Annotation> annotation : annotations) {
                    if (f.isAnnotationPresent(annotation)) {
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    List<Field> fields = allFields.getOrDefault(f.getName(), new ArrayList<>());
                    fields.add(f);
                    allFields.put(f.getName(), fields);
                }
            }
        }
        return allFields;
    }

    /**
     * Search the class inheritance tree.
     * If there is a parameterized type of the raw class {@code matched}, return it.
     *
     * For example, If {@code ExampleList} is a {@code List<String>},
     * You can use this function to get the generic parameter @{code String}.
     *
     * @param matched Searched raw class
     * @return If found, returns its reflection, otherwise returns null.
     */
    @Nullable
    public static ParameterizedType getGenericSuperType(@NotNull Class<?> clazz, @NotNull Class<?> matched) {
        Type type = clazz.getGenericSuperclass();
        if (isParameterizedType(type, matched)) {
            return (ParameterizedType)type;
        }
        Type[] interfaces = clazz.getGenericInterfaces();
        interfaces = interfaces == null ? new Type[0] : interfaces;
        for (Type i : interfaces) {
            if (isParameterizedType(i, matched)) {
                return (ParameterizedType) i;
            }
        }

        if (type instanceof Class<?>) {
            ParameterizedType result = getGenericSuperType((Class<?>) type, matched);
            if (result != null) {
                return result;
            }
        }
        for (Type i : interfaces) {
            if (i instanceof Class<?>) {
                ParameterizedType result = getGenericSuperType((Class<?>) i, matched);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static boolean isParameterizedType(Type type, Class<?> rawType) {
        return type instanceof ParameterizedType &&
            rawType.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType());
    }


    @FunctionalInterface
    public interface MethodReferenceGetter<T, R> extends Serializable, Function<T, R> {
    }


    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class MethodReferenceTuple {

        public final String className;
        public final String methodName;
    }

    /**
     * @param methodReferenceGetter Method reference of getter
     * @return Reflection on getter, including class name and getter name
     */
    @Nullable
    public static <T, R> MethodReferenceTuple getLambda(MethodReferenceGetter<T, R> methodReferenceGetter) {
        @Nullable SerializedLambda lambda = getLambda((Serializable) methodReferenceGetter);
        if (lambda != null) {
            return new MethodReferenceTuple(
                Strings.replace(lambda.getImplClass(), "/", "."), lambda.getImplMethodName());
        } else {
            return null;
        }
    }

    /**
     * @param lambda Method reference
     * @return SerializedLambda
     */
    @Nullable
    private static SerializedLambda getLambda(Serializable lambda) {
        // Thanks for: https://stackoverflow.com/questions/31178103/how-can-i-find-the-target-of-a-java8-method-reference
        for (Class<?> clazz = lambda.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getDeclaredMethod("writeReplace");
                method.setAccessible(true);
                Object replacement = method.invoke(lambda);
                if (!(replacement instanceof SerializedLambda)) {
                    break; // custom interface implementation
                }
                return (SerializedLambda) replacement;
            } catch (NoSuchMethodException ignored) {
            } catch (IllegalAccessException | InvocationTargetException e) {
                break;
            }
        }

        return null;
    }

}