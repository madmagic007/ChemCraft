public class MainTest {

    public static void main(String[] args) {
        System.out.println(mapValue(15 - 15, 15, 25));
    }

    public static double mapValue(double factorValue, double factorMax, double theMax) {
        double theFactor = factorValue / factorMax;
        return theMax * theFactor;
    }
}
