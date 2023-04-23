package cn.coderepublic.array;

public class TestArray16 {
    public static void main(String[] args) {
        int[][] arr = new int[3][2];
        //本质上：定义一维数组，长度为3，每个数组“格子”中，有一个默认的长度为2的数组：

        arr[1] = new int[]{1, 2, 3, 4};

        //数组遍历：
        for (int[] a : arr) {
            for (int num : a) {
                System.out.print(num + "\t");
            }
            System.out.println();
        }

    }
}
