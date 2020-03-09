package io.github.javajerrat.boost.lang.reflect.beancopier;

import com.google.common.base.Preconditions;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/24
 */
@ThreadSafe
public abstract class BeanCopier<E, T> {

    protected final int type;

    private static final int ALL_MAP = 0;
    private static final int ONLY_SOURCE_MAP = 1;
    private static final int ONLY_TARGET_MAP = 2;
    private static final int ALL_OBJECT = 3;

    BeanCopier(@NotNull Class<E> sourceClass, @NotNull Class<T> targetClass) {

        boolean isSourceMap = Map.class.isAssignableFrom(sourceClass);
        boolean isTargetMap = Map.class.isAssignableFrom(targetClass);

        if (isSourceMap && isTargetMap) {
            type = ALL_MAP;
        } else if (isSourceMap) {
            type = ONLY_SOURCE_MAP;
        } else if (isTargetMap) {
            type = ONLY_TARGET_MAP;
        } else {
            type = ALL_OBJECT;
        }
    }

    public List<T> copy(@NotNull Iterable<E> sources, Supplier<T> targetConstructor) {
        ArrayList<T> targets = sources instanceof Collection ? new ArrayList<>(((Collection<E>) sources).size()) : new ArrayList<>();
        for (E source : sources) {
            targets.add(copy(source, targetConstructor));
        }
        return targets;
    }

    @SneakyThrows
    public List<T> copy(@NotNull Iterable<E> sources, Class<T> targetClass) {
        ArrayList<T> targets = sources instanceof Collection ? new ArrayList<>(((Collection<E>) sources).size()) : new ArrayList<T>();
        Constructor<T> constructor = targetClass.getConstructor();
        for (E source : sources) {
            T target = constructor.newInstance();
            copy(source, target);
            targets.add(target);
        }
        return targets;
    }

    public T copy(@NotNull E source, Supplier<T> targetConstructor) {
        T target = targetConstructor.get();
        copy(source, target);
        return target;
    }

    @SneakyThrows
    public T copy(@NotNull E source, Class<T> targetClass) {
        T target = targetClass.getConstructor().newInstance();
        copy(source, target);
        return target;
    }

    @SuppressWarnings("unchecked")
    public void copy(@NotNull E source, @NotNull T target) {
        Preconditions.checkNotNull(source, "Source must not be null");
        Preconditions.checkNotNull(target, "Target must not be null");

        switch (type) {
            case ALL_MAP: {
                ((Map<String, Object>)target).putAll((Map<String, Object>) source);
            } break;
            case ONLY_SOURCE_MAP: {
                mapToBean((Map<String, Object>) source, target);
            } break;
            case ONLY_TARGET_MAP: {
                beanToMap(source, (Map<String, Object>) target);
            } break;
            case ALL_OBJECT: {
                copyBean(source, target);
            } break;
            default: {
                throw new IllegalStateException();
            }
        }
    }

    abstract protected void copyBean(E source, T target);

    abstract protected void mapToBean(Map<String, Object> map, Object bean);

    abstract protected void beanToMap(Object bean, Map<String, Object> map);

    static LinkedHashSet<String> editableProperties(
        @Nullable Set<String> targetProperties, @Nullable Set<String> sourceProperties,
        @Nullable List<String> includes, @Nullable List<String> excludes,
        Predicate<String> isTypeMatched) {
        LinkedHashSet<String> commonSet;
        if (targetProperties != null && sourceProperties != null) {
            commonSet = new LinkedHashSet<>(sourceProperties);
            commonSet.retainAll(targetProperties);
            commonSet.removeIf(name -> !isTypeMatched.test(name));
        } else if (sourceProperties != null) {
            commonSet = new LinkedHashSet<>(sourceProperties);
        } else if (targetProperties != null) {
            commonSet = new LinkedHashSet<>(targetProperties);
        } else {
            commonSet = null;
        }


        if (targetProperties != null || sourceProperties != null) {
            if (includes != null) {
                commonSet.retainAll(includes);
            }
            if (excludes != null) {
                commonSet.retainAll(excludes);
            }
        }
        return commonSet;
    }
}
