import java.util.*;

class HeapOOM {
    private static class OOMObject {
    }

    public static void main(String[] args) {
        System.out.println("堆OutOfMemoryError测试，谨慎执行！");
        List<OOMObject> objs = new ArrayList<>();
        while (true) {
            objs.add(new OOMObject());
        }
    }
}