package com.course.springlearning.aspect.proxy_demo;

public class RealIPayMent implements IPayMent {
    @Override
    public void pay() {
        System.out.println("支付");
    }
}
