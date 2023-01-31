package cn.coderepublic.class01.practice;

import java.util.Arrays;

/**
 * @description: 自己实现插入排序
 * @author: shier
 * @date: 2023/1/31 20:02
 */
public class InsertionSort {

    /*
    arr[0-0]有序，只有1个元素，必定有序
    arr[0-1]有序，从arr[1]往前看，arr[1]小于arr[0]则交换，否则不变
    arr[0-2]有序，从arr[2]往前看，arr[2]小于arr[1]则交换，然后看arr[1]小于arr[0]则交换
    ...
    arr[0-N-1]有序，从arr[N-1]往前看，arr[N-1]小于arr[N-2]则交换，然后比较arr[N-2]和arr[N-3]...
     */

    private static void insertSort(int[] arr) {

        if (arr == null || arr.length < 2) {
            return;
        }

        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j - 1, j);
                }
                break;
            }
        }
    }

    /***
     * 功能描述：交换数组中的元素
     * @param: [arr, i, j]
     * @return: void
     * @author: shier
     * @date: 2023/1/31
    */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 功能描述：对数器
     * @param: [arr]
     * @return: void
     * @author: shier
     * @date: 2023/1/31
    */
    private static void comparator(int[] arr) {
        Arrays.sort(arr);
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

    /**
     * 功能描述：拷贝数组
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

    /**
     * 功能描述：比较两数组是否相等
     * @param: [arr1, arr2]
     * @return: boolean
     * @author: shier
     * @date: 2023/1/31
    */
    private static boolean isEquals(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
                if (arr1[i] != arr2[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 100;
        int maxValue = 100;

        while (testTimes > 0) {
            testTimes--;

            int[] arr = generateRandomArray(maxSize, maxValue);
            int[] copy = copyArray(arr);

            insertSort(arr);
            comparator(copy);

            if (!isEquals(arr, copy)) {
                System.out.println("程序测试错误！！！");
                return;
            }
        }

        System.out.println("程序测试成功！！！");
    }
}
