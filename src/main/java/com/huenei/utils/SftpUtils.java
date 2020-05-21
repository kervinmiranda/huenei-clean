package com.huenei.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Component
public class SftpUtils {

	private static String remoteHost;
	private static String username;
	private static String pswd;

	private static final Logger LOGGER = LoggerFactory.getLogger(SftpUtils.class);

	private ChannelSftp setupJsch() throws JSchException {
		LOGGER.info("Init Connection SFTP");
		JSch jsch = new JSch();
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		Session jschSession = jsch.getSession(username, remoteHost);
		jschSession.setConfig(config);
		jschSession.setPassword(pswd);
		jschSession.connect();
		return (ChannelSftp) jschSession.openChannel("sftp");
	}

	public void putFile(String localFile) throws SftpException, JSchException {
		LOGGER.info("Init Put file to SFTP");
		ChannelSftp channelSftp = setupJsch();
		channelSftp.connect();
		String remoteDir = "home/sftp/";
		channelSftp.put(localFile, remoteDir + "employeesClean.csv");
		channelSftp.exit();
	}

	@Value("${sftp.host}")
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	@Value("${sftp.user}")
	public void setUsername(String username) {
		this.username = username;
	}

	@Value("${sftp.pswd}")
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

}
