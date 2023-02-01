package cn.coderepublic.class02.practice;

/**
 * @description: 如何不使用额外变量交换两个数
 * @author: shier
 * @date: 2023/2/1 07:44
 */
public class Swap {

    public static void main(String[] args) {

        int i = 10;
        int j = 20;

        i = i ^ j;
        j = i ^ j;
        i = i ^ j;

        System.out.println(i + " , " + j);

    }
}
