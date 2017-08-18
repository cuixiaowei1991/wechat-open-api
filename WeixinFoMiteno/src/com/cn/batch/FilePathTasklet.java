package com.cn.batch;

import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

public class FilePathTasklet implements Tasklet {

	private String outputPath;

	@Override
	public RepeatStatus execute(StepContribution setpContribution,
			ChunkContext chunkContext) throws Exception {
		/**
		 * ����inputϵ�в���
		 */
		String username = (String) chunkContext.getStepContext()
				.getJobParameters().get("username");
		String input = (String) chunkContext.getStepContext()
				.getJobParameters().get("input");
		if (CommandLineHandler.checkInput(input) == CommandLineHandler.CHECK_FAILURE) {
			chunkContext.getStepContext().getStepExecution().getJobExecution()
					.stop();
			return RepeatStatus.FINISHED;
		}
		String inputFileMainName = FilenameUtils.getBaseName(input);
		String inputExtension = FilenameUtils.getExtension(input);

		/**
		 * ����outputϵͳ����
		 */
		if (CommandLineHandler.checkOutputPath(this.getOutputPath()) == CommandLineHandler.CHECK_FAILURE) {
			chunkContext.getStepContext().getStepExecution().getJobExecution()
					.stop();
			return RepeatStatus.FINISHED;
		}
		Digest digest = new RIPEMD128Digest();
		byte[] srcBytes = FilenameUtils.getBaseName(input).getBytes();
		digest.update(srcBytes, 0, srcBytes.length);
		byte[] resultBytes = new byte[digest.getDigestSize()];
		digest.doFinal(resultBytes, 0);
		String outputFileMainName = new String(Hex.encode(resultBytes));
		this.setOutputPath(FilenameUtils.getFullPath(this.getOutputPath()));
		String outputPathAndOutputFileMainName = this.getOutputPath()
				+ outputFileMainName;

		/**
		 * ����ExecutionContextϵ�б���
		 */
		CommandLineHandler.putVarToExecution(chunkContext, "username", username);
		CommandLineHandler.putVarToExecution(chunkContext, "input", input);
		CommandLineHandler.putVarToExecution(chunkContext, "outputPath",
				this.getOutputPath());
		CommandLineHandler.putVarToExecution(chunkContext, "inputFileMainName",
				inputFileMainName);
		CommandLineHandler.putVarToExecution(chunkContext, "inputExtension",
				inputExtension);
		CommandLineHandler.putVarToExecution(chunkContext, "outputFileMainName",
				outputFileMainName);
		CommandLineHandler.putVarToExecution(chunkContext,
				"outputPathAndOutputFileMainName",
				outputPathAndOutputFileMainName);

		return RepeatStatus.FINISHED;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

}
