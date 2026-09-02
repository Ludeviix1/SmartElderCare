package com.elder;

public class RecursionDemo {
    private static int count = 0;

    /*public static void main(String[] args) {
        count++;
        System.out.println("count: " + count);
        main(args);
        System.out.println("over");
    }*/

    public static void main(String[] args) {
        //System.out.println(factorial1(5));
        System.out.println(factorial(5));
    }

    /**
     * n!=n*(n-1)*(n-2)*(n-3)*...*3*2*1
     * 5!=5*4*3*2*1
     * factorial(5)=5*factorial(4)      5*24=120
     * factorial(4)=4*factorial(3)      4*6=24
     * factorial(3)=3*factorial(2)      3*2=6
     * factorial(2)=2*factorial(1)      2*1=2
     * factorial(1)=1                   1
     factorial(n)=n*factorial(n-1)
     */
    public static int factorial(int n) {
        //递归退出条件
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }


    //n!=n*(n-1)*(n-2)*(n-3)*...*3*2*1
    public static int factorial1(int n) {
        int result = 1;
        for (int i = n; i >= 1; i--) {
            result *= i;
        }
        return result;
    }
}
