package io.github.javajerrat.boost.lang.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import io.github.javajerrat.boost.codec.json.Jacksons;
import io.github.javajerrat.boost.lang.reflect.beanaccessor.BeanAccessor;
import io.github.javajerrat.boost.lang.reflect.beanaccessor.CglibBeanAccessor;
import io.github.javajerrat.boost.lang.reflect.beanaccessor.JavaReflectBeanAccessor;
import io.github.javajerrat.boost.lang.reflect.beancopier.BeanCopier;
import io.github.javajerrat.boost.lang.reflect.beancopier.CglibBeanCopier;
import io.github.javajerrat.boost.lang.reflect.beancopier.JavaReflectBeanCopier;
import io.github.javajerrat.boost.lang.reflect.exception.PropertyNotFoundException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/21
 */
@Beta
public abstract class Beans {


    public enum ReflectMode {

        /**
         *
         */
        JAVA,

        /**
         *
         */
        CGLIB;
    }



    @SneakyThrows
    public static <T> T copy(@NotNull Object source, @NotNull Supplier<T> targetConstructor) {
        T target = targetConstructor.get();
        copy(source, target);
        return target;
    }

    @SneakyThrows
    public static <T> T copy(@NotNull Object source, @NotNull Class<T> targetClass) {
        Preconditions.checkNotNull(source, "Source must not be null");
        T target = targetClass.getConstructor().newInstance();
        copy(source, target);
        return target;
    }

    public static <T> void copy(Object source, T target) {
        copy(source, target, null);
    }


    @SuppressWarnings("unchecked")
    public static  <T, E> void copy(@NotNull Object source, @NotNull T target, @Nullable Class<E> includes) {
        Preconditions.checkNotNull(source, "Source must not be null");
        Preconditions.checkNotNull(target, "Target must not be null");

        List<String> includesList = includes == null ? null : propertyNames(includes);
        JavaReflectBeanCopier copier = new JavaReflectBeanCopier<>(source.getClass(), target.getClass(),
            includesList, null);
        copier.copy(source, target);
    }

    public static <E, T> List<T> copy(@NotNull List<E> sources, @NotNull Class<E> sourceClass, @NotNull Supplier<T> targetConstructor) {
        Class<T> targetClass = (Class<T>) targetConstructor.get().getClass();
        return copier(sourceClass, targetClass, null, null)
            .copy(sources, targetConstructor);
    }

    public static <E, T> List<T> copy(@NotNull List<E> sources, @NotNull Class<E> sourceClass, @NotNull Class<T> targetClass) {
        return copier(sourceClass, targetClass, null, null)
            .copy(sources, targetClass);
    }



    public static <T, E> BeanCopier<E, T> copier(@NotNull ReflectMode reflectMode, @NotNull Class<E> sourceClass, @NotNull Class<T> targetClass, @Nullable List<String> includes, @Nullable List<String> excludes) {
        switch (reflectMode) {
            case JAVA:
                return new JavaReflectBeanCopier<>(sourceClass, targetClass, includes, excludes);
            case CGLIB:
                return new CglibBeanCopier<>(sourceClass, targetClass, includes, excludes);
            default:
                throw new IllegalStateException();
        }
    }

    public static <T, E> BeanCopier<E, T> copier(@NotNull Class<E> sourceClass, @NotNull Class<T> targetClass, @Nullable List<String> includes, @Nullable List<String> excludes) {
        return copier(ReflectMode.JAVA, sourceClass, targetClass, includes, excludes);
    }

    @SneakyThrows
    public static <T> void set(Object bean, String propertyName, Object value) throws PropertyNotFoundException {
        String setterName = propertyToSetter(propertyName);
        for (Method method : bean.getClass().getMethods()) {
            if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                method.invoke(bean, value);
                break;
            }
        }
        throw new PropertyNotFoundException(propertyName);
    }

    @SneakyThrows
    public static <T> void set(Object bean, String property, Class<T> valueType, T value) throws PropertyNotFoundException {
        String setterName = propertyToSetter(property);
        Method method;
        try {
            method = bean.getClass().getMethod(setterName, valueType);
        } catch (NoSuchMethodException e) {
            throw new PropertyNotFoundException(property, e);
        }
        method.invoke(bean, value);
    }

    @SneakyThrows
    public static Object get(Object bean, String property) throws PropertyNotFoundException {
        @NotNull String getterName = propertyToGetter(property);
        Method method;
        try {
            method = bean.getClass().getMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new PropertyNotFoundException(property, e);
        }
        return method.invoke(bean);
    }

    public static <T> BeanAccessor<T> beanAccessor(Class<T> clazz) {
        return beanAccessor(ReflectMode.JAVA, clazz);
    }

    public static <T> BeanAccessor<T> beanAccessor(ReflectMode reflectMode, Class<T> clazz) {
        switch (reflectMode) {
            case JAVA:
                return new JavaReflectBeanAccessor<>(clazz);
            case CGLIB:
                return new CglibBeanAccessor<>(clazz);
            default:
                throw new IllegalStateException();
        }
    }

    @SneakyThrows
    public static List<String> propertyNames(Class<?> clazz) {
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        return Arrays.stream(propertyDescriptors).map(PropertyDescriptor::getName).collect(Collectors.toList());
    }



    @Nullable
    @Contract("null -> null")
    public static String getterToProperty(@Nullable String getterName) {
        if (getterName != null &&
            Objects.equals(StringUtils.substring(getterName, 0, 3), "get")) {
            return StringUtils.substring(getterName, 3, 4).toLowerCase()
                + StringUtils.substring(getterName, 4);
        } else {
            return getterName;
        }
    }

    @Nullable
    @Contract("null -> null")
    public static String setterToProperty(@Nullable String getterName) {
        if (getterName != null &&
            Objects.equals(StringUtils.substring(getterName, 0, 3), "set")) {
            return StringUtils.substring(getterName, 3, 4).toLowerCase()
                + StringUtils.substring(getterName, 4);
        } else {
            return getterName;
        }
    }

    @NotNull
    public static String propertyToGetter(@NotNull String propertyName) {
        Preconditions.checkArgument(!propertyName.isEmpty());
        return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    @NotNull
    public static String propertyToSetter(@NotNull String propertyName) {
        Preconditions.checkArgument(!propertyName.isEmpty());
        return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    public static <T> T deepCopy(Object src, Class<T> dstSupplier) {
        return Jacksons.DEFAULT_OBJECT_MAPPER.convertValue(src, dstSupplier);
    }

}
