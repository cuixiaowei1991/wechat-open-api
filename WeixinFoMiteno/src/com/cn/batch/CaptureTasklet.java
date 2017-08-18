package com.cn.batch;

import java.text.DecimalFormat;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

public class CaptureTasklet implements Tasklet {

	/**
	 * 依赖注入获取命令和参数行
	 */
	private String parameterLine;
	private String command;
	private int captureTimes;

	@Override
	public RepeatStatus execute(StepContribution setpContribution,
			ChunkContext chunkContext) throws Exception {
		String lengthString = CommandLineHandler.getVarFromExecutionContext(
				chunkContext, "ID_LENGTH");
		double length = Double.parseDouble(lengthString);
		CommandLineHandler.putVarToExecution(chunkContext, "captureTimes",
				String.valueOf(captureTimes));
		String commandLine = CommandLineHandler.commandLineBuild(chunkContext,
				command, parameterLine);
		for (int i = 0; i < captureTimes; ++i) {
			String currentCommandLine = commandLine.replaceAll(":i:",
					String.valueOf(i));
			double with = i * length / captureTimes;
			currentCommandLine = currentCommandLine.replaceAll(":time:",
					round_half_up(with));
			CommandLineHandler.putCommandLineToExecutionContext(chunkContext,
					currentCommandLine);
		}

		return RepeatStatus.FINISHED;
	}

	/**
	 * 四舍五入函数
	 * 
	 * @param input
	 * @return
	 */
	public static String round_half_up(double input) {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		String output = decimalFormat.format(input);
		return output;
	}

	public int getCaptureTimes() {
		return captureTimes;
	}

	public String getCommand() {
		return command;
	}

	public String getParameterLine() {
		return parameterLine;
	}

	public void setCaptureTimes(int captureTimes) {
		this.captureTimes = captureTimes;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setParameterLine(String parameterLine) {
		this.parameterLine = parameterLine;
	}

}
