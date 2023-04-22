package cn.coderepublic.ifwhilefor;

public class TestDoWhile {
    public static void main(String[] args) {
        //1+2+3+4+...100
        //while方式:
                /*
                int i = 101;
                int sum = 0;
                while(i<=100){
                        sum += i;
                        i++;
                }
                System.out.println(i);//101
                System.out.println(sum);//0
                */
        //do-while方式：

        int i = 101;
        int sum = 0;
        do {
            sum += i;
            i++;
        } while (i <= 100);//一定要注意写这个分号，否则编译出错
        System.out.println(i);//102
        System.out.println(sum);//101
                /*
                【1】while和do-while的区别:
                        while:先判断，再执行
                        do-while:先执行，再判断---》至少被执行一次，从第二次开始才进行判断
                【2】什么场合使用do-while:

                while(考试是否通过){
                        考试；
                }
                ---》不合适
                do{
                        考试；
                }while(考试是否通过);
                ---》合适
                */

    }
}
