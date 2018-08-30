
package com.opencodez.quartz.jobs;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class JobWithCronTrigger implements Job {
	
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		System.out.println("Job is Executed---------");
		
	}
	
	
}
