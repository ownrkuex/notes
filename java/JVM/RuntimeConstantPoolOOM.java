import java.util.*;

class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        List<String> strs = new ArrayList<>();
        int i = 0;
        while (true) {
            // 将大量字符串加到运行时常量池中，同时将这些字符串的引用保持住，避免被GC
            strs.add(String.valueOf(i++).intern());
        }
    }
}