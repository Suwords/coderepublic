package cn.coderepublic.method;

public class TestMethod06 {
    public static void main(String[] args) {
        add(5);
        //级别：byte,short,char-->int-->long-->float--->double
    }

    public static void add(double num1) {
        System.out.println("------2");
    }

    public static void add(float num1) {
        System.out.println("------3");
    }

    public static void add(long num1) {
        System.out.println("------4");
    }
        /*
        public static void add(int num1){
                System.out.println("------1");
        }
        */
}
