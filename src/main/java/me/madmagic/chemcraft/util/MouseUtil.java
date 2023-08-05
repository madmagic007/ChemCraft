package me.madmagic.chemcraft.util;

public class MouseUtil {

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y) {
        return isMouseOver(mouseX, mouseY, x, y, 16);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int size) {
        return isMouseOver(mouseX, mouseY, x, y, size, size);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height);
    }
}
