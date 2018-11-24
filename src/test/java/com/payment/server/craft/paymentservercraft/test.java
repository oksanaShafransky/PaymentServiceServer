package com.payment.server.craft.paymentservercraft;

import java.util.Random;

public class test {

    public static void main(String [] args){
        Random random = new Random();
        int count = 0;
        for(int i=0;i<100000;i++) {
            int n = Math.abs(random.nextInt(10)) / 7;
            if(n>0)
                count++;
        }
        System.out.println("count = " +count);
    }
}
