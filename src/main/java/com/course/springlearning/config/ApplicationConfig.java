package com.course.springlearning.config;

import com.course.springlearning.entity.Person;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ApplicationConfig {
    //方法一：通过javaConfig的形式将bean注入到Spring容器中，会显得繁琐，所以有了扫描装配bean
    @Bean(name = "person")//如果不定义name,则beanName为方法名，此处为initPerson
    public Person initPerson(){
        Person person = new Person();
        person.setId(1L);
        person.setName("李四");
        return person;
    }

    //ES配置
    @Bean
    public TransportClient transport() throws UnknownHostException {
        InetSocketTransportAddress node = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300);

//        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();

        TransportClient transportClient = new PreBuiltTransportClient(settings);
        transportClient.addTransportAddress(node);
        return transportClient;
    }
}
