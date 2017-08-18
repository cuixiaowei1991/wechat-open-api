package com.cn.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.cn.batch.utils.CommandLineHandler;

public class WriteToXMLTasklet implements Tasklet {

	/**
	 * ���·��
	 */
	private String outputPath;

	@Override
	public RepeatStatus execute(StepContribution setpContribution,
			ChunkContext chunkContext) throws Exception {
		/**
		 * �������ʱ��
		 */
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String endTime = format.format(currentDate);
		CommandLineHandler.putVarToExecution(chunkContext, "endTime", endTime);

		/**
		 * ת��xml
		 */
		Document xmlDocument = convertExecutionContextToXMLDocument(chunkContext);

		/**
		 * ��ȡusername��outputFileMainName����
		 */
		String username = CommandLineHandler.getVarFromExecutionContext(
				chunkContext, "username");
		String outputFileMainName = CommandLineHandler
				.getVarFromExecutionContext(chunkContext, "outputFileMainName");

		/**
		 * ����xmlFilePath
		 */
		String xmlFilePath = getOutputPath() + username + "_"
				+ outputFileMainName + ".xml";
		File xmlFile = new File(xmlFilePath);

		/**
		 * ����xml��ʽ
		 */
		OutputFormat outPutFormat = OutputFormat.createPrettyPrint();
		outPutFormat.setEncoding("UTF8");
		XMLWriter xmlwriter = new XMLWriter(new FileOutputStream(xmlFile),
				outPutFormat);

		/**
		 * д��xml�ļ�
		 */
		xmlwriter.write(xmlDocument);
		xmlwriter.close();

		return RepeatStatus.FINISHED;
	}

	/**
	 * ��ExecutionContext�еı���ת��Ϊxml�ļ�
	 * 
	 * @param chunkContext
	 * @return
	 */
	public Document convertExecutionContextToXMLDocument(
			ChunkContext chunkContext) {
		ExecutionContext executionContext = chunkContext.getStepContext()
				.getStepExecution().getJobExecution().getExecutionContext();
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("media");
		Iterator<?> it = executionContext.entrySet().iterator();
		while (it.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) it.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			root.addElement(key).addText(value);
		}
		return document;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

}
