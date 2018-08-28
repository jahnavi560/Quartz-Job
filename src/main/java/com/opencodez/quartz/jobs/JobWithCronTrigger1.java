
package com.opencodez.quartz.jobs;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.stereotype.Component;

import com.opencodez.configuration.ConfigureQuartz;
import com.opencodez.util.AppLogger;

@Component
@DisallowConcurrentExecution
public class JobWithCronTrigger1 implements Job {

	private final static AppLogger logger = AppLogger.getInstance();

	@Value("${cron.frequency.jobwithcrontrigger}")
	private String frequency;
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		logger.info("Running JobWithCronTrigger1------------- | frequency {}", frequency);
	}

	@Bean(name = "jobWithCronTriggerBean1")
	public JobDetail sampleJob() {
		return ConfigureQuartz.createJobDetail(this.getClass()).getObject();
	}	

	@Bean(name = "jobWithCronTriggerBeanTrigger1")
	public CronTriggerFactoryBean sampleJobTrigger(@Qualifier("jobWithCronTriggerBean1") JobDetail jobDetail) {
		return ConfigureQuartz.createCronTrigger(jobDetail, frequency);
	}
}
