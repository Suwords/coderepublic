package cn.coderepublic.class01.practice;

import java.util.Arrays;

/**
 * @description: 如果一个数比其左边的数小，比其右边的数大，那么称之为局部最小值
 * 有一个无序数组，任意相邻位置的数不相等，求其任一局部最小值
 * 0位置比1位置小，返回0
 * N-1 位置上比 N-2 位置小，返回 N-1
 * @author: shier
 * @date: 2023/1/31 21:17
 */
public class BSAwesome {

    /*
    3 2 1 2 3
    获取数组中间位置，中间值比相邻左边值大则改变下限，比相邻右边值大则改变上限，比两边都小则返回
     */
    private static int bsAwesome(int[] arr) {
        if (arr == null || arr.length < 1) {
            return -1;
        }

        if (arr.length == 1 || arr[0] < arr[1]) {
            return 0;
        }

        if (arr[arr.length - 1] < arr[arr.length - 2]) {
            return arr.length - 1;
        }

        int l = 0;
        int r = arr.length - 1;
        int mid = 0;

        while(l < r) {
            mid = l + (r - l) >> 1;
            if (arr[mid] > arr[mid - 1]){
                r = mid - 1;
            } else if (arr[mid] > arr[mid + 1]){
                l = mid + 1;
            } else {
                return mid;
            }
        }
        return l;
    }

    /**
     * 功能描述：对数器
     * @param: [arr, a]
     * @return: int
     * @author: shier
     * @date: 2023/1/31
     */
    private static int comparator(int[] arr, int index) {
        if (index < 1) {
            return -1;
        }
        if(arr[index] < arr[index - 1] && arr[index] < arr[index + 1]) {
            return index;
        }
        return -1;
    }

    /***
     * 功能描述：生成随机数组测试用例
     * @param: [maxSize, maxValue]
     * @return: int[]
     * @author: shier
     * @date: 2023/1/31
     */
    private static int[] generateRandomArray(int maxSize, int maxValue) {

        int size = (int) Math.random() * maxSize;

        int[] arr = new int[size];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((Math.random() + 1) * maxValue) - (int) (Math.random() * maxValue);
        }

        return arr;
    }

    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 100;
        int maxValue = 100;

        while (testTime > 0) {
            testTime--;

            int[] arr = generateRandomArray(maxSize, maxValue);
            int bs = bsAwesome(arr);
            int comp = comparator(arr, bs);
            System.out.println(bs + " " + comp);

            if (bs != comp) {
                System.out.println("程序测试失败！！！");
                return;
            }
        }

        System.out.println("程序测试成功！！！");
    }
}
