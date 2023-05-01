package com.ashokit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashokit.binding.DashboardResponse;
import com.ashokit.binding.EnquiryForm;
import com.ashokit.binding.EnquirySearchCriteria;
import com.ashokit.entity.CourseEntity;
import com.ashokit.entity.EnqStatusEntity;
import com.ashokit.entity.StudentEnqEntity;
import com.ashokit.entity.UserDtlsEntity;
import com.ashokit.repository.ICourseRepo;
import com.ashokit.repository.IEnqStatusRepo;
import com.ashokit.repository.IStudentEnqRepo;
import com.ashokit.repository.IUserDtlsRepo;

@Service
public class EnquiryServiceImpl implements IEnquiryService {
	@Autowired
	private IStudentEnqRepo studentEnqRepo;
	@Autowired
	private IUserDtlsRepo userRepo;
	@Autowired
	private IEnqStatusRepo statusRepo;
	@Autowired
	private ICourseRepo courseRepo;
		
	@Override
	public DashboardResponse getDasboardData(Integer userId) {
		DashboardResponse response=new DashboardResponse();
		Optional<UserDtlsEntity> opt = userRepo.findById(userId);
		if(opt.isPresent()) {
			UserDtlsEntity userDtlsEntity = opt.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			Integer totalCnt = enquiries.size();
			Integer enrolledCnt = enquiries.stream()
			                               .filter(enq -> enq.getEnquiryStatus().equalsIgnoreCase("enrolled"))
			                               .collect(Collectors.toList())
	                                       .size();
			Integer lostCnt = enquiries.stream()    
					                   .filter(enq -> enq.getEnquiryStatus().equalsIgnoreCase("lost"))
			                           .collect(Collectors.toList())
			                           .size();
			response.setEnrolledCount(enrolledCnt);
			response.setLostCount(lostCnt);
			response.setTotalEnqCount(totalCnt);
		}
		/*
		//find records based on StudentEnqEntity using foriegn key(FK)
		List<StudentEnqEntity> list=studentEnqrepo.getRecordBasedOnUserId(userId);
		if(list==null) {
			System.out.println("LIST IS EMPTY HOLDING NULL VALUE:: "+list);
		}
		int newEnrolledCount=0;
		int enrolledCount=0;
		int lostCount=0;
		DashboardResponse dashboardRes=new DashboardResponse();
		if(list!=null) {
			for(StudentEnqEntity entity:list) {
				if(entity.getEnquiryStatus().equalsIgnoreCase("new")) {
					newEnrolledCount++;
				}
				if(entity.getEnquiryStatus().equalsIgnoreCase("enrolled")) {
					enrolledCount++;
				}
				if(entity.getEnquiryStatus().equalsIgnoreCase("lost")) {
					lostCount++;
				}
			}
			dashboardRes.setEnrolledCount(enrolledCount);
			dashboardRes.setLostCount(lostCount);
			dashboardRes.setTotalEnqCount(newEnrolledCount+enrolledCount+lostCount);
		}
		*/
		
		System.out.println("EnquiryServiceImpl.getDasboardData()"+response);
		
		return response;
	}

	@Override
	public String upsertEnquiry(EnquiryForm enqFrm,Integer userId,Integer enqFrmId) {
		String status=null;
		//for insert records
		if(userId!=null) {
			//find user by id for insert records
			Optional<UserDtlsEntity> opt=userRepo.findById(userId);
			
			
			if(opt.isPresent()) {
				UserDtlsEntity userEntity=opt.get();
				//create Entity class object
				StudentEnqEntity entity=new StudentEnqEntity();
				//copy form data to entity class objct
				BeanUtils.copyProperties(enqFrm, entity);
	            //set userEntity
				entity.setUser(userEntity);
				StudentEnqEntity data=studentEnqRepo.save(entity);
				System.out.println("EnquiryServiceImpl.upsertEnquiry()"+data);
				status= "Enquiry Added";
			}
			else {
				status="Problem Occured";
			}
		}//if
		
		//for edit or update records
		if(enqFrmId!=null) {
			//find user by id for insert records
			Optional<StudentEnqEntity> opt = studentEnqRepo.findById(enqFrmId);
			if(opt.isPresent()) {
				StudentEnqEntity studentEnqEntity = opt.get();
				//copy updated data to entity class object
				BeanUtils.copyProperties(enqFrm, studentEnqEntity);
				//update user entity
				System.out.println("Before saving the entiyt:: "+studentEnqEntity);
				studentEnqRepo.save(studentEnqEntity);
				System.out.println("After saving the entiyt:: "+studentEnqEntity);
                status="Enquiry Update";
				
			}
		}
		return status;
	}

	@Override
	public List<StudentEnqEntity> getAllStudentEnquires(Integer id) {
		Optional<UserDtlsEntity> opt = userRepo.findById(id);
		if(opt.isPresent()) {
			UserDtlsEntity userDtlsEntity = opt.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			return enquiries;
		}
		//List<StudentEnqEntity> list=studentEnqRepo.getRecordBasedOnUserId(id);
		return null;
	}


	@Override
	public List<String> getCourseName() {
		List<CourseEntity> list=courseRepo.findAll();
		List<String> data=new ArrayList<String>();
		for(CourseEntity entity : list) {
			data.add(entity.getCourseName());
		}
		System.out.println("EnquiryServiceImpl.getCourseName()");
		data.forEach(System.out::println);
		return data;
	}

	@Override
	public List<String> getEnqStatus() {
		List<EnqStatusEntity> list= statusRepo.findAll();
		List<String> data=new ArrayList<String>();
		for(EnqStatusEntity entity : list) {
			data.add(entity.getStatusName());
		}
		System.out.println("EnquiryServiceImpl.getEnqStatus()");
		data.forEach(System.out::println);
		return data;
	}

	@Override
	public EnquiryForm getOneEnquires(Integer id) {
		EnquiryForm enqFrm =new EnquiryForm();
		Optional<StudentEnqEntity> opt = studentEnqRepo.findById(id);
		if(opt.isPresent()) {
			StudentEnqEntity studentEnqEntity = opt.get();
			BeanUtils.copyProperties(studentEnqEntity, enqFrm);
		}
		return enqFrm;
	}

	@Override
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria search, Integer userId) {
		Optional<UserDtlsEntity> opt = userRepo.findById(userId);
		if(opt.isPresent()) {
			UserDtlsEntity userDtlsEntity = opt.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			//filter logic
			if(search.getCourseName()!=null && !"".equals(search.getCourseName())) {
				
				enquiries = enquiries.stream()
				                     .filter(e -> e.getCourseName().equals(search.getCourseName()))
				                     .collect(Collectors.toList());
			}
			if(search.getEnquiryStatus()!=null && !"".equals(search.getEnquiryStatus())) {
				enquiries = enquiries.stream()
						             .filter(e -> e.getEnquiryStatus().equals(search.getEnquiryStatus()))
						             .collect(Collectors.toList());
			}
			if(search.getClassMode()!=null && !"".equals(search.getClassMode())) {
				enquiries = enquiries.stream()
						             .filter(e -> e.getClassMode().equals(search.getClassMode()))
						             .collect(Collectors.toList());
			}
			return enquiries;
		}
		return null;
	}

	
}
