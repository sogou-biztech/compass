package com.sogou.bizdev.compass.sample.jdbctemplate.sequence;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class SequenceGeneratorImpl implements SequenceGenerator {
	private final Logger log = Logger.getLogger(SequenceGeneratorImpl.class);
	protected JdbcTemplate jdbcTemplate;

    //@Resource(name = "jdbcTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    
	@Override
	public Long getSequence(String sequence) {
		String sql="select "+sequence.toLowerCase()+".nextval from dual";
		Long nextVal=jdbcTemplate.queryForLong(sql);
		if(log.isInfoEnabled()){
			log.info("sequence="+sequence+",|nextval="+nextVal);
		}
		return nextVal;
	}

}
