package com.change_vision.astah.extension.plugin.dbreverse.util;

import java.util.prefs.Preferences;

public abstract class ReversePreferences extends Preferences {

	private static Preferences instance = null;

	public static Preferences getInstace(Class<?> clazz) {
		if (instance == null) {
			instance = Preferences.userNodeForPackage(clazz);
		}

		return instance;
	}

	public static Preferences getInstance() {
		return instance;
	}
}