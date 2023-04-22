package cn.coderepublic.variable;

/*
局部变量：定义在方法中
成员变量：定义在类中，方法外
*/
public class TestVar04 {
    int b = 20;

    public static void main(String[] args) {
//        System.out.println(a);//no
        int a = 10;
        System.out.println(a);//yes
//        System.out.println(b);//no
        {
            int c = 40;
            System.out.println(c);//yes
//            int a = 50;//属于变量的重复定义
        }
//        System.out.println(c);//no
    }

    public void eat() {
        System.out.println(b);//yes
//        System.out.println(a);//no
        int a = 30;//不是变量的重复定义

        System.out.println(a);//yes
    }
}
