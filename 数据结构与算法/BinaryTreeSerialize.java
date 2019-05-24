class Demo {
    public static void main(String[] args) {
        Node binaryTreeRoot = Node.of("1",
            Node.of("2", 
                Node.of("4"), 
                null
            ),
            Node.of("3", 
                Node.of("5",
                    Node.of("7"),
                    Node.of("8")
                ),
                Node.of("6")
            )
        );
        String valueDLR = binaryTreeRoot.serializeDLR();
        System.out.println("DLR: " + valueDLR);
        System.out.println(binaryTreeRoot.equals(Node.deserializeDLR(valueDLR)));

        String valueLevel = binaryTreeRoot.serializeLevel();
        System.out.println("level: " + valueLevel);
        System.out.println(binaryTreeRoot.equals(Node.deserializeLevel(valueLevel)));
        Node.deserializeLevel("#!1!#!");
    }
}