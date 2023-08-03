package com.itranswarp.learnjava.util;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 16:57
 */
public class Validators {
  /**
   * Spring会自动把所有类型为Validator的Bean装配为一个List注入进来
   * 这个参数告诉Spring容器，如果找到一个类型为Validator的Bean，就注入，如果找不到，就忽略。
   */
  @Autowired(required = false)
  List<Validator> validators;

  public void validate(String email, String password, String name) {
    for (var validator : this.validators) {
      validator.validate(email, password, name);
    }
  }
}
