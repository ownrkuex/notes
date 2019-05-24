class JavaVMStackOOM {

    private int threadCount = 0;

    private void dontStop() {
        while (true) {
        }
    }

    public void stackLeakByThread () {
        while (true) {
            new Thread(() -> dontStop()).start();
            threadCount++;
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM javaVMStackOOM = new JavaVMStackOOM();
        try {
            javaVMStackOOM.stackLeakByThread();
        } catch (OutOfMemoryError e) {
            System.out.println("thread count: " + javaVMStackOOM.threadCount);
            throw e;
        }
    }
}