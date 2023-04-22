package cn.coderepublic.ifwhilefor;

import java.util.Scanner;

public class TestIf05 {
    public static void main(String[] args) {
        //1.录入小朋友的年龄：
        Scanner sc = new Scanner(System.in);
        System.out.println("请录入小朋友的年龄：");
        int age = sc.nextInt();

        //2.根据年龄判断：
        if (age >= 7) {
            System.out.println("yes");
        } else if (age >= 5) {
            //录入小朋友的性别；
            System.out.println("请录入小朋友的性别：男：1  女 ：0");
            int sex = sc.nextInt();
            if (sex == 1) {//男生
                System.out.println("yes");
            } else {//女孩
                System.out.println("no");
            }
        } else {//age<5
            System.out.println("no");
        }
    }
}