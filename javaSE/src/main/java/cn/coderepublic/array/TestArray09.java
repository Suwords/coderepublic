package cn.coderepublic.array;

import java.util.Arrays;

public class TestArray09 {
    public static void main(String[] args) {
        //功能：给定一个数组,删除元素3：

        //1.给定一个数组：
        int[] arr = {12, 34, 56, 7, 3, 10, 34, 45, 56, 7, 666};

        //2.输出删除前的数组：
        System.out.println("删除元素前的数组：" + Arrays.toString(arr));


        //找到要删除的元素对应的索引即可：
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1200) {
                index = i;
                break;
            }
        }

        //3.删除

        if (index != -1) {
            for (int i = index; i <= arr.length - 2; i++) {
                arr[i] = arr[i + 1];
            }
            arr[arr.length - 1] = 0;
        } else {//index==-1
            System.out.println("根本没有你要删除的元素！");
        }


        //4.输出删除后的数组：
        System.out.println("删除元素后的数组：" + Arrays.toString(arr));
    }
}
