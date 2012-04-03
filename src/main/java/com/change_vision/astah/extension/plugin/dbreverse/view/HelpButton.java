package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;

public class HelpButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 3790149848394492688L;

	private static final String NAME = "button.help";

	private static Logger logger = LoggerFactory.getLogger(HelpButton.class);

	public HelpButton() {
		setName(NAME);
		setText(Messages.getMessage("button.text.help"));
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Desktop desktop = Desktop.getDesktop();
		try {
			URL url = new URL(Messages.getMessage("help.url"));
			desktop.browse(url.toURI());
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		}
	}
}