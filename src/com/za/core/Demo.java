package com.za.core;

import com.rs.core.exception.ZACheckException;
import com.rs.core.za.check.annotation.ZA_IsAccount;
import com.rs.core.za.check.annotation.ZA_IsPassword;
import com.rs.core.za.check.annotation.ZA_IsPhone;
import com.rs.core.za.check.util.ZAUtil;

/**
 * 实体类注解DEMO 实体类的校验注解需要搭配ZAUtil.checkModel使用
 */
public class Demo {
	@ZA_IsAccount // 账号校验注解
	private String account;
	@ZA_IsPassword(minlength = 8, maxlength = 12, msg = "密码不能为空且长度需在8-12位内") // 密码校验注解
	private String password;
	@ZA_IsPhone // 手机号码校验注解
	private String phone;

	public static void main(String[] args) {
		// 实体类校验测试
		Demo demo = new Demo("Abc", "123", "123");
		try {
			// 校验属性,校验不通过会抛出异常
			ZAUtil.checkModel(demo);
		} catch (ZACheckException e) {
			// 输出错误 密码不能为空且长度需在8-12位内
			e.printStackTrace();
		}
	}

	public Demo(String account, String password, String phone) {
		super();
		this.account = account;
		this.password = password;
		this.phone = phone;
	}
}
