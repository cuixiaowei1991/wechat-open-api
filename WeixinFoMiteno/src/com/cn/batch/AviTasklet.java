package com.cn.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

public class AviTasklet implements Tasklet { 

	/**
	 * ����ע���ȡ����Ͳ�����
	 */
	private String parameterLine;
	private String command;

	@Override
	public RepeatStatus execute(StepContribution setpContribution,
			ChunkContext chunkContext) throws Exception {

		/**
		 * ����commandLine����
		 */
		String commandLine = CommandLineHandler.commandLineBuild(chunkContext,
				command, parameterLine);
		CommandLineHandler.putCommandLineToExecutionContext(chunkContext, commandLine);
		return RepeatStatus.FINISHED;
	}

	public String getCommand() {
		return command;
	}

	public String getParameterLine() {
		return parameterLine;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setParameterLine(String parameterLine) {
		this.parameterLine = parameterLine;
	}

}
