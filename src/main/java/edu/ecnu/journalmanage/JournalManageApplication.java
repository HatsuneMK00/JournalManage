package edu.ecnu.journalmanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.ecnu.journalmanage.mapper")
public class JournalManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(JournalManageApplication.class, args);
    }

}
