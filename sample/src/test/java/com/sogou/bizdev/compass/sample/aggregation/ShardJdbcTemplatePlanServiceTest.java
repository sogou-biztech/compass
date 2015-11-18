package com.sogou.bizdev.compass.sample.aggregation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.sogou.bizdev.compass.aggregation.exception.SqlExecutionFailureException;
import com.sogou.bizdev.compass.sample.common.po.Plan;
import com.sogou.bizdev.compass.sample.common.po.PlanCount;

@ContextConfiguration(locations = { 
		"classpath*:/conf/aggregation/test-aggregation-shardjdbctemplate.xml",
		"classpath*:test-shard-*.xml" 
	})
@RunWith(BlockJUnit4ClassRunner.class)
public class ShardJdbcTemplatePlanServiceTest  {
	
	private ShardJdbcTemplatePlanService service;
	
	@Before
	public void setUp() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
			"classpath*:/conf/aggregation/test-aggregation-shardjdbctemplate.xml",
			"classpath*:test-shard-*.xml" 
		});
		
		service = (ShardJdbcTemplatePlanService) ctx.getBean("planService");
	}
	
	/**
	 * 在test-aggregation-shardjdbctemplate.xml中指定了ShardJdbcTemplate的maxRows为1000
	 * 此处结果集超过了这个数目，因此应当抛出SqlExecutionFailureException
	 */
	@Test(expected = SqlExecutionFailureException.class)
	public void testListAllPlan() {
		List<Plan> allPlanList = service.listAllPlan();
		
		for (Plan plan : allPlanList) {
			System.out.println(plan);
		}
	}
	
	@Test
	public void testListPlan() {
		int pageCount = 2;
		int pageSize = 200;
		
		List<Plan> pagedPlanList = service.listPlan(pageCount, pageSize);
		
		for (Plan plan : pagedPlanList) {
			System.out.println(plan);
		}
		
	}
	
	@Test
	public void testQueryPlansByPlanId() {
		Long cpcplanId = 1773L;
		Plan plan = service.queryPlanByPlanId(cpcplanId);
		
		System.out.println(plan);
	}
	
	@Test
	public void testQueryPlansByAccountIds() {
		List<Plan> planList = service.queryPlansByAccountIds(this.getAccountIdsAsList());
		
		for (Plan plan : planList) {
			System.out.println(plan);
		}
	}
	
	@Test
	public void testQueryPlansByAccountIdsAndTime() {
		List<Long> accountIds = this.getAccountIdsAsList();
		String startTime = "2012-07-01";
		String endTime = "2012-09-01";
		
		List<Plan> planList = service.queryPlansByAccountIdsAndTime(accountIds, startTime, endTime);
		
		for (Plan plan : planList) {
			System.out.println(plan);
		}
	}
	
	@Test
	public void testCountPlanOfAccounts() {
		List<PlanCount> planCountList = service.countPlanOfAccounts(this.getAccountIdsAsList());
		
		for (PlanCount planCount : planCountList) {
			System.out.println("accountid=" + planCount.getAccountId() + " has cpcplanidCount=" + planCount.getPlanCount());
		}
	}
	
	@Test
	public void testCountAllPlanOfAccounts() {
		List<PlanCount> planCountList = service.countAllPlanOfAccounts();
		
		for (PlanCount planCount : planCountList) {
			System.out.println("accountid=" + planCount.getAccountId() + " has cpcplanidCount=" + planCount.getPlanCount());
		}
	}
	
	@Test
	public void testUpdatePlan() {
		Long cpcplanId = 1773L;

		Plan plan = service.queryPlanByPlanId(cpcplanId);
		plan.setEndDate(new Date(System.currentTimeMillis()));
		
		service.updatePlan(plan);
	}
	
	@Test
	public void testUpdatePlans() {
		List<Plan> planList = service.queryPlansByAccountIds(this.getAccountIdsAsList());
		
		for (Plan plan : planList) {
			plan.setEndDate(new Date(System.currentTimeMillis()));
		}
		
		service.updatePlans(planList);
	}
	
	private Long[] getAccountIds() {
		Long[] args = new Long[] { 291392L, 300032L, 322688L, 331456L, 293640L, 298440L, 306888L, 312648L, 313608L,
				323336L, 344776L, 351816L, 355720L, 421705L, 423497L, 425033L, 426377L, 316561L, 319185L, 336465L,
				343953L, 345489L, 352209L, 353105L, 356369L, 357969L, 364817L };

		return args;
	}
	
	private List<Long> getAccountIdsAsList() {
		return Arrays.asList(this.getAccountIds());
	}

}
