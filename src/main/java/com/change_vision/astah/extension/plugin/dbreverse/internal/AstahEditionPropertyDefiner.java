package com.change_vision.astah.extension.plugin.dbreverse.internal;

import ch.qos.logback.core.PropertyDefinerBase;

import com.change_vision.astah.extension.plugin.dbreverse.util.AstahAPIUtil;


public class AstahEditionPropertyDefiner extends PropertyDefinerBase {

	private AstahAPIUtil handler = new AstahAPIUtil();

	@Override
	public String getPropertyValue() {
		String edition = handler.getEdition();
		if (edition.isEmpty()) {
			edition = "professional";
		}
		return edition;
	}
}