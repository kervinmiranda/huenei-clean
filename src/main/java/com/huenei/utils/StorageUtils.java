package com.huenei.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.huenei.domain.MessageGcp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageUtils.class);

	public void fileMoveToSFTP(String message) {
		SftpUtils sftUtils = new SftpUtils();
		MessageGcp data = new Gson().fromJson(message, MessageGcp.class);
		Storage storage = StorageOptions.getDefaultInstance().getService();
		Blob blob = storage.get(BlobId.of(data.getBody(), data.getValue()));
		Path destFilePath = Paths.get("src/main/resources/employees.csv");
		blob.downloadTo(destFilePath);
		try {
			encrytName();
			sftUtils.putFile("src/main/resources/employeesClean.csv");
		} catch (SftpException | JSchException | IOException e) {
			LOGGER.error("Error", e);
		}

	}

	public void encrytName() throws IOException {
		CSVReader reader = new CSVReader(new FileReader("src/main/resources/file.csv"));
		CSVWriter writers = new CSVWriter(new FileWriter("src/main/resources/newfile.csv"));
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			List<String> lineAsList = new ArrayList<>(Arrays.asList(nextLine));
			lineAsList.set(1, mask(lineAsList.get(1)));
			writers.writeNext(lineAsList.toArray(new String[0]));
		}
		writers.close();
		reader.close();
	}

	public String mask(String name) {
		StringBuilder maskName = new StringBuilder();
		if (!name.equals("employeeName")) {
			maskName.append(name);
			name = maskName.reverse().toString();
		}
		return name;
	}

}
