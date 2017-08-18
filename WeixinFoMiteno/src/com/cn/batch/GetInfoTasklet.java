package com.cn.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

/**
 * 获得视频文件的编码信息
 * 
 * @author admin
 * 
 */
public class GetInfoTasklet implements Tasklet {
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(GetInfoTasklet.class);

	/**
	 * 依赖注入获取命令和参数行
	 */
	private String parameterLine;
	private String command;

	/**
	 * 编码命令行
	 */
	private String commandLine;

	@Override
	public RepeatStatus execute(StepContribution setpContribution,
			ChunkContext chunkContext) throws Exception {

		/**
		 * 获取命令行
		 */
		commandLine = CommandLineHandler.commandLineBuild(chunkContext,
				command, parameterLine);

		/**
		 * 启动编码器线程
		 */
		Runtime getInfoRuntime = Runtime.getRuntime();
		Process getInfoProcess = getInfoRuntime.exec(commandLine);
		final InputStream getLengthError = getInfoProcess.getErrorStream();

		new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						getLengthError));
				try {
					while (br.ready()) {
						br.readLine();
					}
				} catch (IOException ex) {
					logger.error("ex=" + ex);
				}
			}
		}).start();
		getLengthError.close();
		BufferedReader logReader = new BufferedReader(new InputStreamReader(
				getInfoProcess.getInputStream()), 1024);
		StringBuffer bufferLog = new StringBuffer();
		String readString;
		while ((readString = logReader.readLine()) != null) {
			bufferLog.append(readString);
			bufferLog.append(";");
		}
		logReader.close();

		/**
		 * 取得视频文件信息
		 */
		String info = bufferLog.toString();
		String ID_FILENAME = StringUtils.substringBetween(info, "ID_FILENAME=",
				";");
		String ID_DEMUXER = StringUtils.substringBetween(info, "ID_DEMUXER=",
				";");
		String ID_VIDEO_FORMAT = StringUtils.substringBetween(info,
				"ID_VIDEO_FORMAT=", ";");
		String ID_VIDEO_BITRATE = StringUtils.substringBetween(info,
				"ID_VIDEO_BITRATE=", ";");
		String ID_VIDEO_WIDTH = StringUtils.substringBetween(info,
				"ID_VIDEO_WIDTH=", ";");
		String ID_VIDEO_HEIGHT = StringUtils.substringBetween(info,
				"ID_VIDEO_HEIGHT=", ";");
		String ID_VIDEO_FPS = StringUtils.substringBetween(info,
				"ID_VIDEO_FPS=", ";");
		String ID_VIDEO_ASPECT = StringUtils.substringBetween(info,
				"ID_VIDEO_ASPECT=", ";");
		String ID_LENGTH = StringUtils
				.substringBetween(info, "ID_LENGTH=", ";");
		
		/**
		 * 编码开始时间
		 */
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String startTime = format.format(currentDate);

		/**
		 * 获取并处理ExecutionContext变量
		 */
		CommandLineHandler.putVarToExecution(chunkContext, "ID_FILENAME",
				ID_FILENAME.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "ID_DEMUXER",
				ID_DEMUXER.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "ID_VIDEO_FORMAT",
				ID_VIDEO_FORMAT.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "ID_VIDEO_BITRATE",
				ID_VIDEO_BITRATE.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "ID_VIDEO_WIDTH",
				ID_VIDEO_WIDTH.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "ID_VIDEO_HEIGHT",
				ID_VIDEO_HEIGHT.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "ID_VIDEO_FPS",
				ID_VIDEO_FPS.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "ID_VIDEO_ASPECT",
				ID_VIDEO_ASPECT.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "ID_LENGTH",
				ID_LENGTH.trim());
		CommandLineHandler.putVarToExecution(chunkContext, "startTime",
				startTime);
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
