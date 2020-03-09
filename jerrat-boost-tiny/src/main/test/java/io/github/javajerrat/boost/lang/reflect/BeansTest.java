package io.github.javajerrat.boost.lang.reflect;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.github.javajerrat.boost.basetools.rand.RandomHelper;
import io.github.javajerrat.boost.lang.collection.Colls;
import io.github.javajerrat.boost.lang.io.IOs;
import io.github.javajerrat.boost.lang.reflect.beancopier.BeanCopier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/22
 */
class BeansTest {

    @Data
    public static class Point {
        private int x;
        private int y;
        private int z;
    }

    @Data
    public static class Point2 {
        private Integer x;
        private Integer y;
        private Integer z;
    }

    @Data
    public static class Point3 {
        private int x;
        private int y;
    }

    @Test
    void copy() {

        {
            Point p1 = new Point();
            Point2 p2 = Beans.copy(p1, Point2::new);
            System.out.println(p2);
        }

        {
            Point p1 = new Point();
            p1.setX(1);
            Point3 p2 = Beans.copy(p1, Point3::new);
            System.out.println(p2);
        }

        {
            Point p1 = new Point();
            p1.setX(1);
            HashMap<Object, Object> map = new HashMap<>();
            Beans.copy(p1, map);
            System.out.println(map);
        }

        @Data
        class Editable {
            int x;
        }

        {
            Point p1 = new Point();
            p1.setX(1);
            HashMap<Object, Object> map = new HashMap<>();
            Beans.copy(p1, map, Editable.class);
            System.out.println(map);
        }

        {
            ImmutableMap<Object, Object> map = ImmutableMap.builder()
                .put("x", 1)
                .put("y", 2)
                .build();
            Point p1 = Beans.copy(map, Point::new);
            System.out.println(p1);
        }

        {
            ImmutableMap<String, Object> map = ImmutableMap.<String, Object>builder()
                .put("x", 1)
                .put("y", 2)
                .build();
            Map<String, Object> map2 = Beans.copy(map, LinkedHashMap::new);
            System.out.println(map2);
        }

    }

    @Test
    @SneakyThrows
    void cglibCopy() {
        BeanCopier<Point, Point3> copier = Beans.copier(Beans.ReflectMode.CGLIB, Point.class, Point3.class, null, null);
        BeanCopier<Point, Point3> javaCopier = Beans.copier(Beans.ReflectMode.JAVA, Point.class, Point3.class, null, null);

        net.sf.cglib.beans.BeanCopier beanCopier = net.sf.cglib.beans.BeanCopier.create(Point.class, Point3.class, false);
        net.sf.cglib.beans.BeanCopier beanCopier2 = net.sf.cglib.beans.BeanCopier.create(Point.class, Point2.class, false);
        net.sf.cglib.beans.BeanCopier beanCopier3 = net.sf.cglib.beans.BeanCopier.create(Point.class, Pair.class, false);
        List<Point> pointList = new ArrayList<>();
        int count = 1000000;
        for (int i = 0; i < count; i++) {
            Point p1 = new Point();
            p1.setX(RandomHelper.of().nextInt());
            pointList.add(p1);
        }

        Iterable<Point> points = Iterables.concat(Colls.repeatListOf(pointList, 100));

        {
            StopWatch sw = StopWatch.createStarted();
            for (Point p : points) {
                Point3 p2 = new Point3();
                copier.copy(p, p2);
                consumer(p2);
            }
            System.out.println(sw.toString());

        }

        {
            StopWatch sw = StopWatch.createStarted();
            for (Point p : points) {
                Point3 p2 = new Point3();
                javaCopier.copy(p, p2);
                consumer(p2);
            }
            System.out.println(sw.toString());

        }

        {
            StopWatch sw = StopWatch.createStarted();
            for (Point p : points) {
                Point3 p2 = new Point3();
                beanCopier.copy(p, p2, null);
                consumer(p2);
            }
            System.out.println(sw.toString());

        }
    }

    @SneakyThrows
    private void consumer(Point3 p3) {
        IOs.nullWriter().write(p3.toString());
    }
}