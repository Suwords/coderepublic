package cn.coderepublic.ifwhilefor;

public class TestFor15 {
    public static void main(String[] args) {
        for (int a = 1; a <= 5; a++) {
            for (int b = 3; b <= 6; b++) {
                if (a + b == 7) {
                    System.out.println(a + "----" + b);
                }
            }
        }
    }
}
