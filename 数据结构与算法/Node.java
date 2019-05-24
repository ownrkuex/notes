import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public final class Node {

    private final String id;

    private final Node left;

    private final Node right;

    public static final String NULL_ID = "#";

    public static final Node NULL = new Node(NULL_ID, null, null);

    public static boolean isNull(Node node) {
        return node == null || NULL_ID.equals(node.getId());
    }

    public static Node of(String id) {
        if (NULL_ID.equals(id)) {
            return NULL;
        }
        return new Node(id, null, null);
    }

    public static Node of(String id, Node left, Node right) {
        if (NULL_ID.equals(id) && isNull(left) && isNull(right)) {
            return NULL;
        }
        return new Node(id, left, right);
    }

    private Node(String id, Node left, Node right) {
        Objects.requireNonNull(id, "node id can not be null");
        if ("".equals(id.trim())) {
            throw new IllegalArgumentException("node id can not be blank");
        }
        if (id.equals(NULL_ID) && (!isNull(left) || !isNull(right))) {
            throw new IllegalStateException("NULL node has no children");
        }

        this.id = id;
        this.left = left;
        this.right = right;
    }

    public String getId() {
        return id;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String toString() {
        return getId() + "{ left: " + getLeft() + ", right: " + getRight() + " }";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Node)) {
            return false;
        }
        Node otherNode = (Node) other;
        return getId().equals(otherNode.getId()) && Objects.equals(getLeft(), otherNode.getLeft())
                && Objects.equals(getRight(), otherNode.getRight());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        int leftHashCode = getLeft() == null ? 0 : getLeft().hashCode();
        int rightHashCode = getRight() == null ? 0 : getRight().hashCode();
        result = result * 31 + leftHashCode;
        result = result * 31 + rightHashCode;

        return result;
    }

    public String serializeLevel() {
        Queue<Node> queue = new LinkedBlockingQueue<>();
        String str = "";
        queue.add(this);
        while (queue.size() != 0) {
            Node currentNode = queue.poll();
            str += currentNode.getId() + "!";
            if (!isNull(currentNode)) {
                if (!isNull(currentNode.getLeft())) {
                    queue.add(currentNode.getLeft());
                } else {
                    queue.add(NULL);
                }
                if (!isNull(currentNode.getRight())) {
                    queue.add(currentNode.getRight());
                } else {
                    queue.add(NULL);
                }
            }
        }

        return str;
    }

    private static Node deserializeLevel(List<String> ids) {
        if ((ids.size() & 1) == 0) {
            ids.add(NULL_ID);
        }
        Node[] nodes = new Node[ids.size()];
        int lastIndexOfNonNull = nodes.length - 2;
        for (int i = nodes.length - 1; i > 0; i--) {
            Node right = nodes[i];
            if (!NULL_ID.equals(ids.get(i)) && isNull(right)) {
                right = Node.of(ids.get(i));
            }

            Node left = nodes[--i];
            if (!NULL_ID.equals(ids.get(i)) && isNull(left)) {
                left = Node.of(ids.get(i));
            }

            while (--lastIndexOfNonNull > -1) {
                if (!NULL_ID.equals(ids.get(lastIndexOfNonNull))) {
                    break;
                }
            }
            if (lastIndexOfNonNull == -1) {
                throw new IllegalArgumentException("missing root");
            }
            nodes[lastIndexOfNonNull] = Node.of(ids.get(lastIndexOfNonNull), left, right);
        }

        return nodes[0];
    }

    public static Node deserializeLevel(String str) {
        return deserializeLevel(getIds(str));
    }

    public String serializeDLR() {
        String str = getId() + "!";
        if (isNull(this)) {
            return str;
        }

        if (isNull(getLeft())) {
            str += NULL_ID + "!";
        } else {
            str += getLeft().serializeDLR();
        }
        if (isNull(getRight())) {
            str += NULL_ID + "!";
        } else {
            str += getRight().serializeDLR();
        }

        return str;
    }

    private static Node deserializeDLR(List<String> ids) {
        if (NULL_ID.equals(ids.get(0))) {
            ids.remove(0);
            return null;
        }

        return new Node(ids.remove(0), deserializeDLR(ids), deserializeDLR(ids));
    }

    private static List<String> getIds(String str) {
        List<String> ids = new ArrayList<>(Arrays.asList(str.split("!")));
        Iterator<String> iter = ids.iterator();
        while (iter.hasNext()) {
            String id = iter.next();
            if (id == null || id.equals("")) {
                iter.remove();
            }
        }

        return ids;
    }

    public static Node deserializeDLR(String str) {
        return Node.deserializeDLR(getIds(str));
    }

    public static void printBinaryTreeInLine(Node root) {
        Node currentLineEnd = root;
        Node nextLineEnd = root;
        Queue<Node> queue = new LinkedBlockingQueue<>();
        queue.add(root);
        while (queue.size() != 0) {
            Node currentNode = queue.poll();
            System.out.print(currentNode.getId() + " ");
            if (!isNull(currentNode.getLeft())) {
                queue.add(currentNode.getLeft());
                nextLineEnd = currentNode.getLeft();
            }
            if (!isNull(currentNode.getRight())) {
                queue.add(currentNode.getRight());
                nextLineEnd = currentNode.getRight();
            }

            if (currentNode == currentLineEnd) {
                System.out.println();
                currentLineEnd = nextLineEnd;
            }
        }
        System.out.println();
    }
}