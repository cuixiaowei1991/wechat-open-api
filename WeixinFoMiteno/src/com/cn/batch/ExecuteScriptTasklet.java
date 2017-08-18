package com.cn.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

public class ExecuteScriptTasklet implements Tasklet {
	private Logger logger = Logger.getLogger(ExecuteScriptTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution stepContribution,
			ChunkContext chunkContext) throws Exception {

		String scriptFileFullPath = CommandLineHandler
				.getVarFromExecutionContext(chunkContext, "scriptFileFullPath");

		/**
		 * ִ���������ļ�
		 */
		Runtime runtime = Runtime.getRuntime();
		Process encoderProcess = runtime.exec(scriptFileFullPath);
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				encoderProcess.getErrorStream()));
		/**
		 * �����������߳������encoderProcess.getErrorStream�Ļ�����
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (br.readLine() != null) {
					}
				} catch (IOException ex) {
					logger.error(ex);
				}
			}
		}).start();
		/**
		 * ��encoderProcess.getInputStream()��ñ�����Ϣ
		 */
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(encoderProcess.getInputStream()), 1024);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			/**
			 * ��ʾ������Ϣ
			 */
			logger.debug(line);
		}
		br.close();
		try {
			logger.debug("encoderProcess return " + encoderProcess.waitFor());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		encoderProcess.destroy();

		return RepeatStatus.FINISHED;
	}

}
