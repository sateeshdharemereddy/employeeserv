package com.paypal.bfs.test.employeeserv.exception;

public class EmployeeException extends RuntimeException {
	private static final long serialVersionUID = -8791237861140256052L;

	public EmployeeException(Exception ex) {
		super(ex);
	}

}
