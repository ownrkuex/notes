public class Demo3_1 {

    private static boolean ready;
    private static int number;

    private static class ReadThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) throws Exception {
        new ReadThread().start();
        Thread.sleep(50);
        number = 42;
        ready = true;
    }
}