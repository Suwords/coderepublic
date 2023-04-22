package cn.coderepublic.ifwhilefor;

import java.util.Scanner;

public class TestFor10 {
    public static void main(String[] args) {
                /*
                        实现一个功能：
                   【1】请录入10个整数，当输入的数是666的时候，退出程序。
                   【2】判断其中录入正数的个数并输出。
                   【3】判断系统的退出状态：是正常退出还是被迫退出。
                */
        //引入一个计数器：
        int count = 0;
        //引入一个布尔类型的变量：
        boolean flag = true; //---》理解为一个“开关”，默认情况下开关是开着的
        Scanner sc = new Scanner(System.in);
        for (int i = 1; i <= 10; i++) {//i:循环次数
            System.out.println("请录入第" + i + "个数：");
            int num = sc.nextInt();
            if (num > 0) {//录入的正数
                count++;
            }
            if (num == 666) {
                flag = false;//当遇到666的时候，“开关”被关上了
                //退出循环：
                break;
            }

        }

        System.out.println("你录入的正数的个数为：" + count);


        if (flag) {//flag==true
            System.out.println("正常退出！");
        } else {//flag==false
            System.out.println("被迫退出！");
        }


    }
}
