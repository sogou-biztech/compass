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
		String sql = "select i_account_id, c_email from account where i_account_id=?";
		List<Account> accounts = getJdbcTemplate().query(sql, new Object[]{accountId}, new RowMapper(){

			@Override
			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
				Account account = new Account();
				account.setiAccountId(rs.getLong(1));
				account.setcEmail(rs.getString(2));
				return account;
			}
			
		});
		return CollectionUtils.isEmpty(accounts) ? null : accounts.get(0);
	}

}
