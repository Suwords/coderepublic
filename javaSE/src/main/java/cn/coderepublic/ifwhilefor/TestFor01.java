package cn.coderepublic.ifwhilefor;

public class TestFor01 {
    public static void main(String[] args) {
        //1+2+3+..+100
        //while:
                /*int i = 1;
                int sum = 0;
                while(i<=100){
                        sum += i;
                        i++;
                }
                System.out.println(sum);
                */

        //for:
        int sum = 0;
        int i;
        for (i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println(sum);
        System.out.println(i);

                /*
                【1】for的结构：
                for(条件初始化;条件判断;迭代){
                        循环体；
                }

                【2】i的作用域：作用范围：离变量最近{}  --->可以自己去控制
                【3】for循环格式特别灵活：格式虽然很灵活，但是我们自己写代码的时候不建议灵活着写。
                for(;;){}  -->死循环

                int i = 1;
                for(;i<=100;){
                        sum += i;
                        i++;
                }

                【4】死循环：
                for(;;){}

                while(true){}

                do{

                }while(true);

                【5】循环分为两大类：
                第一类：当型   while(){}   for(;;){}
                第二类：直到型  do{}while();

                【6】以后常用：for循环
                【7】do-while,while,for循环谁的效率高？  一样高
                */
    }
}
