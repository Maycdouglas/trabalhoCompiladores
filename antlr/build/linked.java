import java.util.Scanner;

public class linked {
    public static class Node {
        public int val;
        public Node next;
    }

    public static class LList {
        public Node head;
        public int size;
    }

    public static LList createList() {
        LList n = new LList();
        n.head = null;
        n.size = 0;
        return n;
    }

    public static Node lastNode(Node h) {
        if ((h.next == null)) {
            return h;
        }
        return lastNode(h.next);
    }

    public static void addNode(LList l, int x) {
        Node no = new Node();
        no.val = x;
        no.next = null;
        if ((l.head == null)) {
            l.head = no;
        } else {
            Node last = lastNode(l.head);
            last.next = no;
        }
        l.size = (l.size + 1);
    }

    public static Node removeHead(LList l) {
        if ((l.head != null)) {
            Node node = l.head;
            l.head = l.head.next;
            l.size = (l.size - 1);
            return node;
        }
        return null;
    }

    public static void printList(Node h) {
        if ((h != null)) {
            System.out.print(h.val);
            System.out.print("-");
            System.out.print(">");
            printList(h.next);
        }
        if ((h == null)) {
            System.out.print("N");
            System.out.print("U");
            System.out.print("L");
            System.out.print("L");
            System.out.println();
        }
    }

    public static void printLList(LList l) {
        System.out.print((l.size));
        System.out.print(":");
        printList(l.head);
    }

    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        int k = 65;
        LList l = createList();
        addNode(l, k);
        for (int _i0 = 0; _i0 < 5; _i0++) {
            k = (k + 1);
            addNode(l, k);
        }
        printLList(l);
        Node head = removeHead(l);
        printLList(l);
    }

}
