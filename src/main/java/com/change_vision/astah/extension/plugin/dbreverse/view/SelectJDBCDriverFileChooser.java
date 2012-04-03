package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class SelectJDBCDriverFileChooser extends JFileChooser {

	private static final long serialVersionUID = -967994978981753371L;

	private static final String[] EXTENTIONS = new String[] {".jar"};

	private static final String DESCRIPTION = "JDBC Driver";

	private String filePath = null;

	public SelectJDBCDriverFileChooser() {
		FileFilter filter = new CommonFileFilter(EXTENTIONS, DESCRIPTION);
		addChoosableFileFilter(filter);
		setFileFilter(filter);

		if (showOpenDialog(ReverseDialog.getInstance().getGlassPane()) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = getSelectedFile();
		if (file == null) {
			return;
		}

		this.filePath = file.getAbsolutePath();
	}

	public String getFilePath() {
		return this.filePath;
	}
}