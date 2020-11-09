package com.paypal.bfs.test.employeeserv.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.EmployeeException;
import com.paypal.bfs.test.employeeserv.model.AddressEntity;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	private ModelMapper modelMapper;

	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
		modelMapper = new ModelMapper();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer saveEmployee(Employee employee) {
		try {
			Integer count = employeeRepository.countByFirstNameAndLastName(employee.getFirstName(),
					employee.getLastName());
			Integer employeeId = -1;
			if (count == 0) {
				EmployeeEntity employeeEntity = modelMapper.map(employee, EmployeeEntity.class);
				AddressEntity addressEntity = modelMapper.map(employee.getAddress(), AddressEntity.class);
				employeeEntity.setAddressEntity(addressEntity);
				employeeEntity = employeeRepository.save(employeeEntity);
				employeeId = employeeEntity.getId();
				log.info("Employee with Id: {} has been successfully saved.", employeeId);
			}
			return employeeId;
		} catch (Exception e) {
			throw new EmployeeException(e);
		}
	}

	@Override
	public Optional<Employee> findEmployeeById(Integer id) {
		try {
			Optional<EmployeeEntity> employee = employeeRepository.findById(id);
			if (employee.isPresent()) {
				log.debug("Employee Exist with Id: {}", id);
				Employee employeeResp = modelMapper.map(employee.get(), Employee.class);
				Address address = modelMapper.map(employee.get().getAddressEntity(), Address.class);
				employeeResp.setAddress(address);
				return Optional.of(employeeResp);
			} else {
				log.warn("No Employee Exist with id: {}", id);
			}
			return Optional.empty();
		} catch (Exception e) {
			throw new EmployeeException(e);
		}
	}

}
