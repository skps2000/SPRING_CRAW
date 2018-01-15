package com.project.postgres.data.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class Employees {
	
	String first_name;
	String last_name;
	
	public String toString(){
		return first_name + " " + last_name;
	}
	
}
