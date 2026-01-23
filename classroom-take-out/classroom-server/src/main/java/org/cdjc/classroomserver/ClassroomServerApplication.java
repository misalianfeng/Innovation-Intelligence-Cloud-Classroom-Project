package org.cdjc.classroomserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.cdjc.classroomserver.mapper")
public class ClassroomServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassroomServerApplication.class, args);
    }
}


