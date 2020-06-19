package com.lfg.poc.opentelemetry.stockservice;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.Base58;
import org.testcontainers.utility.MountableFile;


public class ElasticsearchDumpLoader extends GenericContainer<ElasticsearchDumpLoader> {
	
	private static Logger logger = LoggerFactory.getLogger("ElasticsearchDumpLoader");
	
	private ElasticsearchDumpLoader(String dumpFilesClassPathLocation) {
		super("node:alpine");
		logger.info("Starting an elasticsearch dump loader container using [{}]", "node:alpine");
		withNetworkAliases("elasticsearch-dump-loader-" + Base58.randomString(6))
				.withCopyFileToContainer(MountableFile.forClasspathResource(dumpFilesClassPathLocation), "/etc/temp/")
				.withCommand("/bin/sh", "-c", "FILE=dump_done; until [ -f \"$FILE\" ]; do sleep 5; done");
    }
	
	public static ElasticsearchDumpLoader getInstance(String dumpFilesClassPathLocation) {
		return new ElasticsearchDumpLoader(dumpFilesClassPathLocation);
	}
	
	public void dump(String esHttpHostAddress, String index, String mappingFileName, String dataFileName) {
		
		String mappingCommand = "elasticdump --input=/etc/temp/" + mappingFileName
				+ " --output=http://" + esHttpHostAddress + "/" + index + " --type=mapping";
		
		String dumpCommand = "elasticdump --input=/etc/temp/" + dataFileName
				+ " --output=http://" + esHttpHostAddress + "/" + index + " --type=data";
		
		runCommand("Installing elasticdump..", "npm install elasticdump -g");
		runCommand("Installing curl..", "apk --no-cache add curl");
		runCommand("Checking elasticsearch..", "curl http://" + esHttpHostAddress);
		runCommand("Loading mapping file..", mappingCommand);
		runCommand("Dumping data..", dumpCommand);
		runCommand("Finishing dump..", "touch dump_done");
	}
	
	private void runCommand(String title, String command) {
		
		print(title);
		
		try {
			ExecResult result = execInContainer("/bin/sh", "-c", command);
			printExecResult(command, result);
		} catch (UnsupportedOperationException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private void printExecResult(String command, ExecResult result) {
		logger.info("excuted command " + command + " with result: ");
		print(result.getStdout());
		print(result.getStderr());
	}
	
	private void print(String text) {
		if(!StringUtils.isEmpty(text))
			logger.info(text.replaceAll("(?m)^[ \t]*\r?\n", ""));
	}
}
