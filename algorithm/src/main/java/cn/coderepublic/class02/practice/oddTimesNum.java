package cn.coderepublic.class02.practice;

import java.util.HashMap;

/**
 * @description: 一个数组中有一种数出现了奇数次，其他数都出现了偶数次，怎么找到并打印这种数
 * @author: shier
 * @date: 2023/2/1 07:58
 */
public class oddTimesNum {

    /*
    将数组中所有元素异或，最后的值就是奇数次的数
     */

    private static int oddTimes(int[] arr) {

        int result = 0;

        for (int i = 0; i < arr.length; i++) {
            result = result ^ arr[i];
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 2, 2, 3, 3, 4, 4};
        System.out.println(oddTimes(arr));
    }
}
