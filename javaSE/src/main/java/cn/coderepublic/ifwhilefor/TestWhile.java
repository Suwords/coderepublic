package cn.coderepublic.ifwhilefor;

public class TestWhile {
    public static void main(String[] args) {
        //功能：1+2+3+4+5
        //1.定义变量：
        int num1 = 1;
        int num2 = 2;
        int num3 = 3;
        int num4 = 4;
        int num5 = 5;
        //2.定义一个求和变量，用来接收和：
        int sum = 0;
        sum += num1;
        sum += num2;
        sum += num3;
        sum += num4;
        sum += num5;

        //3.输出和
        System.out.println(sum);
    }
}
