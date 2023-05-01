package com.ashokit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ashokit.binding.LoginForm;
import com.ashokit.binding.SignUpForm;
import com.ashokit.binding.UnlockForm;
import com.ashokit.service.IUserService;

@Controller
public class UserController {
	@Autowired
	private IUserService service;
	
	@GetMapping("/signup")
	public String createAccountPage(@ModelAttribute("signup")SignUpForm signupFrm,
			                         @RequestParam(name="succMsg",required=false)String msg,
			                         @RequestParam(name="errMsg",required=false)String msg1,
			                         Model model) {
		model.addAttribute("succMsg", msg);
		model.addAttribute("errMsg", msg1);
		return "signup";
	}
	
	@PostMapping("/signup")
	public String saveSignupData(@ModelAttribute("signup")SignUpForm signupFrm,RedirectAttributes attributes) {
		boolean isSuccess=service.signUp(signupFrm);
		if(isSuccess)
			attributes.addAttribute("succMsg", "Account created,\n please check your email temporary password has \n been sent to your registered email account");
		else
			attributes.addAttribute("errMsg", "Your email id is already reistered \n choose unique email");
		return "redirect:signup";
	}
	
	@GetMapping("/unlock")
	public String unlockAccount(@ModelAttribute("unlock")UnlockForm unlockFrm,@RequestParam(name="email",required=false)String email,Model model) {
		
		System.out.println("UserController.unlockAccount()"+unlockFrm);
		model.addAttribute("email", email);
		return "unlock";
	}
	
	@PostMapping("/unlock")
	public String saveUnlockAccountData(@ModelAttribute("unlock")UnlockForm unlockFrm,RedirectAttributes attr,Model model) {
		System.out.println("UserController.saveUnlockAccountData()"+unlockFrm);
		if(unlockFrm.getNewPwd().equals(unlockFrm.getConfirmPwd())) {
			boolean status=service.unlockAccount(unlockFrm);
			if(status) {
				model.addAttribute("succMsg", "Your account unlocked");
				model.addAttribute("email", unlockFrm.getEmail());
			}
			else {
				model.addAttribute("errMsg", "Given Temporary password is wrong");
				model.addAttribute("email", unlockFrm.getEmail());
			}
		}else {
			model.addAttribute("errMsg","New password and confirm password should be matched");
			model.addAttribute("email", unlockFrm.getEmail());
		}
		return "unlock";
	}
	
	@GetMapping("/login")
	public String loginPage(@ModelAttribute("login")LoginForm loginFrm) {
		return "login";
	}
	
	@PostMapping("/login")
	public String loginData(@ModelAttribute("login")LoginForm loginFrm,Model model) {
		System.out.println("UserController.loginData()"+loginFrm);
		String status=service.login(loginFrm);
		if(status.contains("success")) {
			
			//display dashoard
			return "redirect:/dashboard";
		}
		model.addAttribute("errMsg", status);
		model.addAttribute("login", new LoginForm());
		return "login";
	}
	
	
	
	
	@GetMapping("/forgotPwd")
	public String forgotPwdPage() {
		return "forgotPwd";
	}
	
	@PostMapping("/forgotPwd")
	public String forgotPwdData(@RequestParam("email")String email,Model model) {
		System.out.println("UserController.forgotPwdData()"+email);
		String status=service.forgetPwd(email);
		if(status.contains("success"))
			model.addAttribute("succMsg", "Password recovered successful, Your current password is sent on your registered email id.");
		else {
			model.addAttribute("errMsg", status);
		}
		return "forgotPwd";
	}

}
