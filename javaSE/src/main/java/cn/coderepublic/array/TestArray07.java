package cn.coderepublic.array;

import java.util.Scanner;

public class TestArray07 {
    public static void main(String[] args) {
        //功能：给定一个数组,在数组下标为2的位置上添加一个元素91

        //1.给定一个数组：
        int[] arr = {12, 34, 56, 7, 3, 10, 55, 66, 77, 88, 999, 89};
        //           0  1   2 3 4 5
        //2.输出增加元素前的数组：
                /*
                System.out.print("增加元素前的数组：");
                for(int i=0;i<arr.length;i++){
                        if(i!=arr.length-1){
                                System.out.print(arr[i]+",");
                        }else{//i==arr.length-1 最后一个元素不用加,
                                System.out.print(arr[i]);
                        }
                }
                */

        //从键盘接收数据：
        Scanner sc = new Scanner(System.in);
        System.out.println("请录入你要添加元素的指定下标：");
        int index = sc.nextInt();
        System.out.println("请录入你要添加的元素：");
        int ele = sc.nextInt();

        //3.增加元素
        //调用方法：
        insertEle(arr, index, ele);


        //4.输出增加元素后的数组：
        System.out.print("\n增加元素后的数组：");
        for (int i = 0; i < arr.length; i++) {
            if (i != arr.length - 1) {
                System.out.print(arr[i] + ",");
            } else {//i==arr.length-1 最后一个元素不用加,
                System.out.print(arr[i]);
            }
        }

    }


    /*
    提取一个添加元素的方法：
    在数组的指定位置上添加一个指定的元素。
    在哪个数组的哪个位置添加哪个元素！
    不确定因素：形参：哪个数组，哪个位置，哪个元素
    返回值：无

    */
    public static void insertEle(int[] arr, int index, int ele) {
        for (int i = arr.length - 1; i >= (index + 1); i--) {
            arr[i] = arr[i - 1];
        }
        arr[index] = ele;
    }
}
