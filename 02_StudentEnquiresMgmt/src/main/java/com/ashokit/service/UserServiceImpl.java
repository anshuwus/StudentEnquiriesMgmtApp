package com.ashokit.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashokit.binding.LoginForm;
import com.ashokit.binding.SignUpForm;
import com.ashokit.binding.UnlockForm;
import com.ashokit.entity.UserDtlsEntity;
import com.ashokit.repository.IUserDtlsRepo;
import com.ashokit.utils.EmailUtils;
import com.ashokit.utils.PwdUtils;

@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	private IUserDtlsRepo repo;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private HttpSession session;
	
	@Override
	public boolean signUp(SignUpForm signupfrm) {
		UserDtlsEntity userEntity= repo.findByEmail(signupfrm.getEmail());
		if(userEntity!=null)
			return false;
		//generate temp password
		String tempPwd=PwdUtils.generatePwd();
			
		//create Entity class object
		UserDtlsEntity entity=new UserDtlsEntity();
		//copy binding form data to entity object
      	BeanUtils.copyProperties(signupfrm, entity);
    	entity.setPwd(tempPwd);
				
		//set account status as Locked
		entity.setAccStatus("LOCKED");
		
		//save user signup details in DB(insert record)
		repo.save(entity);
		
		//send temp email to user to unlock the account
		String to=signupfrm.getEmail();
		String subject="Please Unlock Your Account";
		StringBuffer body=new StringBuffer("");
		body.append("<h1>Use below temporary password to unlock your account</h1>");
		body.append("<p>Dear, "+signupfrm.getName()+" thanks for creating your account,</p><p>Your temporary password: "+tempPwd+"</p><p> In order to login in our app please click below link.</p>");
		body.append("<a href=\"http://localhost:8081/unlock?email="+to+"\">click the link here</a>");
		emailUtils.sendEmail(to, subject, body);
		
		return true;
	}

	@Override
	public boolean unlockAccount(UnlockForm unlockFrm) {
		UserDtlsEntity entity= repo.findByEmail(unlockFrm.getEmail());
		if(entity.getPwd().equals(unlockFrm.getTempPwd())) {
			entity.setPwd(unlockFrm.getConfirmPwd());
			entity.setAccStatus("UNLOCKED");
			repo.save(entity);
			return true;
		}
		else
		    return false;
	}

	@Override
	public String login(LoginForm loginfrm) {
		UserDtlsEntity entity=repo.findByEmailAndPwd(loginfrm.getEmail(), loginfrm.getPwd());
		System.out.println("UserServiceImpl.login()"+entity);
		if(entity==null) {
			return "Invalid Credentials";
		}
		if(entity.getAccStatus().equals("LOCKED")) {
			return "Your Account is Locked";
		}
		//create session and store user data in session object
		session.setAttribute("userId", entity.getUserId());
		return "success";
	}
	
	
	@Override
	public String forgetPwd(String email) {
		//check record presence in db with given email
		UserDtlsEntity entity=repo.findByEmail(email);
		String status=null;
		//if record not available send error msg
		if(entity==null) {
			return "No records is found with this email id";
		}
		if(entity.getAccStatus().equals("LOCKED")) {
			return "Your Account is Locked";
		}
		//if record available send pwd to email and send success msg
	    if(entity.getEmail().equals(email)) {
			String pwd=entity.getPwd();
			String subject="Recover Password";
			StringBuffer body= new StringBuffer("");
			body.append("<h1>Please recover your password</h1>");
			body.append("<p>Dear, "+entity.getName()+"</p><p> we've received your request for recovering your password.</p><p> Below is your password.</p>");
			body.append("<p>Your Password:"+pwd+"</p>");
			body.append("<a href=\"http://localhost:8081/login\">click here to login your account</a>");
			emailUtils.sendEmail(email, subject, body);
			status="success";
		}
		return status;
	}
	
	
}