package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;

public class CommonGridBagLayout extends GridBagLayout {

	private static final long serialVersionUID = -1418485298443056812L;

	private GridBagConstraints constraint = null;
	private int column;
	private int line;

	public CommonGridBagLayout() {
		super();
		constraint = new GridBagConstraints();
		constraint.gridheight = 1;
		constraint.fill = GridBagConstraints.BOTH;
		column = 0;
		line = 0;
	}

	public void setLayout(JComponent component, int width, double weightx, boolean lineFeed) {
		constraint.gridwidth = width;
		constraint.gridx = column;
		constraint.gridy = line;
		constraint.weightx = weightx;

		setConstraints(component, constraint);

		column += width;
		if (lineFeed) {
			line++;
			column = 0;
		}
	}
}