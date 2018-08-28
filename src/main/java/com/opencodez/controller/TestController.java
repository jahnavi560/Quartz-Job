package com.opencodez.controller;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencodez.configuration.ConfigureQuartz;
import com.opencodez.quartz.jobs.JobWithCronTrigger;

@RestController
public class TestController {

@Value("${cron.frequency.jobwithcrontrigger}")
String time;
	
	@Autowired
	private SchedulerFactoryBean schedFactory;

	
	
	@RequestMapping("/schedulecronJob")
	public String scheduleCronJob() {
		String scheduled = "Job is Scheduled!!";
		try {
			JobDetailFactoryBean jdfb = ConfigureQuartz.createJobDetail(JobWithCronTrigger.class);
			jdfb.setBeanName("cronJobBean");
			jdfb.afterPropertiesSet();
			
			CronTriggerFactoryBean stfb = ConfigureQuartz.createCronTrigger(jdfb.getObject(),time);
			stfb.setBeanName("cronJobBeanTrigger");
			stfb.afterPropertiesSet();
			
			schedFactory.getScheduler().scheduleJob(jdfb.getObject(), stfb.getObject());
			
		} catch (Exception e) {
			scheduled = "Could not schedule a job. " + e.getMessage();
		}
		return scheduled;
	}
	
	@RequestMapping("/unschedulecronJob")
	public String unscheduleCron() {
		String scheduled = "Job is Unscheduled!!";
		TriggerKey tkey = new TriggerKey("cronJobBeanTrigger");
		JobKey jkey = new JobKey("cronJobBean"); 
		try {
			schedFactory.getScheduler().unscheduleJob(tkey);
			schedFactory.getScheduler().deleteJob(jkey);
		} catch (SchedulerException e) {
			scheduled = "Error while unscheduling " + e.getMessage();
		}
		return scheduled;
	}
	
}