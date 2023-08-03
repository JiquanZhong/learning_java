package com.itranswarp.learnjava.util.impl;

import com.itranswarp.learnjava.util.Validator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ZHONG Jiquan
 * @create 03/08/2023 - 16:57
 */
@Component
@Order
public class NameValidator implements Validator {
	@Override
	public void validate(String email, String password, String name) {
		if (name == null || name.isBlank() || name.length() > 20) {
			throw new IllegalArgumentException("invalid name: " + name);
		}
	}
}
