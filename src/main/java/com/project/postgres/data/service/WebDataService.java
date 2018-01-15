package com.project.postgres.data.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebDataService {
	
	final Logger logger = LogManager.getLogger(WebDataService.class);
	
	@Autowired
	SqlSession sqlSession;
		
	public List<HashMap<String,Object>> now(HashMap<String,Object> pMap){
		return sqlSession.selectList("EmployeesMapper.now", pMap);
	}
	
	public List<HashMap<String,Object>> list(HashMap<String,Object> pMap){
		return sqlSession.selectList("EmployeesMapper.list", pMap);
	}

}
