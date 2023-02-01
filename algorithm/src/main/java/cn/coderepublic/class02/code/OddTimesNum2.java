package cn.coderepublic.class02.code;

/**
 * @description: arr中，有两种数，出现奇数次
 * @author: shier
 * @date: 2023/1/5 07:21
 */
public class OddTimesNum2 {
    public static void printOddTimesNum2(int[] arr){
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }

        // a 和 b是两种数
        // eor != 0
        // eor 最右侧的1提取出来
        // eor:  00110010110111000
        // right:00000000000001000
        int rightOne = eor & (-eor); // 提取出最右的1

        int onlyOne = 0; // eor`
        for (int i = 0; i < arr.length; i++) {
            // arr[1] = 111100011110000
            // rightOne=000000000010000
            if ((arr[i] & rightOne) != 0) {
                onlyOne ^= arr[i];
            }
        }
        System.out.println(onlyOne + " " + (eor ^ onlyOne));
    }
}
