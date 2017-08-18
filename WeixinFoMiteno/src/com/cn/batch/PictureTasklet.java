package com.cn.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

public class PictureTasklet implements Tasklet {

	/**
	 * 依赖注入获取命令和参数行
	 */
	private String parameterLine;
	private String command;
	private String pictureOutputPath;

	@Override
	public RepeatStatus execute(StepContribution setpContribution,
			ChunkContext chunkContext) throws Exception {

		/**
		 * 处理commandLine参数
		 */
		int captureTimes = Integer.parseInt(CommandLineHandler
				.getVarFromExecutionContext(chunkContext, "captureTimes"));
		String commandLine = CommandLineHandler.commandLineBuild(chunkContext,
				command, parameterLine);
		String username = CommandLineHandler.getVarFromExecutionContext(
				chunkContext, "username");
		commandLine = commandLine.replaceAll(":username:", username);
		for (int i = 0; i < captureTimes; ++i) {
			String currentCommandLine = commandLine.replaceAll(":i:",
					String.valueOf(i));
			currentCommandLine = currentCommandLine.replaceAll(
					":pictureOutputPath:", pictureOutputPath);
			CommandLineHandler.putCommandLineToExecutionContext(chunkContext,
					currentCommandLine);
		}
		return RepeatStatus.FINISHED;
	}

	public String getCommand() {
		return command;
	}

	public String getParameterLine() {
		return parameterLine;
	}

	public String getPictureOutputPath() {
		return pictureOutputPath;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setParameterLine(String parameterLine) {
		this.parameterLine = parameterLine;
	}

	public void setPictureOutputPath(String pictureOutputPath) {
		this.pictureOutputPath = pictureOutputPath;
	}

}
