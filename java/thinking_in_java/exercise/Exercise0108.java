public class Exercise0108 {

    private static class Subject {
        private static Object object = new Object();

        public Object getObject() {
            return object;
        }
    }

    public static void main(String[] args) {
        Subject subject = new Subject();
        System.out.println(subject.getObject());
        double count = Math.random() * 10 + 1;
        for (int i = 0; i < count; i++) {
            System.out.println(new Subject().getObject() == subject.getObject());
        }
    }
}