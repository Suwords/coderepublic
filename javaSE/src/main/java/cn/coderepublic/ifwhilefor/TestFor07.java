package cn.coderepublic.ifwhilefor;

public class TestFor07 {
    public static void main(String[] args) {

        outer:
        for (int i = 1; i <= 100; i++) {
            while (i == 36) {
                continue outer;  //1-100没有36
            }
            System.out.println(i);
        }
    }
}
