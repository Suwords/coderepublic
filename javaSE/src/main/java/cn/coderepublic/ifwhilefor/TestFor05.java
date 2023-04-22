package cn.coderepublic.ifwhilefor;

public class TestFor05 {
    public static void main(String[] args) {
        //功能：输出1-100中被6整除的数：
        //方式1：
                /*
                for(int i=1;i<=100;i++){
                        if(i%6==0){//被6整除
                                System.out.println(i);
                        }
                }
                */

        //方式2：
        for (int i = 1; i <= 100; i++) {
            if (i % 6 != 0) {//不被6整除
                continue;//停止本次循环，继续下一次循环
            }
            System.out.println(i);
        }
    }
}
