package sample.datastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LinkListReverse {
    public static void main(String[] args) {
        Node first = new Node(0);
        Node current = first;
        for (int i = 1; i <= 10; i++) {
            Node next = new Node(i);
            current.next = next;
            current = next;
        }
        System.out.println();

        print(first);
        Node reversed = reverse(first);
        print(reversed);
    }

    private static void print(Node head) {
        Node cur = head;
        while (cur != null) {
            System.out.println(cur.value);
            cur = cur.next;
        }
    }

    public static Node reverse(Node head) {
        Node pre = head;
        Node cur = head.next;

        while (cur != null) {
            Node temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        head.next = null;

        return pre;
    }
}

class Node {
    public Object value;
    public Node next;

    public Node(Object value) {
        this.value = value;
    }
}
