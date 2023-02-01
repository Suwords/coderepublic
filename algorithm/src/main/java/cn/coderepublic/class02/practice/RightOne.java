package cn.coderepublic.class02.practice;

/**
 * @description: 给定一个int类型的数，提取出最右侧的1，统计数N的二进制中有几个1
 * @author: shier
 * @date: 2023/2/1 08:13
 */
public class RightOne {

    /*
    0110 1110
    1001 0010
    0000 0010
     */

    private static int rightOne(int num) {
//        num & (~num + 1)
        return num & (-num);
    }

    /*
    先提取rightone
    num & rightone
    count++
     */
}
