package com.example.server_interface;

import com.example.common_resource.config.SystemConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.example"})
@MapperScan(basePackages = {"com.example"})
@SpringBootApplication
public class NormalServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NormalServerApplication.class, args);
        System.out.println("****************************************************");
        System.out.println("Name: " + SystemConfig.getName());
        System.out.println("Version: " + SystemConfig.getVersion());
        System.out.println("Copyright: " + SystemConfig.getCopyright());
        System.out.println("Normal Server Starts......");
        System.out.println("****************************************************");
    }

}
