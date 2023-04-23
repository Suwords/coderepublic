package cn.coderepublic.array;

public class TestArray04 {
    public static void main(String[] args) {
        //实现一个功能：给定一个数组int[] arr = {12,3,7,4,8,125,9,45}; ，求出数组中最大的数。
        //1.给定一个数组
        int[] arr = {12, 3, 7, 4, 8, 725, 9, 45, 666, 36};

        //2.求出数组中的最大值：
        //调用方法：
        int num = getMaxNum(arr);
        System.out.println("当前数组中最大的数为：" + num);
    }

    /*
    想提取一个方法：求数组中的最大值
    求哪个数组中的最大值 ---》不确定因素：哪个数组 (形参)---》返回值：最大值
    */
    public static int getMaxNum(int[] arr) {
        //先找一个数上擂台，假定认为这个数最大：
        int maxNum = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > maxNum) {
                maxNum = arr[i];
            }
        }
        return maxNum;

    }
}
