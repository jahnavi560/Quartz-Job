select * from qrtz_triggers;
select * from qrtz_cron_triggers;
select * from qrtz_job_details;

select * from qrtz_scheduler_state;


insert into qrtz_job_details values('schedulerFactoryBean','cronJobBean1','DEFAULT','','com.opencodez.quartz.jobs.JobWithCronTrigger1',1,1,0,0,'');

insert into qrtz_triggers values('schedulerFactoryBean','cronJobBeanTrigger1','DEFAULT','cronJobBean1','DEFAULT','',1535523840000,1535437440000,0,'WAITING','CRON','1535437424000','0','','-1','');

insert into qrtz_cron_triggers values('schedulerFactoryBean','cronJobBeanTrigger1','DEFAULT','0 15 12 * * ?','Asia/Calcutta');






update qrtz_cron_triggers set CRON_EXPRESSION='0 52 12 * * ?' where TRIGGER_NAME='cronJobBeanTrigger1';
update qrtz_triggers set NEXT_FIRE_TIME=1535440920000 where TRIGGER_NAME='cronJobBeanTrigger1';
update qrtz_triggers set PREV_FIRE_TIME=-1 where TRIGGER_NAME='cronJobBeanTrigger1';

update qrtz_triggers set CALENDAR_NAME=null where TRIGGER_NAME='cronJobBeanTrigger1';