package com.change_vision.astah.extension.plugin.dbreverse.util;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {
	public static final String FILE_NAME = "astah_plugin_dbreverse.properties";
	public static final String DEFAULT_DBREVERSE_DIR_KEY = "default_dberevese_directory";

	private static final String FILE_PATH;
	private static Properties config;

	private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

	static {
		config = new Properties();

		StringBuilder builder = new StringBuilder();
		builder.append(System.getProperty("user.home"));
		builder.append(File.separator);
		builder.append(".astah");
		builder.append(File.separator);
		builder.append("professional");
		builder.append(File.separator);
		builder.append(FILE_NAME);

		FILE_PATH = builder.toString();

		load();
	}

	public static String getDefaultDirectoryPath() {
		String outputDirPath = config.getProperty(DEFAULT_DBREVERSE_DIR_KEY);
		if (StringUtils.isBlank(outputDirPath)) {
			outputDirPath = SystemUtils.getUserHome().getAbsolutePath();
		}
		return outputDirPath;
	}

	public static void saveCPlusXmlPath(String path) {
		config.put(DEFAULT_DBREVERSE_DIR_KEY, path);
		store();
	}

	public static void load() {
		InputStream stream = null;
		try {
			stream = new FileInputStream(FILE_PATH);
			config.load(stream);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public static void store() {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(FILE_PATH);
			config.store(stream, null);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
}