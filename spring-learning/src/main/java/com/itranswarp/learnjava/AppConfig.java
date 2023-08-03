package com.itranswarp.learnjava;

import com.itranswarp.learnjava.service.User;
import com.itranswarp.learnjava.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.time.ZoneId;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 15:54
 */
@ComponentScan
@Configuration
public class AppConfig {
  public static void main(String[] args) {
    //		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
    //    BeanFactory context = new XmlBeanFactory(new ClassPathResource("application.xml"));
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    // 获取Bean:
    UserService userService = context.getBean(UserService.class);
    // 正常调用:
    User user = userService.login("bob@example.com", "password");
  }

  /** 创建第三方Bean 我们自己在@Configuration类中编写一个Java方法创建并返回它，注意给方法标记一个@Bean注解 */
  @Bean("Z")
  ZoneId createZoneId() {
    return ZoneId.of("Z");
  }

  /**
   * 可以用@Bean("name")指定别名，也可以用@Bean+@Qualifier("name")指定别名。
   * @Primary: 如果没有指出Bean的名字，Spring会注入标记有@Primary的Bean
   */
  @Bean
  @Primary
  @Qualifier("utc8")
  ZoneId createZoneOfUTC8() {
    return ZoneId.of("UTC+08:00");
  }
}
