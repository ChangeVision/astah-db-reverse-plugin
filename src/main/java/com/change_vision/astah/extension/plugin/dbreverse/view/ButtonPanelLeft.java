package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.FlowLayout;

import javax.swing.JPanel;

public class ButtonPanelLeft extends JPanel {

	private static final long serialVersionUID = -429273504694722027L;

	public ButtonPanelLeft() {
		setLayout(new FlowLayout(FlowLayout.LEFT));

		add(new HelpButton());
	}
}