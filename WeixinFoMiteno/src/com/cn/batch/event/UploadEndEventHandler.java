package com.cn.batch.event;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

/**
 * 处理ftp上传文件完成后，系统发送到消息
 * 
 * @author admin
 * 
 */
public class UploadEndEventHandler {

	private JobLauncher jobLauncher;

	private Job job;

	public Job getJob() {
		return job;
	}

	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	public Object handler(UploadEndEvent event)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Map<?, ?> source = (Map<?, ?>) event.getSource();
		String username=(String) source.get("username");
		String input = (String) source.get("input");
		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addString("username", username);
		builder.addString("input", input);
		jobLauncher.run(job, builder.toJobParameters());
		return source;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

}
