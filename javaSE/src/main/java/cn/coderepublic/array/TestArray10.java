package cn.coderepublic.array;

public class TestArray10 {
    public static void main(String[] args) {
        //从侧面验证：
        //int[] arr1; //如果对数组只声明，没有后续操作，那么相当于 白定义了。
        //int[] arr2 = null;
        //System.out.println(arr2.length);//Exception in thread "main" java.lang.NullPointerException
        //int[] arr3 = new int[0];
        //System.out.println(arr3.length);
        //int[] arr4 = new int[4];
        //System.out.println(arr4.length);

        //System.out.println(args.length);//0
        //从这个结果证明，参数是String[],实参是  new String[0]
        //默认情况下，虚拟机在调用main方法的时候就是传入了一个长度为0的数组

        System.out.println(args.length);
        for (String str : args) {
            System.out.println(str);
        }
    }
}
