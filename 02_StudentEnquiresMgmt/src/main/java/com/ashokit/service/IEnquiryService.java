package com.ashokit.service;

import java.util.List;

import com.ashokit.binding.DashboardResponse;
import com.ashokit.binding.EnquiryForm;
import com.ashokit.binding.EnquirySearchCriteria;
import com.ashokit.entity.StudentEnqEntity;

public interface IEnquiryService {
	
	public DashboardResponse getDasboardData(Integer id);
	public String upsertEnquiry(EnquiryForm enqFrm,Integer userId,Integer enqFrmId);
	public List<StudentEnqEntity> getAllStudentEnquires(Integer id);
	public EnquiryForm getOneEnquires(Integer id);
	public List<String> getCourseName();
	public List<String> getEnqStatus();
	
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria search,Integer userId);
}
