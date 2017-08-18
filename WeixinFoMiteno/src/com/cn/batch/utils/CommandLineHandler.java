package com.cn.batch.utils;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;

/**
 * ���������������
 * 
 * @author admin
 * 
 */
public class CommandLineHandler {
	/**
	 * ��־
	 */
	private static Logger logger = Logger.getLogger(CommandLineHandler.class);

	/**
	 * ��̬����
	 */
	public final static boolean CHECK_FAILURE = false;
	public final static boolean CHECK_SUCCESS = true;

	/**
	 * ����·�����
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
	 * ���·�����
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
	 * ��������������
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
		 * ��ExecutionContext�л�ȡ����
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
		 * ��ʼ��commandLine����
		 */
		String newCommandLine = parameterLine;

		/**
		 * �����µ������в���
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
	 * ��ExecutionContext�и���commandLine����
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
	 * ����ExecutionContext����
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
	 * ��ExecutionContext�л�ȡ����
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
	 * ��ȡCommandLine����
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
