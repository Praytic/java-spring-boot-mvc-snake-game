package com.vchernogorov;

import com.vchernogorov.manager.GameManager;
import com.vchernogorov.manager.GameManagerImpl;
import com.vchernogorov.manager.SnakeManagerImpl;
import com.vchernogorov.manager.ViewManager;
import org.slf4j.Logger;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ComponentScan
@SpringBootConfiguration
@EnableScheduling
@EnableAsync
public class Application {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        return scheduler;
    }

    @Bean
    public GamePanel gamePanel(SnakeManagerImpl.SnakeAdapter snakeAdapter, GameManager gameManager,
                               ViewManager viewManager, GameManagerImpl.GameAdapter gameAdapter) {
        GamePanel panel = new GamePanel(viewManager::draw);
        panel.addKeyListener(snakeAdapter);
        panel.addKeyListener(gameAdapter);
        panel.setPreferredSize(gameManager.getField().getBorders().getSize());
        return panel;
    }

    @Bean
    public DemoFrame frame(GamePanel gamePanel) {
        return new DemoFrame(gamePanel);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).headless(false).run(args);
    }

    public static void debug(Logger logger, String pattern, Object... parameters) {
        log(logger, pattern, true, parameters);
    }

    public static void info(Logger logger, String pattern, Object... parameters) {
        log(logger, pattern, false, parameters);
    }

    public static void log(Logger logger, String pattern, boolean debug, Object... parameters) {
        if (parameters.length == 0) {
            logger.debug(pattern);
            return;
        }
        String[] splitedPattern = pattern.split("\\[\\]");
        String newPattern = "";
        int i = 0;
        for (; i < splitedPattern.length - 1; i++) {
            newPattern += splitedPattern[i] + "[{},{}]";
        }
        newPattern += splitedPattern[i];
        List newParameters = new ArrayList();
        for (Object parameter : parameters) {
            if (parameter instanceof Rectangle) {
                newParameters.add(((Rectangle) parameter).x);
                newParameters.add(((Rectangle) parameter).y);
            }
            else if (parameter instanceof Point) {
                newParameters.add(((Point) parameter).x);
                newParameters.add(((Point) parameter).y);
            }
            else {
                newParameters.add(parameter);
            }
        }
        if (debug) {
            logger.debug(newPattern, newParameters.toArray());
        }
        else {
            logger.info(newPattern, newParameters.toArray());
        }
    }
}
