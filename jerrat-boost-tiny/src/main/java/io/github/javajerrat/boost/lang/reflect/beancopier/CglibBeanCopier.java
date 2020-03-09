package io.github.javajerrat.boost.lang.reflect.beancopier;

import com.esotericsoftware.reflectasm.MethodAccess;
import io.github.javajerrat.boost.lang.reflect.Reflects;
import io.github.javajerrat.boost.lang.reflect.impl.Utils;
import io.github.javajerrat.boost.lang.reflect.impl.Utils.PropertyIndexTuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.concurrent.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/24
 */
@ThreadSafe
public class CglibBeanCopier<E, T> extends BeanCopier<E, T> {

    private final MethodAccess sourceAccess;
    private final MethodAccess targetAccess;
    private final Map<String, PropertyIndexTuple> sourceProperties;
    private final Map<String, PropertyIndexTuple> targetProperties;
    private final int[] getterToSetterMap;
    private final Set<String> commonPropertyNames;

    public CglibBeanCopier(@NotNull Class<E> sourceClass, @NotNull Class<T> targetClass, @Nullable List<String> includes, @Nullable List<String> excludes) {
        super(sourceClass, targetClass);
        if (Map.class.isAssignableFrom(sourceClass)) {
            this.sourceAccess = null;
            this.sourceProperties = null;
        } else {
            this.sourceAccess = MethodAccess.get(sourceClass);
            this.sourceProperties = Collections.unmodifiableMap(Utils.scanAllProperties(sourceAccess));
        }

        if (Map.class.isAssignableFrom(targetClass)) {
            this.targetAccess = null;
            this.targetProperties = null;
        } else {
            this.targetAccess = MethodAccess.get(targetClass);
            this.targetProperties = Collections.unmodifiableMap(Utils.scanAllProperties(targetAccess));
        }

        LinkedHashSet<String> commonSet = editableProperties(
            sourceProperties == null ? null : sourceProperties.keySet(),
            targetProperties == null ? null : targetProperties.keySet(),
            includes,
            excludes,
            (propertyName) -> {
                assert sourceProperties != null;
                assert targetProperties != null;
                PropertyIndexTuple sourceProperty = sourceProperties.get(propertyName);
                PropertyIndexTuple targetProperty = targetProperties.get(propertyName);
                return Objects.equals(
                    targetAccess.getReturnTypes()[targetProperty.getterIndex],
                    sourceAccess.getReturnTypes()[sourceProperty.getterIndex]);
            });

        this.commonPropertyNames = commonSet == null ? null : Collections.unmodifiableSet(commonSet);

        if (this.commonPropertyNames != null) {
            assert sourceProperties != null;
            assert targetProperties != null;
            this.getterToSetterMap = new int[this.commonPropertyNames.size() * 2];
            int p = 0;
            for (String propertyName : this.commonPropertyNames) {
                int getterIndex = sourceProperties.get(propertyName).getterIndex;
                int setterIndex = targetProperties.get(propertyName).setterIndex;
                getterToSetterMap[p++] = getterIndex;
                getterToSetterMap[p++] = setterIndex;
            }
        } else {
            this.getterToSetterMap = null;
        }
    }



    @Override
    protected void copyBean(@NotNull E source, @NotNull T target) {
        for (int i = 0; i < this.getterToSetterMap.length; i += 2) {
            int getterIndex = this.getterToSetterMap[i];
            int setterIndex = this.getterToSetterMap[i + 1];
            Object v = sourceAccess.invoke(source, getterIndex);
            targetAccess.invoke(target, setterIndex, v);
        }
    }

    @Override
    protected void mapToBean(@NotNull Map<String, Object> map, @NotNull Object bean) {
        for (String name : this.commonPropertyNames) {
            PropertyIndexTuple targetProperty = targetProperties.get(name);
            Object v = map.get(name);
            if (v != null &&
                Objects.equals(v.getClass(),
                    Reflects.primitiveToWrapper(targetAccess.getReturnTypes()[targetProperty.getterIndex]))) {
                targetAccess.invoke(bean, targetProperty.setterIndex, v);
            }
        }

    }

    @Override
    protected void beanToMap(@NotNull Object bean, @NotNull Map<String, Object> map) {
        for (String name : this.commonPropertyNames) {
            Object v = sourceAccess.invoke(bean, sourceProperties.get(name).getterIndex);
            if (v != null) {
                map.put(name, v);
            }
        }
    }
}
