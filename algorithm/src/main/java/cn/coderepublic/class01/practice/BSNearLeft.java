package cn.coderepublic.class01.practice;

import java.util.Arrays;

/**
 * @description: 在有序数组中找到>=某个数最左侧的位置
 * @author: shier
 * @date: 2023/1/31 20:55
 */
public class BSNearLeft {

    /*
    1 2 3 3 3 4 5     3
    获取数组中间的值，比较中间值与查找值，大于改变上限，小于改变下限
     */

    private static int bsNearLeft(int[] arr, int a){

        if (arr == null || arr.length < 1) {
            return -1;
        }

        int l = 0;
        int r = arr.length - 1;
        int index = 0;
        int mid = 0;
        while (l <= r) {
            mid = l + ((r - l) >> 1);
            if (arr[mid] >= a) {
                index = mid;
                r = mid - 1;
            } else if (arr[mid] < a) {
                l = mid + 1;
            }
        }

        return index;
    }

    /**
     * 功能描述：对数器
     * @param: [arr, a]
     * @return: int
     * @author: shier
     * @date: 2023/1/31
     */
    private static int comparator(int[] arr, int a) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= a) {
                return i;
            }
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

        Arrays.sort(arr);

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

    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 100;
        int maxValue = 100;

        while (testTime > 0) {
            testTime--;

            int[] arr = generateRandomArray(maxSize, maxValue);
            int value = (int) ((Math.random() + 1) * maxValue) - (int) (Math.random() * maxValue);
            int bs = bsNearLeft(arr, value);
            int comp = comparator(arr, value);

            if (bs != comp) {
                System.out.println("程序测试失败！！！");
                return;
            }
        }

        System.out.println("程序测试成功！！！");
    }

}
