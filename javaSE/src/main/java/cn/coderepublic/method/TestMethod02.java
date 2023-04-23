package cn.coderepublic.method;

public class TestMethod02 {

    public static void add(int num1, int num2) {
        int sum = 0;
        sum += num1;
        sum += num2;
        System.out.println(sum);
        //return;
    }

    public static void main(String[] args) {
        //10+20:
        //方法的调用：（用方法）
        add(10, 20);
        //30+90:
        add(30, 90);
        //50+48:
        //System.out.println(add(50,48));//报错：TestMethod02.java:22: 错误: 此处不允许使用 '空' 类型

    }
}
