package com.itranswarp.learnjava.util.impl;

import com.itranswarp.learnjava.util.Validator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 16:56
 */
@Component
@Order
public class PasswordValidator implements Validator {
	@Override
	public void validate(String email, String password, String name) {
		if (!password.matches("^.{6,20}$")) {
			throw new IllegalArgumentException("invalid password");
		}
	}
}
