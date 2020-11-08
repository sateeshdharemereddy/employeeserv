package com.paypal.bfs.test.employeeserv.service;

import java.util.Optional;

import com.paypal.bfs.test.employeeserv.api.model.Employee;

public interface EmployeeService {
	
	/**
	 * saveEmployee method helps to create an employee with valid employee data 
	 * @param employee
	 * @return
	 */
	public Integer saveEmployee(Employee employee);
	
	/**
	 * This findEmployeeById method fetch the employee associated with id.
	 * @param id
	 * @return
	 */
	public Optional<Employee> findEmployeeById(Integer id);

}
