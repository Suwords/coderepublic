package cn.coderepublic.method;

public class TestMethod01 {

    //方法的定义：（写方法）
    public static int add(int num1, int num2) {
        int sum = 0;
        sum += num1;
        sum += num2;
        return sum;//将返回值返回到方法的调用处
    }

    public static void main(String[] args) {
        //10+20:
        //方法的调用：（用方法）
        int num = add(10, 20);
        System.out.println(num);
                /*
                int num1 = 10;
                int num2 = 20;
                int sum = 0;
                sum += num1;
                sum += num2;
                System.out.println(sum);
                */
        //30+90:
        int sum = add(30, 90);
        System.out.println(sum);
                /*
                int num3 = 30;
                int num4 = 90;
                int sum1 = 0 ;
                sum1 += num3;
                sum1 += num4;
                System.out.println(sum1);
                */
        //50+48:
        System.out.println(add(50, 48));

    }


}
