import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sun.misc.ProxyGenerator;

public class Util {

    public static void generateProxyClassFile(Class proxyClass, String path) {
        byte[] classCode = ProxyGenerator.generateProxyClass(proxyClass.getSimpleName(), proxyClass.getInterfaces());
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            outputStream.write(classCode);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }  
            }  
        }  
    }
}