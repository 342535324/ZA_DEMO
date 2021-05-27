package com.za.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rs.core.za.testUtil.annotation.ZA_InterfaceNotes;
import com.rs.core.za.testUtil.annotation.ZA_InterfaceNotesParameter;
import com.rs.core.za.testUtil.common.ZAParameter;

@Controller
@RequestMapping("/main")
public class MainController {
	@ZA_InterfaceNotes(name = "验证码登陆 请用POST请求 每天同ip 错误超过5次将被禁止当天登录 ", time = "2021-5-27 12:52:34") // 接口注解
	@ZA_InterfaceNotesParameter(name = "phone", type = ZAParameter.TYPE_String, describe = "手机号码") // 参数注解
	@ZA_InterfaceNotesParameter(name = "code", type = ZAParameter.TYPE_String, describe = "短信验证码") // 参数注解
	@RequestMapping("/login.app")
	@ResponseBody
	public String login(String phone, String code) {
		try {
			// 业务代码
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{code:1}";
	}
}
