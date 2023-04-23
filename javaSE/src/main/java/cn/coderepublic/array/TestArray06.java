package cn.coderepublic.array;

public class TestArray06 {
    public static void main(String[] args) {
        //查询指定元素的位置--》找出元素对应的索引
        //给定一个数组：
        int[] arr = {12, 34, 56, 7, 3, 56};
        //           0  1  2  3 4  5

        //功能：查询元素888对应的索引：
        //调用方法：
        int index = getIndex(arr, 999);
        //后续对index的值进行判断：
        if (index != -1) {
            System.out.println("元素对应的索引：" + index);
        } else {//index==-1
            System.out.println("查无次数！");
        }
    }

    /*
    定义一个方法：查询数组中指定的元素对应的索引：
    不确定因素：哪个数组，哪个指定元素  （形参）
    返回值：索引

    */
    public static int getIndex(int[] arr, int ele) {
        int index = -1; //这个初始值只要不是数组的索引即可
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == ele) {
                index = i;//只要找到了元素，那么index就变成为i
                break;//只要找到这个元素，循环就停止
            }
        }
        return index;
    }
}
