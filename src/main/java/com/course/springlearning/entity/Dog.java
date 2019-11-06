package com.course.springlearning.entity;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary：当有多个Pet的实现类时，优先选择该实现类的Bean注入Spring容器
@Primary
public class Dog implements Pet {
    @Override
    public void move() {
        System.out.println("running");
    }
}
