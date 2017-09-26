package com.zorro.manage.controller.user;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;
import com.zorro.manage.common.constant.ErrorInfoConst;
import com.zorro.manage.common.framework.JsonResult;
import com.zorro.manage.common.framework.LoginStatusCheck;
import com.zorro.manage.controller.BaseController;
import com.zorro.manage.controller.user.param.DoRegisterParam;
import com.zorro.manage.model.User;
import com.zorro.manage.service.IUserService;
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
//	@Autowired
//	IUserService userService;
	
	@RequestMapping("/login")
	public ModelAndView login() {
		
		return new ModelAndView("user/login");
	}
	
	@RequestMapping("/register")
	@LoginStatusCheck
	public ModelAndView register() {
		
		return new ModelAndView("user/register");
	}
	
	@ResponseBody
	@RequestMapping("/doRegister")
	public JsonResult doRegister(DoRegisterParam param) {
		// 常规请求参数验证
		if (checkDoRegisterParam(param) == false) {
			return new JsonResult(0, ErrorInfoConst.PARAM_ILLEGAL);
		}
		
		// 密码是否合法验证
		boolean passwordLegal = false;
		if (param.getPassword().equals(param.getRepeatPassword())) {
			passwordLegal = true;
		}
		if (!passwordLegal) {
			return new JsonResult(0, ErrorInfoConst.PWD_ILLEGAL);
		}
		
		// 邮箱是否已经注册
//		boolean emailExist = userService.checkEmailIsExist(param.getEmail());
//		if (emailExist) {
//			return new JsonResult(0, ErrorInfoConst.EMAIL_EXIST);
//		}
		
		User user = new User();
		user.setEmail(param.getEmail());
		user.setNickname(param.getNickname());
		user.setPassword(param.getPassword());
		user.setCreateTime(LocalDateTime.now(ZoneId.systemDefault()));
		user.setLastLoginTime(LocalDateTime.now(ZoneId.systemDefault()));
		
		//userService.registerNewUser(user);
		response.addCookie(new Cookie("lzcookie", "asdfasdfas"));
		response.addHeader("lzheader", "lztest");
		
		return new JsonResult(1, "it is a test");
	}

	private boolean checkDoRegisterParam(DoRegisterParam param) {
		if (param == null) {
			return false;
		}
		
		// 所有参数均不能为空
		if (Strings.isNullOrEmpty(param.getEmail()) || Strings.isNullOrEmpty(param.getNickname())
				|| Strings.isNullOrEmpty(param.getPassword()) || Strings.isNullOrEmpty(param.getRepeatPassword())) {
			return false;
		}
		
		// 邮箱格式校验
		boolean emailCheck = Pattern.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$", param.getEmail());
		if (!emailCheck) {
			return false;
		}
		
		return true;
	}
}