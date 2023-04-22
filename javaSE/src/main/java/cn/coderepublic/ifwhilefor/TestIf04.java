package cn.coderepublic.ifwhilefor;

import java.util.Scanner;

public class TestIf04 {
    public static void main(String[] args) {
        //1.给出积分：
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入会员积分：");

        //先判断键盘录入的数据是不是int类型的
        if (sc.hasNextInt() == true) {//是int类型数据：
            //将这个int类型的数据接收：
            int score = sc.nextInt();
            //判断这个积分是否是正数：
            if (score >= 0) {
                String discount = "";
                //2.根据积分判断折扣：
                if (score >= 8000) {
                    discount = "0.6";
                } else if (score >= 4000) {
                    discount = "0.7";
                } else if (score >= 2000) {
                    discount = "0.8";
                } else {
                    discount = "0.9";
                }
                System.out.println("该会员享受的折扣为：" + discount);

            } else {//score<0
                System.out.println("对不起，你录入的积分是负数！不符合需求！");
            }
        } else {//不是int类型的数据
            System.out.println("你录入的积分不是整数！");
        }

    }
}
