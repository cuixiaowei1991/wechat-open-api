package com.cn.batch;

import java.io.File;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

public class CleanTasklet implements Tasklet {
	@Override
	public RepeatStatus execute(StepContribution setpContribution,
			ChunkContext chunkContext) throws Exception {
		String outputPathAndOutputFileMainName = CommandLineHandler
				.getVarFromExecutionContext(chunkContext,
						"outputPathAndOutputFileMainName");

		int captureTimes = Integer.parseInt(CommandLineHandler
				.getVarFromExecutionContext(chunkContext, "captureTimes"));

		StringBuffer buffer = new StringBuffer();
		buffer.append(outputPathAndOutputFileMainName + "_tmp" + ".avi")
				.append(";");
		buffer.append(outputPathAndOutputFileMainName + "_index" + ".avi")
				.append(";");
		for (int i = 0; i < captureTimes; ++i) {
			buffer.append(outputPathAndOutputFileMainName + "_src_" + i
					+ ".jpg");
			buffer.append(";");
		}
		String[] files = buffer.toString().split(";");
		for (int i = 0; i < files.length; ++i) {
			File file = new File(files[i]);
			file.delete();
		}
		return RepeatStatus.FINISHED;
	}

}
