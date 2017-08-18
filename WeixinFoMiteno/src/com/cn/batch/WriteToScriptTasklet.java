package com.cn.batch;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

public class WriteToScriptTasklet implements Tasklet {

	private String outputPath;

	@Override
	public RepeatStatus execute(StepContribution setpContribution,
			ChunkContext chunkContext) throws Exception {
		/**
		 * 获取username和outputFileMainName变量
		 */
		String username = CommandLineHandler.getVarFromExecutionContext(
				chunkContext, "username");
		String outputFileMainName = CommandLineHandler
				.getVarFromExecutionContext(chunkContext, "outputFileMainName");
		StringBuffer commandLine = CommandLineHandler
				.getCommandLineFromExecutionContext(chunkContext);

		/**
		 * 构建xmlFilePath
		 */
		String scriptFileFullPath = getOutputPath() + username + "_"
				+ outputFileMainName + ".bat";
		File scriptFile = new File(scriptFileFullPath);

		/**
		 * 写入script文件
		 */
		scriptFile.createNewFile();
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(
				new FileOutputStream(scriptFileFullPath)));
		pw.println(commandLine);
		pw.flush();
		pw.close();

		/**
		 * 将scriptFileFullPath变量保存到ExecutionContext
		 */
		CommandLineHandler.putVarToExecution(chunkContext,
				"scriptFileFullPath", scriptFileFullPath);

		/**
		 * 返回
		 */
		return RepeatStatus.FINISHED;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

}
