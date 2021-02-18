package com.phonebook.exception;

public class PermissaoException extends Exception {
	
	private static final long serialVersionUID = -7745447652369049559L;

	public PermissaoException() {}
	
	public PermissaoException(String mensagem) { super(mensagem); }
}
