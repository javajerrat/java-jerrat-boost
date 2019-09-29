package io.github.javajerrat.boost.lang.datatype;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2018/12/3
 */

public class Enums {

    public Enums() {
        throw new UnsupportedOperationException();
    }
    /**
     * From the enumeration of the enumeration class enumClass, find an enumeration where one of the fields in the enumeration is equal to the value. Where the field is described by the getter parameter
     * @param enumClass enumeration class
     * @param getter field getter
     * @param value value
     * @return  found enumeration
     */
    public static <T extends Enum, R> Optional<T> enumOf(Class<T> enumClass, Function<T, R> getter, R value) {
        T[] enums = enumClass.getEnumConstants();
        for (T item: enums) {
            if (Objects.equals(getter.apply(item), value)) {
                return Optional.ofNullable(item);
            }
        }
        return Optional.empty();
    }
}
