package cn.coderepublic.operator;

public class TestOpe05 {
    public static void main(String[] args) {
        int a = 5;
        System.out.println(a++ + a++);
        System.out.println(a++ + ++a);
        System.out.println(++a + a++);
        System.out.println(++a + ++a);
    }
}
