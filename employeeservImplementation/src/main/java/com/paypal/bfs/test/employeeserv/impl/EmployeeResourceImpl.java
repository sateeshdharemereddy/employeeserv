package com.paypal.bfs.test.employeeserv.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation class for employee resource.
 */
@Slf4j
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

	private EmployeeService employeeService;

	@Autowired
	public EmployeeResourceImpl(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Override
	public ResponseEntity<Employee> employeeGetById(String id) {
		Optional<Employee> employee = employeeService.findEmployeeById(Integer.valueOf(id));
		if (employee.isPresent()) {
			return new ResponseEntity<>(employee.get(), HttpStatus.OK);
		} else {
			log.info("Failed to get the employee with Id: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@Override
	public ResponseEntity<String> createEmployee(Employee employee) {
		Integer employeeId = employeeService.saveEmployee(employee);
		log.info("Employee with Id: {} has been created...", employeeId);
		if (employeeId != -1) {
			return new ResponseEntity<>(String.format("Employee Id: %s And Name: %s has been successfully saved..",
					employeeId, employee.getFirstName()), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(String.format("Employee with Name : %s , %s is already exist.",
					employee.getFirstName(), employee.getLastName()), HttpStatus.CREATED);
		}
	}

}
