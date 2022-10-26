package com.orange.fileprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FileProcessingApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(FileProcessingApplication.class, args);
    }

}
