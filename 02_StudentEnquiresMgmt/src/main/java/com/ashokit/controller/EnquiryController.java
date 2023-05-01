package com.ashokit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ashokit.binding.DashboardResponse;
import com.ashokit.binding.EnquiryForm;
import com.ashokit.binding.EnquirySearchCriteria;
import com.ashokit.entity.StudentEnqEntity;
import com.ashokit.service.IEnquiryService;

@Controller
public class EnquiryController {
	@Autowired
	private IEnquiryService service;
	@Autowired
	private HttpSession session;
	
	
	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		//logic to fetch data for dashboard
		System.out.println("Dashboard method called...");
		Integer userId=(Integer) session.getAttribute("userId");
		System.out.println("UserId:: "+userId);
		DashboardResponse dashboardData=service.getDasboardData(userId);
		model.addAttribute("dashboardData", dashboardData);
		return "dashboard";
	}
	
	@GetMapping("/add_enquiry")
	public String addEnquiryPage(@ModelAttribute("enquiries")EnquiryForm enqFrm,Model model,@RequestParam(value="enquiryId", required=false)Integer id) {
		//for Edit form
		if(id!=null) {
		    EnquiryForm enqFrm1 = service.getOneEnquires(id);
		    model.addAttribute("enquiries", enqFrm1);
		    model.addAttribute("enquiryId", id);
		}
		//get courses for drop down
		//get enq status for drop down
		//create binding class object
		//set data in model object
		initForm(model);
		return "add-enquiry";
	}
	
	@PostMapping("/add_enquiry")
	public String addEnquiryData(@ModelAttribute("enquiries")EnquiryForm enqFrm ,
			                     RedirectAttributes attr  ,
			                     @RequestParam(value="enquiryId", required=false)Integer enqFrmId ) {
		System.out.println("EnquiryController.addEnquiryData()"+enqFrm);
	    System.out.println("EnqFrmId: "+enqFrmId);
	    //save the data based on userId 
	  	Integer userId=(Integer) session.getAttribute("userId");
	  	
	  	//update records
		if(enqFrmId!=null) {
		    String msg=service.upsertEnquiry(enqFrm, null,enqFrmId);
		    attr.addFlashAttribute("succMsg", msg);
		}
		else {
			//insert records
			String msg=service.upsertEnquiry(enqFrm,userId,null);
			attr.addFlashAttribute("succMsg", msg);
		}
     	return "redirect:add_enquiry";
	}
	
	
	
	@GetMapping("/view_enquiries")
	public String viewPage(@ModelAttribute("enquiries")EnquirySearchCriteria enqSearch,Model model) {
		initForm(model);
		Integer userId=(Integer)session.getAttribute("userId");
		List<StudentEnqEntity> list=null;
		if(userId!=null) {
			 list=  service.getAllStudentEnquires(userId);
		}
		model.addAttribute("list", list);
		return "view-enquiries";
	}
	
	@PostMapping("/view_enquiries")
	public String viewPageData(@ModelAttribute("enquiries")EnquirySearchCriteria enqSearch) {
		System.out.println("EnquiryController.viewPageData()"+enqSearch);
		return "view-enquiries";
	}
	
	@GetMapping("/logout")
	public String logoutPage() {
		session.invalidate();
		return "index";
	}
	
	@GetMapping("/filter-enquiries")
	private String getFilteredEnqs(@RequestParam("course")String course,
			                       @RequestParam("status")String status,
			                       @RequestParam("mode")String mode,
			                       RedirectAttributes attr,
			                       Model model) {
		EnquirySearchCriteria search= new EnquirySearchCriteria();
		search.setCourseName(course);
		search.setEnquiryStatus(status);
		search.setClassMode(mode);
		System.out.println("EnquiryController.getFilteredEnqs()"+search);
		Integer userId=(Integer)session.getAttribute("userId");
		System.out.println("userId : "+userId);
        List<StudentEnqEntity> list = service.getFilteredEnqs(search, userId);
        model.addAttribute("list", list);
        System.out.println("list : "+list.size());
		return "filter-enquiries";
	}
	
	private void initForm(Model model) {
		//get courses for drop down
		List<String> courses=service.getCourseName();
		
		//get enq status for drop down
		List<String> status=service.getEnqStatus();
			
		//set data in model class object
		model.addAttribute("status", status);
		model.addAttribute("course", courses);
	}
	
}
