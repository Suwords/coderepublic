package cn.coderepublic.class01.practice;

import java.util.Arrays;

/**
 * @description: 自己实现冒泡排序
 * @author: shier
 * @date: 2023/1/31 08:11
 */
public class BubbleSort {

    /*
    1. arr[0~N-1]上，比较arr[0] arr[1]，找出较大者，交换位置；比较arr[1] arr[2]，找出较大者，交换位置
    ...
    比较arr[N-2] arr[N-1]，找出较大者，交换位置

    2. arr[0~N-2]上 ...
    3. arr[0~N-3]上 ...
     */

    private static void bubbleSort(int[] arr) {

        if (arr == null || arr.length < 2) {
            return;
        }
        // 0 ~ N-1
        // 0 ~ N-2
        // 0 ~ N-3 ...
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 1; j <= i; j++) {
                if (arr[j-1] > arr[j]) {
                    swap(arr, j-1, j);
                }
            }
        }
    }

    /**
     * 功能描述：交换数组元素，此方法实现对于 i==j 时会报错
     * @param: [arr, i, j]
     * @return: void
     * @author: shier
     * @date: 2023/1/31
    */
    private static void swap(int[] arr, int i, int j){
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    /***
     * 功能描述：对数器
     * @param: [arr]
     * @return: int[]
     * @author: shier
     * @date: 2023/1/31
     */
    private static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    /***
     * 功能描述：生成随机测试用例
     * @param: [maxSize, maxValue]
     * @return: int[]
     * @author: shier
     * @date: 2023/1/31
     */
    private static int[] generateRandomArray(int maxSize, int maxValue){
        int[] arr = new int[maxSize];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int)(Math.random() + 1) * maxValue - (int)(Math.random() * maxValue);
        }

        return arr;
    }

    /***
     * 功能描述：比较两数组是否相同
     * @param: [arr1, arr2]
     * @return: boolean
     * @author: shier
     * @date: 2023/1/31
     */
    private static boolean isEquals(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /***
     * 功能描述：复制数组
     * @param: [arr]
     * @return: int[]
     * @author: shier
     * @date: 2023/1/31
     */
    private static int[] copyArray(int[] arr){
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    public static void main(String[] args) {
        int testTimes = 500000;
        int maxSize = 10;
        int maxValue = 10;

        while (testTimes > 0) {
            testTimes--;
            // 随机生成测试用例
            int[] arr = generateRandomArray(maxSize, maxValue);
            int[] copy = copyArray(arr);

//            for (int i = 0; i < arr.length; i++) {
//                System.out.print(arr[i] + " ");
//            }

            // 排序
            bubbleSort(arr);
            comparator(copy);

            // 比对
            if (!isEquals(arr, copy)) {
                System.out.println("测试排序错误！！！！");

                for (int i = 0; i < arr.length; i++) {
                    System.out.print(arr[i] + " ");
                }

                System.out.println();

                for (int i = 0; i < copy.length; i++) {
                    System.out.print(copy[i] + " ");
                }

                return;
            }
        }

        System.out.println("测试排序成功！！！");
    }
}
