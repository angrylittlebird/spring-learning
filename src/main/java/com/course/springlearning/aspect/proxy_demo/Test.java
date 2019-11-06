package com.course.springlearning.aspect.proxy_demo;

public class Test {
    public static void main(String[] args) {
        IPayMent payMent = new ALiPayMent(new RealIPayMent());
        payMent.pay();
    }
}
