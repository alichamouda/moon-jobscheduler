
package com.moon.jobscheduler._moonquartz.services;

import java.util.Optional;

import com.moon.jobscheduler._moonquartz.dtos.JobDescriptor;

/**
 * An object that defines the contract for defining dynamic jobs
 * 
 * @author Julius Krah
 * @since September 2017
 */
public interface JobService {

	/**
	 * Create and schedule a job by abstracting the Job and Triggers in the
	 * JobDescriptor. You must specify and name and group for this job that
	 * uniquely identifies the job. If you specify a name/group pair that is not
	 * unique, the scheduler will silently ignore the job
	 * 
	 * @param group
	 *            the group a job belongs to
	 * @param descriptor
	 *            the payload containing the Job and its associated Trigger(s).
	 *            The name and group uniquely identifies the job.
	 * @return JobDescriptor this contains the JobDetail and Triggers of the
	 *         newly created job
	 */
	JobDescriptor createJob(String group, JobDescriptor descriptor);

	/**
	 * Searches for a Job identified by the given {@code JobKey}
	 * 
	 * @param group
	 *            the group a job belongs to
	 * @param name
	 *            the name of the dynamically scheduled job
	 * @return the jobDescriptor if found or an empty Optional
	 */
	Optional<JobDescriptor> findJob(String group, String name);

	/**
	 * Updates the Job that matches the given {@code JobKey} with new
	 * information
	 * 
	 * @param group
	 *            the group a job belongs to
	 * @param name
	 *            the name of the dynamically scheduled job
	 * @param descriptor
	 *            the payload containing the updates to the JobDetail
	 */
	void updateJob(String group, String name, JobDescriptor descriptor);

	/**
	 * Deletes the Job identified by the given {@code JobKey}
	 * 
	 * @param group
	 *            the group a job belongs to
	 * @param name
	 *            the name of the dynamically scheduled job
	 */
	void deleteJob(String group, String name);

	/**
	 * Pauses the Job identified by the given {@code JobKey}
	 * 
	 * @param group
	 *            the group a job belongs to
	 * @param name
	 *            the name of the dynamically scheduled job
	 */
	void pauseJob(String group, String name);

	/**
	 * Resumes the Job identified by the given {@code JobKey}
	 * 
	 * @param group
	 *            the group a job belongs to
	 * @param name
	 *            the name of the dynamically scheduled job
	 */
	void resumeJob(String group, String name);
}
