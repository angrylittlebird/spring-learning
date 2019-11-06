package com.course.springlearning;

import com.course.springlearning.entity.People;
import com.course.springlearning.entity.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

//@ComponentScan 扫描当前包及其子包下的注有@Component的类
@SpringBootApplication
public class SpringLearningApplication {

    public static void main(String[] args) {

        //run方法返回的是ConfigurableApplicationContext，该类继承自ApplicationContext，即Spring核心容器类之一(另一个是BeanFactory，ApplicationContext也继承了BeanFactory）
        ApplicationContext applicationContext = SpringApplication.run(SpringLearningApplication.class, args);
        //获取Spring容器中的Bean实例,这里根据类型获取实例，同样的@AutoWired 也是通过类型查找自动注入
        Person person = applicationContext.getBean(Person.class);
        System.out.println("name is " + person.getName());
        People people = applicationContext.getBean(People.class);
        System.out.println("name is " + people.getName() );
        people.move();
    }

}
