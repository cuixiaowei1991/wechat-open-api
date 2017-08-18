package com.cn.batch.utils;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;

/**
 * 参数检查和命令编译
 * 
 * @author admin
 * 
 */
public class CommandLineHandler {
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(CommandLineHandler.class);

	/**
	 * 静态变量
	 */
	public final static boolean CHECK_FAILURE = false;
	public final static boolean CHECK_SUCCESS = true;

	/**
	 * 输入路径检查
	 * 
	 * @param input
	 * @return
	 */
	public static boolean checkInput(String input) {
		if (input == null || input.equals("")
				|| (new File(input)).isFile() != true) {
			logger.error("The outputPath parameter (" + input
					+ ") is not to be set currently!");
			return CommandLineHandler.CHECK_FAILURE;
		} else {
			return CommandLineHandler.CHECK_SUCCESS;
		}
	}

	/**
	 * 输出路径检查
	 * 
	 * @param outputPath
	 * @return
	 */
	public static boolean checkOutputPath(String outputPath) {
		if (outputPath == null || outputPath.equals("")
				|| (new File(outputPath)).isDirectory() != true) {
			logger.error("The outputPath parameter (" + outputPath
					+ ") is not to be set currently!");
			return CommandLineHandler.CHECK_FAILURE;
		} else {
			return CommandLineHandler.CHECK_SUCCESS;
		}
	}

	/**
	 * 构建编码命令行
	 * 
	 * @param chunkContext
	 * @param command
	 * @param parameterLine
	 * @return
	 */
	public static String commandLineBuild(ChunkContext chunkContext,
			String command, String parameterLine) {

		ExecutionContext executionContext = chunkContext.getStepContext()
				.getStepExecution().getJobExecution().getExecutionContext();

		/**
		 * 从ExecutionContext中获取变量
		 */
		String username = executionContext.getString("username");
		String input = executionContext.getString("input");
		String outputPath = executionContext.getString("outputPath");
		String inputFileMainName = executionContext
				.getString("inputFileMainName");
		String inputExtension = executionContext.getString("inputExtension");
		String outputFileMainName = executionContext
				.getString("outputFileMainName");
		String outputPathAndOutputFileMainName = executionContext
				.getString("outputPathAndOutputFileMainName");

		/**
		 * 初始化commandLine变量
		 */
		String newCommandLine = parameterLine;

		/**
		 * 构建新的命令行参数
		 */
		newCommandLine = newCommandLine.replaceAll(":username:", username);
		newCommandLine = newCommandLine.replaceAll(":command:", command);
		newCommandLine = newCommandLine.replaceAll(":input:", input);
		newCommandLine = newCommandLine.replaceAll(":outputPath:", outputPath);
		newCommandLine = newCommandLine.replaceAll(":inputFileMainName:",
				inputFileMainName);
		newCommandLine = newCommandLine.replaceAll(":inputExtension:",
				inputExtension);
		newCommandLine = newCommandLine.replaceAll(":outputFileMainName:",
				outputFileMainName);
		newCommandLine = newCommandLine.replaceAll(
				":outputPathAndOutputFileMainName:",
				outputPathAndOutputFileMainName);

		return newCommandLine;
	}

	/**
	 * 在ExecutionContext中更新commandLine变量
	 * 
	 * @param chunkContext
	 * @param commandLine
	 */
	public static void putCommandLineToExecutionContext(
			ChunkContext chunkContext, String commandLine) {
		ExecutionContext executionContext = chunkContext.getStepContext()
				.getStepExecution().getJobExecution().getExecutionContext();

		StringBuffer buffer;
		if (executionContext.containsKey("commandLine") == false) {
			buffer = new StringBuffer();
		} else {
			buffer = (StringBuffer) executionContext.get("commandLine");
		}
		buffer.append(commandLine).append(System.getProperty("line.separator"));
		executionContext.put("commandLine", buffer);
	}

	/**
	 * 更新ExecutionContext变量
	 * 
	 * @param chunkContext
	 * @param key
	 * @param value
	 */
	public static void putVarToExecution(ChunkContext chunkContext, String key,
			String value) {

		ExecutionContext executionContext = chunkContext.getStepContext()
				.getStepExecution().getJobExecution().getExecutionContext();
		executionContext.putString(key, value);
	}

	/**
	 * 从ExecutionContext中获取变量
	 * 
	 * @param chunkContext
	 * @param key
	 * @return
	 */
	public static String getVarFromExecutionContext(ChunkContext chunkContext,
			String key) {
		ExecutionContext executionContext = chunkContext.getStepContext()
				.getStepExecution().getJobExecution().getExecutionContext();
		String value = executionContext.getString(key);
		return value;
	}

	/**
	 * 获取CommandLine变量
	 * 
	 * @param chunkContext
	 * @return
	 */
	public static StringBuffer getCommandLineFromExecutionContext(
			ChunkContext chunkContext) {
		ExecutionContext executionContext = chunkContext.getStepContext()
				.getStepExecution().getJobExecution().getExecutionContext();
		StringBuffer result = (StringBuffer) executionContext
				.get("commandLine");
		return result;
	}

}
