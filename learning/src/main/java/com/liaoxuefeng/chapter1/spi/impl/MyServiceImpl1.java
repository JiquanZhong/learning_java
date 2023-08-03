package com.liaoxuefeng.chapter1.spi.impl;

import com.liaoxuefeng.chapter1.spi.MyService;

/**
 * @author ZHONG Jiquan
 * @create 05/08/2023 - 05:03
 */
// MyServiceImpl1.java
public class MyServiceImpl1 implements MyService {
	@Override
	public void doSomething() {
		System.out.println("MyServiceImpl1 is doing something.");
	}
}

