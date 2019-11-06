package com.course.springlearning.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//方案二：扫描装配的形式，无需一个个手动注册，@Component和@ComponentScan配合使用
@Component
public class People {
    @Value("1")
    private Long id;
    @Value("Jack")
    private String name;

    //根据类型查找后注入，但是Pet接口有多个实现怎么办？
    //Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans,
    // or using @Qualifier to identify the bean that should be consumed
    @Autowired
    @Qualifier("bird")//因为Pet接口有多个实现类，指定其中的Bird实现类注入Spring容器
    private Pet pet;

    public void move(){
        pet.move();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
