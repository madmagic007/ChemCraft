public class MainTest {

    static class Block {

        public Block() {
            someMethod();
        }

        protected void someMethod() {}
    }

    static class BaseBlock extends Block {

        protected boolean val = false;

        public BaseBlock(boolean val) {
            super();
            this.val = val;
        }

        @Override
        protected void someMethod() {
            System.out.println(val);
        }

        public void someOtherMethod() {
            System.out.println("other: " + val);
        }
    }

    static class MyBlock extends BaseBlock {

        public MyBlock() {
            super(true);
        }

        @Override
        protected void someMethod() {
            val = true;
            super.someMethod();
        }
    }

    static class MyOtherBlock extends BaseBlock {

        public MyOtherBlock() {
            super(false);
        }
    }

    public static void main(String[] args) {
        MyBlock block = new MyBlock();
        new MyOtherBlock();
        block.someOtherMethod();
    }
}
