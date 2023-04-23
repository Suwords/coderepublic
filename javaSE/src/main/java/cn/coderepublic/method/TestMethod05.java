package cn.coderepublic.method;

public class TestMethod05 {
    public static void main(String[] args) {
        //10+20:
        int sum = add(10, 20);
        System.out.println(sum);

        //20+40+80:
        //System.out.println(add(add(20,40),80));
        System.out.println(add(20, 40, 80));
        //30+60+90+120:
        //System.out.println(add(add(30,60),add(90,120)));
        System.out.println(add(30, 60, 90, 120));
        //9.8+4.7:
        //System.out.println(add(9.8,4.7));
        System.out.println(add(9.8, 4.7));
    }

    //定义一个方法：两个数相加：两个int类型数据相加
    public static int add(int num1, int num2) {
        return num1 + num2;
    }

    //定义一个方法：三个数相加：
    public static int add(int num1, int num2, int num3) {
        return num1 + num2 + num3;
    }

    //定义一个方法：四个数相加：
    public static int add(int num1, int num2, int num3, int num4) {
        return num1 + num2 + num3 + num4;
    }

    //定义一个方法：两个数相加：两个double类型的数据相加
    public static double add(double num1, double num2) {
        return num1 + num2;
    }


}
