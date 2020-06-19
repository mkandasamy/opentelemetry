package com.lfg.poc.opentelemetry.itemservice;

//import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);
    
    
    //@Autowired
    //private ApplicationContext appContext;
    
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Your application started with option names : {}", args.getOptionNames());
        
        //Arrays.asList(appContext.getBeanDefinitionNames()).stream().sorted().forEach(System.out::println);
        
        
    }
}