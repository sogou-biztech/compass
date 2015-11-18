package com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import com.sogou.bizdev.compass.sample.common.po.Account;
import com.sogou.bizdev.compass.sample.jdbctemplate.masterslave.dao.AccountDao;

public class AccountDaoImpl implements AccountDao {
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Account getAccountById(Long accountId) {
		String sql = "select accountid, email,password,registdate from account where accountid=?";
		List<Account> accounts = getJdbcTemplate().query(sql, new Object[]{accountId}, new RowMapper(){

			@Override
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account account = new Account();
				account.setAccountId(rs.getLong(1));
				account.setEmail(rs.getString(2));
				account.setPassword(rs.getString(3));
				account.setRegistDate(rs.getDate(4));
				return account;
			}
			
		});
		return CollectionUtils.isEmpty(accounts) ? null : accounts.get(0);
	}

}
