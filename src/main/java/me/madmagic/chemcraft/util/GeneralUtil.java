package me.madmagic.chemcraft.util;

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
}
