package com.course.springlearning.aspect.proxy_demo;

public class ALiPayMent implements IPayMent {
    private IPayMent payMent;

    public ALiPayMent(IPayMent payMent) {
        this.payMent = payMent;
    }

    public void afterPay() {
        System.out.println("后置操作");
    }

    public void beforePay() {
        System.out.println("前置操作");
    }

    @Override
    public void pay() {
        beforePay();
        payMent.pay();
        afterPay();
    }


}
