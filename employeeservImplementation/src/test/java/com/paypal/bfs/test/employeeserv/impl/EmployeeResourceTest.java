package com.paypal.bfs.test.employeeserv.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

public class EmployeeResourceTest {

	private MockMvc mockMvc;

	@Mock
	private EmployeeService employeeService;

	@InjectMocks
	private EmployeeResourceImpl employeeResource;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(employeeResource).build();
	}

	@Test
	public void testEmployeeGetById() throws Exception {
		// Given
		String employeeId = "1";

		// When
		when(employeeService.findEmployeeById(Mockito.anyInt())).thenReturn(Optional.of(mockEmployee()));

		// Then
		mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/bfs/employees/" + employeeId).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.address.line1").isNotEmpty());
	}
	
	@Test
	public void testEmployeeGetByIdWhenNotFound() throws Exception {
		// Given
		String employeeId = "1";

		// When
		when(employeeService.findEmployeeById(Mockito.anyInt())).thenReturn(Optional.empty());

		// Then
		mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/bfs/employees/" + employeeId).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void testCreateEmployee() throws Exception {

		// When
		when(employeeService.saveEmployee(any(Employee.class))).thenReturn(1);

		// Then
		mockMvc.perform(MockMvcRequestBuilders.post("/v1/bfs/employees/").content(asJsonString(mockEmployee()))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isCreated());

	}
	
	@Test
	public void testCreateEmployeeCheckIdempotency() throws Exception {

		// When
		when(employeeService.saveEmployee(any(Employee.class))).thenReturn(-1);

		// Then
		mockMvc.perform(MockMvcRequestBuilders.post("/v1/bfs/employees/").content(asJsonString(mockEmployee()))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$").isString());

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

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
