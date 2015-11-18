package com.sogou.bizdev.compass.sample.common.po;

import java.io.Serializable;
import java.util.Date;

public class AccountForTest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long accountId;
	
	private String email;
	
	private String password;
	
	private Date registDate;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	@Override
	public String toString() {
		return "AccountForTest [accountId=" + accountId + ", email=" + email
				+ ", password=" + password + ", registDate=" + registDate + "]";
	}
}