package com.change_vision.astah.extension.plugin.dbreverse.internal;

import javax.swing.JFrame;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.view.IViewManager;

public class AstahAPIHandler {

	public JFrame getMainFrame() {
		IViewManager viewManager;
		try {
			viewManager = getProjectAccessor().getViewManager();
		} catch (InvalidUsingException e) {
			throw new IllegalStateException(e);
		}
		return viewManager.getMainFrame();
	}

	public String getEdition() {
		return getProjectAccessor().getAstahEdition();
	}

	public ProjectAccessor getProjectAccessor() {
		ProjectAccessor projectAccessor = null;
		try {
			projectAccessor = ProjectAccessorFactory.getProjectAccessor();
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		if (projectAccessor == null) {
			throw new IllegalStateException("projectAccessor is null.");
		}

		return projectAccessor;
	}
}