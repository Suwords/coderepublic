package cn.coderepublic.ifwhilefor;

public class TestWhile02 {
    public static void main(String[] args) {
                /*
                【1】1+2+3+4+5+。。。。+100
                int i = 1;
                int sum = 0;
                while(i<=100){
                        sum += i;
                        i++;
                }
                System.out.println(sum);
                【2】2+4+6+8+。。。。+998+1000
                int i = 2;
                int sum = 0;
                while(i<=1000){
                        sum += i;
                        i = i+2;
                }
                System.out.println(sum);
                【3】5+10+15+20+。。。+100
                int i = 5;
                int sum = 0;
                while(i<=100){
                        sum += i;
                        i = i+5;
                }
                System.out.println(sum);

                【4】99+97+95+。。5+3+1
                int i = 99;
                int sum = 0;
                while(i>=1){
                        sum += i;
                        i = i-2;
                }
                System.out.println(sum);
                【5】1*3*5*7*9*11*13

                */
        int i = 1;
        int result = 1;
        while (i <= 13) {
            result *= i;
            i = i + 2;
        }
        System.out.println(result);
    }
}
