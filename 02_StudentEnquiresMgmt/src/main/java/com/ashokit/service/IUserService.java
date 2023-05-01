package com.ashokit.service;

import com.ashokit.binding.LoginForm;
import com.ashokit.binding.SignUpForm;
import com.ashokit.binding.UnlockForm;

public interface IUserService {
	
	public boolean signUp(SignUpForm signupfrm);	
	  
	public boolean unlockAccount(UnlockForm unclockFrm);

	public String login(LoginForm loginfrm);
	public String forgetPwd(String email);
}
