package cn.coderepublic.ifwhilefor;

public class TestFor04 {
    public static void main(String[] args) {
        outer: //----》定义标签结束的位置
        for (int i = 1; i <= 100; i++) {
            System.out.println(i);
            while (i == 36) {
                break outer; //----》根据标签来结束循环
            }
        }
    }
}
