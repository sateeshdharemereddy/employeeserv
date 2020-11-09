package com.paypal.bfs.test.employeeserv.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.EmployeeException;
import com.paypal.bfs.test.employeeserv.model.AddressEntity;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;

public class EmployeeServiceTest {

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	@Mock
	private EmployeeRepository employeeRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSaveEmployeeWhenNewData() {
		// given
		Employee employee = mockEmployee();

		// when
		when(employeeRepository.countByFirstNameAndLastName(anyString(), anyString())).thenReturn(0);

		EmployeeEntity empEntity = new EmployeeEntity();
		empEntity.setId(1);
		when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(empEntity);

		// Then
		int newEmployeeId = employeeService.saveEmployee(employee);
		assertEquals(1, newEmployeeId);
		verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
	}

	// Check Idempotence
	@Test
	public void testSaveEmployeeWhenExistingData() {

		// given
		Employee employee = mockEmployee();

		// when
		when(employeeRepository.countByFirstNameAndLastName(anyString(), anyString())).thenReturn(1);

		EmployeeEntity empEntity = new EmployeeEntity();
		empEntity.setId(1);
		when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(empEntity);

		// Then
		int newEmployeeId = employeeService.saveEmployee(employee);
		assertEquals(-1, newEmployeeId);
		verify(employeeRepository, times(0)).save(any(EmployeeEntity.class));

	}

	@Test(expected = EmployeeException.class)
	public void testSaveEmployeeWhenException() {

		// given
		Employee employee = mockEmployee();

		// when
		when(employeeRepository.countByFirstNameAndLastName(anyString(), anyString()))
				.thenThrow(new IllegalStateException("testing"));

		// Then
		employeeService.saveEmployee(employee);
	}

	@Test
	public void testFindEmployeeByIdWithFoundCase() {
		// Given
		Integer empId = 1;

		// When
		when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mockEmployeeEntity()));

		// Then
		Optional<Employee> employee = employeeService.findEmployeeById(empId);
		assertTrue(employee.isPresent());
		assertNotNull(employee.get());
		verify(employeeRepository, times(1)).findById(Mockito.anyInt());
	}

	@Test
	public void testFindEmployeeByIdWithNotFoundCase() {
		// Given
		Integer empId = 1;

		// When
		when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

		// Then
		Optional<Employee> employee = employeeService.findEmployeeById(empId);
		assertFalse(employee.isPresent());
		verify(employeeRepository, times(1)).findById(Mockito.anyInt());
	}

	@Test(expected = EmployeeException.class)
	public void testFindEmployeeByIdWhenException() {
		// Given
		Integer empId = 1;

		// When
		when(employeeRepository.findById(Mockito.anyInt())).thenThrow(new IllegalStateException("testing..."));

		// Then
		employeeService.findEmployeeById(empId);
		verify(employeeRepository, times(1)).findById(Mockito.anyInt());
	}

	private EmployeeEntity mockEmployeeEntity() {
		ModelMapper modelMapper = new ModelMapper();
		Employee employee = mockEmployee();
		EmployeeEntity employeeEntity = modelMapper.map(employee, EmployeeEntity.class);
		AddressEntity addressEntity = modelMapper.map(employee.getAddress(), AddressEntity.class);
		employeeEntity.setAddressEntity(addressEntity);
		return employeeEntity;
	}

	private Employee mockEmployee() {
		Employee employee = new Employee();
		employee.setFirstName("Sam");
		employee.setLastName("B");
		employee.setDob("03/03/1990");
		employee.setAdditionalProperty("empKey", new ArrayList<>());

		Address address = new Address();
		address.setCity("abc");
		address.setCountry("USA");
		address.setLine1("line 1");
		address.setLine2("line 2 address");
		address.setState("CA");
		address.setZipCode("32423423");
		address.setAdditionalProperty("addrkey1", new ArrayList<>());
		employee.setAddress(address);
		return employee;
	}

}
