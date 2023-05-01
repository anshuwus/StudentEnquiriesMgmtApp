package com.ashokit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashokit.entity.CourseEntity;

public interface ICourseRepo extends JpaRepository<CourseEntity, Integer> {

}
