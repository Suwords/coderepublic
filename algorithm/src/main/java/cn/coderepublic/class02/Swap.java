package cn.coderepublic.class02;

/**
 * @description: 不用额外变量交换两个数
 * @author: shier
 * @date: 2023/1/5 07:13
 */
public class Swap {
    public static void main(String[] args) {
        int a = 16;
        int b = 603;

        System.out.println(a);
        System.out.println(b);

        a = a ^ b;
        b = a ^ b;
        a = a ^ b;

        System.out.println(a);
        System.out.println(b);

        int[] arr = {3, 1, 100};

        int i = 0;
        int j = 0;

        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];

        System.out.println(arr[i] + "," + arr[j]);

        System.out.println(arr[0]);
        System.out.println(arr[2]);

        swap(arr, 0, 0);

        System.out.println(arr[0]);
        System.out.println(arr[2]);
    }

    public static void swap(int[] arr, int i, int j) {
        // arr[0] = arr[0] ^ arr[0]
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }
}
