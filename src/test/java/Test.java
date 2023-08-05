public class Test {

    public static void main(String[] args) throws InterruptedException {
        new TestedClass(1, 2);
    }

    private static class TestedClass extends TestClass {

        public TestedClass(int i, int j) {
            super(i, j);
        }
    }

    private static abstract class TestClass {

        public TestClass(int i) {

        }

        public TestClass(int i, int j) {

        }
    }
}
