import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

class Demo {
    public static void main(String[] args) {
        Node binaryTreeRoot1 = Node.of("1",
            Node.of("2", Node.of("4"), null),
            Node.of("3", 
                Node.of("5", Node.of("7"),Node.of("8")),
                Node.of("6")
            )
        );

        Node.printBinaryTreeInLine(binaryTreeRoot1);

        System.out.println("-------------------");

        Node binaryTreeRoot2 = Node.of("1",
            Node.of("2",
                Node.of("4"),
                Node.of("5")
            ),
            Node.of("3")
        );

        Node.printBinaryTreeInLine(binaryTreeRoot2);
    }
}