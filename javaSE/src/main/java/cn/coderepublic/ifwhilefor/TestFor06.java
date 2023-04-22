package cn.coderepublic.ifwhilefor;

public class TestFor06 {
    public static void main(String[] args) {
        //continue:结束本次离它近的循环，继续下一次循环
                /*
                for(int i=1;i<=100;i++){
                        if(i==36){
                                continue;//1-100中间没有36
                        }
                        System.out.println(i);
                }
                */

        for (int i = 1; i <= 100; i++) {
            while (i == 36) {
                System.out.println("------");
                continue; //1-35+死循环
            }
            System.out.println(i);
        }
    }
}
