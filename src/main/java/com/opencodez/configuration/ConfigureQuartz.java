
package com.opencodez.configuration;

import java.io.IOException;
import java.util.Properties;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerKey;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import com.opencodez.quartz.jobs.JobWithCronTrigger;

@Configuration
public class ConfigureQuartz {

	
	@Autowired
	SchedulerFactoryBean schedFactory;
	@Value("${cron.frequency.jobwithcrontrigger}")
	private String frequency ;

	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		System.out.println("JobFactory");
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}
	
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws IOException {
		System.out.println("schedulerFactoryBean");
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setOverwriteExistingJobs(true);
		factory.setAutoStartup(true);
		factory.setDataSource(dataSource);
		factory.setJobFactory(jobFactory);
		factory.setQuartzProperties(quartzProperties());
		
		return factory;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		System.out.println("quartzProperties");
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}
	
	
	// Use this method for creating cron triggers instead of simple triggers:
		public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
			System.out.println("createCronTrigger");
			CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
			factoryBean.setJobDetail(jobDetail);
			factoryBean.setCronExpression(cronExpression);
			factoryBean.setMisfireInstruction(SimpleTrigger.REPEAT_INDEFINITELY);
			return factoryBean;
		}

		public static JobDetailFactoryBean createJobDetail(Class jobClass) {
			System.out.println("createJobDetail");
			JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
			factoryBean.setJobClass(jobClass);
			// job has to be durable to be stored in DB:
			factoryBean.setDurability(true);
			return factoryBean;
		}
	@Bean
	public String scheduleCronJob(SchedulerFactoryBean schedFactory) {
		System.out.println("scheduleCronJob");
		String scheduled = "------------------Job is Scheduled!!------------------"+frequency;
		try {
			JobDetailFactoryBean jdfb = ConfigureQuartz.createJobDetail(JobWithCronTrigger.class);
			jdfb.setBeanName("cronJobBean");
			jdfb.afterPropertiesSet();
			
			CronTriggerFactoryBean stfb = ConfigureQuartz.createCronTrigger(jdfb.getObject(),frequency);
			stfb.setBeanName("cronJobBeanTrigger");
			stfb.afterPropertiesSet();
			
			schedFactory.getScheduler().scheduleJob(jdfb.getObject(), stfb.getObject());
			
		} catch (Exception e) {
			scheduled = "--------------------Could not schedule a job. " + e.getMessage();
		}
		System.out.println(scheduled);
		return scheduled;
	}

	
	@PreDestroy
	public void unscheduleCron() {
		String scheduled = "--------------------Job is Unscheduled!!----------------";
		TriggerKey tkey = new TriggerKey("cronJobBeanTrigger");
		JobKey jkey = new JobKey("cronJobBean"); 
		try {
			schedFactory.getScheduler().unscheduleJob(tkey);
			schedFactory.getScheduler().deleteJob(jkey);
		} catch (SchedulerException e) {
			scheduled = "------------Error while unscheduling " + e.getMessage();
		}
		System.out.println(scheduled);
	}
}
