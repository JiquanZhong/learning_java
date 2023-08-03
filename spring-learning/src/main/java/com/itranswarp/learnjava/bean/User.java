package com.itranswarp.learnjava.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 15:55
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private long id;
  private String email;
  private String password;
  private String name;
}
