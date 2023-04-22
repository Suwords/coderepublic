package cn.coderepublic.ifwhilefor;

public class TestFor11 {
    public static void main(String[] args) {
        //1*6=6   2*6=12  3*6=18  4*6=24  5*6=30  6*6=36
                /*
                System.out.print("1*6=6"+"\t");
                System.out.print("2*6=12"+"\t");
                System.out.print("3*6=18"+"\t");
                System.out.print("4*6=24"+"\t");
                System.out.print("5*6=30"+"\t");
                System.out.print("6*6=36"+"\t");

                for(int i=1;i<=6;i++){
                        System.out.print(i+"*6="+i*6+"\t");
                }
                //换行
                System.out.println();

                //1*7=7   2*7=14  3*7=21  4*7=28  5*7=35  6*7=42  7*7=49
                for(int i=1;i<=7;i++){
                        System.out.print(i+"*7="+i*7+"\t");
                }
                //换行
                System.out.println();

                //1*8=8   2*8=16  3*8=24  4*8=32  5*8=40  6*8=48  7*8=56  8*8=64
                for(int i=1;i<=8;i++){
                        System.out.print(i+"*8="+i*8+"\t");
                }
                //换行
                System.out.println();
                */

        for (int j = 1; j <= 9; j++) {
            for (int i = 1; i <= j; i++) {
                System.out.print(i + "*" + j + "=" + i * j + "\t");
            }
            //换行
            System.out.println();
        }
    }
}
