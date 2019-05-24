class FinalizeEscapeGC {

    private static FinalizeEscapeGC SAVE_HOOK = null;

    @Override
    public void finalize() {
        SAVE_HOOK = this;
        System.out.println("finalize method invoke");
    }

    public static void main(String[] args) throws Throwable {
        // 第一次自救成功
        SAVE_HOOK = new FinalizeEscapeGC();
        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            System.out.println("alive");
        } else {
            System.out.println("cleaned");
        }
        // 第二次自救失败
        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            System.out.println("alive");
        } else {
            System.out.println("cleaned");
        }
    }
}