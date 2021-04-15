package com.moon.jobscheduler._moonquartz.services;
import static org.quartz.JobKey.jobKey;
import java.util.Objects;
import java.util.Set;
import com.moon.jobscheduler._moonquartz.dtos.JobDescriptor;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
public class IJobService extends AbstractJobService {

	public IJobService(@Qualifier("schedulerFactory") Scheduler scheduler) {
		super(scheduler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobDescriptor createJob(String group, JobDescriptor descriptor) {
		descriptor.setGroup(group);
		JobDetail jobDetail = descriptor.buildJobDetail();
		Set<Trigger> triggersForJob = descriptor.buildTriggers();
		log.info("About to save job with key - {}", jobDetail.getKey());
		try {
			scheduler.scheduleJob(jobDetail, triggersForJob, true);
			log.info("Job with key - {} saved sucessfully", jobDetail.getKey());
		} catch (SchedulerException e) {
			log.error("Could not save job with key - {} due to error - {}", jobDetail.getKey(), e.getLocalizedMessage());
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return descriptor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJob(String group, String name, JobDescriptor descriptor) {
		try {
			JobDetail oldJobDetail = scheduler.getJobDetail(jobKey(name, group));
			if (Objects.nonNull(oldJobDetail)) {
				JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
				jobDataMap.put("randomField", descriptor.getTopicName());
				JobBuilder jb = oldJobDetail.getJobBuilder();
				JobDetail newJobDetail = jb.usingJobData(jobDataMap).storeDurably().build();
				scheduler.addJob(newJobDetail, true);
				log.info("Updated job with key - {}", newJobDetail.getKey());
				return;
			}
			log.warn("Could not find job with key - {}.{} to update", group, name);
		} catch (SchedulerException e) {
			log.error("Could not find job with key - {}.{} to update due to error - {}", group, name, e.getLocalizedMessage());
		}
	}

}
