package cn.coderepublic.ifwhilefor;

public class TestFor03 {
    public static void main(String[] args) {
        //break的作用：停止最近的循环
                /*
                for(int i=1;i<=100;i++){
                        System.out.println(i);
                        if(i==36){
                                break;//1-36
                        }
                }
                */
        for (int i = 1; i <= 100; i++) {
            System.out.println(i);
            while (i == 36) {
                break; //1-100  ---》break停止的是while循环，而不是外面的for循环
            }
        }
    }
}
