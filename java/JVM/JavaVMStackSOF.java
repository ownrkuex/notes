class JavaVMStackSOF {
    private int stackLength = 1;

    public void stackLeak() {
        long a = 1;
        double b = 1.;
        long c = 2;
        int d = 1;
        int e = 3;
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF javaVMStackSOF = new JavaVMStackSOF();
        try {
            javaVMStackSOF.stackLeak();
        } catch (StackOverflowError e) {
            System.out.println("stack length: " + javaVMStackSOF.stackLength);
        }
    }
}