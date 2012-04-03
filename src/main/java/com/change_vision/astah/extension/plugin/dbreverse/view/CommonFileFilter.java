package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CommonFileFilter  extends FileFilter {
	private String[] extentions;
	private String description;

	public CommonFileFilter(String[] extentions, String description) {
		this.extentions = extentions;
		this.description = description;
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		for (String ext : extentions) {
			if (f.getName().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		StringBuilder fullDescription = new StringBuilder("");
		if (description == null) {
			fullDescription.append("(");
		} else {
			fullDescription.append(description)
			.append(" (");
		}

		int size = extentions.length;
		for (int i = 0; i < size; i++) {
			String ext = extentions[i];
			if (i == 0 && !ext.startsWith(".")) {
				fullDescription.append(".");
			}
			fullDescription.append(ext);
			if (i != size - 1) {
				fullDescription.append(", ");
			}
		}
		fullDescription.append(")");

		return fullDescription.toString();
	}
}