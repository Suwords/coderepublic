package cn.coderepublic.array;

public class TestArray15 {
    public static void main(String[] args) {
        //定义一个二维数组：
        int[][] arr = new int[3][];//本质上定义了一个一维数组，长度为3

        int[] a1 = {1, 2, 3};
        arr[0] = a1;

        arr[1] = new int[]{4, 5, 6, 7};

        arr[2] = new int[]{9, 10};

        //读取6这个元素：
        //System.out.println(arr[1][2]);

        //对二维数组遍历：
        //方式1：外层普通for循环+内层普通for循环：
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + "\t");
            }
            System.out.println();
        }

        //方式2：外层普通for循环+内层增强for循环：
        for (int i = 0; i < arr.length; i++) {
            for (int num : arr[i]) {
                System.out.print(num + "\t");
            }
            System.out.println();
        }

        //方式3：外层增强for循环+内层增强for循环：
        for (int[] a : arr) {
            for (int num : a) {
                System.out.print(num + "\t");
            }
            System.out.println();
        }

        //方式4：外层增强for循环+内层普通for循环：
        for (int[] a : arr) {
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[i] + "\t");
            }
            System.out.println();
        }
    }
}
