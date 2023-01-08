package cn.coderepublic.datastruct;

/**
 * @description: 删除给定值
 * @author: shier
 * @date: 2023/1/8 07:51
 */
public class DeleteGivenValue {
    public static class Node {
        public int value;
        public Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    // head = removeValue(head, 2)
    public static Node removeValue (Node head, int num) {
        // head 来到第一个不需要删除的位置
        while (head != null) {
            if (head.value != num) {
                break;
            }
            head = head.next;
        }
        // 1) head == null
        // 2) head != null
        Node pre = head;
        Node cur = head;
        while (cur != null) {
            if (cur.value == num) {
                pre.next = cur.next;
            } else {
                pre = cur;
            }
            cur = cur.next;
        }

        return head;
    }
}
