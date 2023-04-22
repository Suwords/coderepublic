package cn.coderepublic.ifwhilefor;

public class TestIf01 {
    public static void main(String[] args) {
        //实现一个功能：给出三个数（1-6），对三个数求和计算，根据和的大小来分配不同的奖品
        //1.给出三个数：
        int num1 = 6;
        int num2 = 2;
        int num3 = 3;
        //2.求和
        int sum = 0;
        sum += num1;
        sum += num2;
        sum += num3;
        System.out.println("和为：" + sum);

        //3.根据和判断奖品：
        //如果和大于等于14，那么就是一等奖
        if (sum >= 14) {
            System.out.println("一等奖");
            System.out.println("恭喜你很幸运，中了一等奖");
        }

        if (sum >= 10 && sum < 14) {
            System.out.println("二等奖");
        }

        if (sum >= 6 && sum < 10) {
            System.out.println("三等奖");
        }

        if (sum < 6) {
            System.out.println("四等奖");
        }

                /*
                if-单分支：
                （1）结构：
                        if(条件表达式，这个表达式的结果是布尔值：要么是false，要么是true){
                                //如果上面()中的表达式返回结果是true，那么执行{}中代码
                                //如果上面()中的表达式返回结果是false ，那么不执行{}中代码
                                //PS:{}中的代码是否执行，取决于()中表达式的返回结果
                        }
                （2）上面的代码中，我用四个单分支拼凑出四个选择，每个选择是独立的，依次判断执行的
                （3）if后面的()中的条件，要按照自己需求尽量完善
                （4）{}可以省略不写,但是一旦省略，这个if就只负责后面的一句话，所以我们不建议初学者省略
                */
    }
}
