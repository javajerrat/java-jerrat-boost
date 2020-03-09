package io.github.javajerrat.boost.lang.reflect;

import static org.junit.jupiter.api.Assertions.*;

import io.github.javajerrat.boost.lang.reflect.beanaccessor.BeanAccessor;
import io.github.javajerrat.boost.lang.reflect.beanaccessor.JavaReflectBeanAccessor;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/21
 */
class BeanAccessorTest {

    @Data
    public static class Point {
        private int x;

        private int y;


    }

    @Data
    public static class PointEntity extends Point {
        private String name;
    }

    @Test
    @SneakyThrows
    void test() {
        BeanAccessor<PointEntity> pr = new JavaReflectBeanAccessor<>(PointEntity.class);
        PointEntity p = new PointEntity();
        assertEquals(0, (Integer)pr.get(p, "x"));
        pr.set(p, "x", 10);
        assertEquals(10, (Integer)pr.get(p, "x"));

    }

}