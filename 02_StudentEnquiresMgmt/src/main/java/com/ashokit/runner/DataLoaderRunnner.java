package com.ashokit.runner;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ashokit.entity.CourseEntity;
import com.ashokit.entity.EnqStatusEntity;
import com.ashokit.repository.ICourseRepo;
import com.ashokit.repository.IEnqStatusRepo;

//@Component
public class DataLoaderRunnner implements ApplicationRunner{
	@Autowired
	private ICourseRepo courseRepo;
	@Autowired
	private IEnqStatusRepo enqRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		CourseEntity c1=new CourseEntity();
		CourseEntity c2=new CourseEntity();
		CourseEntity c3=new CourseEntity();
		c1.setCourseName("Java Fullstack");
		c2.setCourseName("DevOps");
		c3.setCourseName("AWS");
		List<CourseEntity> list=Arrays.asList(c1,c2,c3);
		courseRepo.saveAll(list);
		
		EnqStatusEntity e1=new EnqStatusEntity();
		EnqStatusEntity e2=new EnqStatusEntity();
		EnqStatusEntity e3=new EnqStatusEntity();
		e1.setStatusName("New");
		e2.setStatusName("Enrolled");
		e3.setStatusName("Lost");
		List<EnqStatusEntity> list2=Arrays.asList(e1,e2,e3);
		enqRepo.saveAll(list2);
	}

}
