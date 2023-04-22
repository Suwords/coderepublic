package cn.coderepublic.ifwhilefor;

public class TestFor09 {
    public static void main(String[] args) {
        /* 输出1-100中被5整除的数,每行输出6个*/
        //引入一个计数器：
        int count = 0;//初始值为0
        for (int i = 1; i <= 100; i++) {
            if (i % 5 == 0) {//被5整除的数
                System.out.print(i + "\t");
                count++;//每在控制台输出一个数，count就加1操作
                if (count % 6 == 0) {
                    System.out.println();//换行
                }
            }
        }
    }
}
