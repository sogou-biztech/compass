package com.sogou.bizdev.compass.sample.common.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AccountExample {
    protected String orderByClause;
    
    protected boolean distinct;
    
    protected List<Criteria> oredCriteria;
 
    public AccountExample() {
        oredCriteria = new ArrayList<Criteria>();
    }
 
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }
 
    public String getOrderByClause() {
        return orderByClause;
    }
 
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
 
    public boolean isDistinct() {
        return distinct;
    }
 
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }
 
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }
 
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }
 
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }
 
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }
 
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andAccountIdIsNull() {
            addCriterion("ACCOUNTID is null");
            return (Criteria) this;
        }

        public Criteria andAccountIdIsNotNull() {
            addCriterion("ACCOUNTID is not null");
            return (Criteria) this;
        }

        public Criteria andAccountIdEqualTo(Long value) {
            addCriterion("ACCOUNTID =", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdNotEqualTo(Long value) {
            addCriterion("ACCOUNTID <>", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdGreaterThan(Long value) {
            addCriterion("ACCOUNTID >", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ACCOUNTID >=", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdLessThan(Long value) {
            addCriterion("ACCOUNTID <", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdLessThanOrEqualTo(Long value) {
            addCriterion("ACCOUNTID <=", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdIn(List<Long> values) {
            addCriterion("ACCOUNTID in", values, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdNotIn(List<Long> values) {
            addCriterion("ACCOUNTID not in", values, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdBetween(Long value1, Long value2) {
            addCriterion("ACCOUNTID between", value1, value2, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdNotBetween(Long value1, Long value2) {
            addCriterion("ACCOUNTID not between", value1, value2, "accountId");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("EMAIL is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("EMAIL is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("EMAIL =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("EMAIL <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("EMAIL >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("EMAIL >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("EMAIL <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("EMAIL <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("EMAIL like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("EMAIL not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("EMAIL in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("EMAIL not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("EMAIL between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("EMAIL not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("PASSWORD is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("PASSWORD is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("PASSWORD =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("PASSWORD <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("PASSWORD >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("PASSWORD >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("PASSWORD <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("PASSWORD <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("PASSWORD like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("PASSWORD not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("PASSWORD in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("PASSWORD not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("PASSWORD between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("PASSWORD not between", value1, value2, "password");
            return (Criteria) this;
        }
        
        public Criteria andRegistDateIsNull() {
            addCriterion("REGISTDATE is null");
            return (Criteria) this;
        }

        public Criteria andRegistDateIsNotNull() {
            addCriterion("REGISTDATE is not null");
            return (Criteria) this;
        }

        public Criteria andRegistDateEqualTo(Date value) {
            addCriterionForJDBCDate("REGISTDATE =", value, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("REGISTDATE <>", value, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateGreaterThan(Date value) {
            addCriterionForJDBCDate("REGISTDATE >", value, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("REGISTDATE >=", value, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateLessThan(Date value) {
            addCriterionForJDBCDate("REGISTDATE <", value, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("REGISTDATE <=", value, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateIn(List<Date> values) {
            addCriterionForJDBCDate("REGISTDATE in", values, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("REGISTDATE not in", values, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("REGISTDATE between", value1, value2, "registDate");
            return (Criteria) this;
        }

        public Criteria andRegistDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("REGISTDATE not between", value1, value2, "registDate");
            return (Criteria) this;
        }
    }
 
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
 
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}