package com.base.app.model;

import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvBindByPosition;

@Component
public class ClientDetails {

	@CsvBindByPosition(position = 0)
	private int id;
	
	@CsvBindByPosition(position = 1)
	private String name;
	
	@CsvBindByPosition(position = 2)
	private String email;
	
	@CsvBindByPosition(position = 3)
	private String address;
	
	@CsvBindByPosition(position = 4)
	private String company;
	
	@CsvBindByPosition(position = 5)
	private double salary;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "ClientDetails [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", company="
				+ company + ", salary=" + salary + "]";
	}
	
	
}
