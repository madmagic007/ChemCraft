package me.madmagic.chemcraft.util;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class GeneralUtil {

    public static boolean anyNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) return true;
        }
        return false;
    }

    public static boolean isAny(Object toCheck, Object... possibleValues) {
        for (Object possibleValue : possibleValues) {
            if (toCheck.equals(possibleValue)) return true;
        }
        return false;
    }

    public static <T> void forEachIndexed(List<T> list, BiConsumer<T, Integer> consumer) {
        for (int i = 0; i < list.size(); i++) {
            consumer.accept(list.get(i), i);
        }
    }

    public static <T> boolean notNullAnd(T toCheck, Predicate<T> predicate) {
        return toCheck != null && predicate.test(toCheck);
    }

    public static int clamp(int val, int max) {
        return Math.max(0, Math.min(max, val));
    }

    public static double mapValue(double factorValue, double factorMax, double theMax) {
       return mapValue(factorValue, factorMax, 0, theMax);
    }

    public static double mapValue(double factorValue, double factorMax, double theMin, double theMax) {
        double theFactor = factorValue / factorMax;
        double range = theMax - theMin;
        return theMin + range * theFactor;
    }
}
