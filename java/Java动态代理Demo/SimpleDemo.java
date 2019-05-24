import java.lang.reflect.Proxy;

public class SimpleDemo {
    public static void main(String[] args) {
        Subject subjectProxy = (Subject) Proxy.newProxyInstance(SubjectImpl.class.getClassLoader(),
                SubjectImpl.class.getInterfaces(), new SubjectInvocationHandler(new SubjectImpl()));
        System.out.println(subjectProxy.getClass());
        subjectProxy.sayHello();
    }
}