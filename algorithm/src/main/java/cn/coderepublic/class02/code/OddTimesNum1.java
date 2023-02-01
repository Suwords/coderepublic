package cn.coderepublic.class02.code;

/**
 * @description: arr中，只有一种数，出现奇数次
 * @author: shier
 * @date: 2023/1/5 07:19
 */
public class OddTimesNum1 {
    public static void printOddTimesNum1(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        System.out.println(eor);
    }
}
