import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Demo {
    public static void main(String[] args) throws Exception {
        Class<?> proxyClass = Proxy.getProxyClass(SubjectImpl.class.getClassLoader(),
                SubjectImpl.class.getInterfaces());
        Constructor constructor = proxyClass.getConstructor(InvocationHandler.class);
        Subject subjectProxy = (Subject) constructor.newInstance(new SubjectInvocationHandler(new SubjectImpl()));

        System.out.println(subjectProxy.getClass());
        System.out.println(subjectProxy.toString());
        subjectProxy.sayHello();

        Util.generateProxyClassFile(proxyClass, "./ProxyClass.class");
    }
}