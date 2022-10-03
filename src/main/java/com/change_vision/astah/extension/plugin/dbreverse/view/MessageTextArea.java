package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class MessageTextArea extends JTextArea {

	private static final long serialVersionUID = -7352942865238325817L;

	private static MessageTextArea instance = null;

	public MessageTextArea() {
		setEditable(false);
		setAutoscrolls(true);
		setLineWrap(true);
		setWrapStyleWord(true);
		setRows(6);
	}

	public static MessageTextArea getInstance() {
		if (instance == null) {
			instance = new MessageTextArea();
		}

		return instance;
	}
}