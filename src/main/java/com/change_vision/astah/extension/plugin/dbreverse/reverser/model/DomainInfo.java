package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

public class DomainInfo {

	private String name;

	private String datatypeName;

	private String defaultLength;

	private String defaultPrecision;

	private String defaultValue;

	private boolean isNotNull;

	private String definition;

	public DomainInfo() {
		name = "";
		datatypeName = "";
		defaultLength = "";
		defaultPrecision = "";
		defaultValue = "";
		isNotNull = false;
		definition = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String physicalName) {
		this.name = physicalName;
	}

	public String getDatatypeName() {
		return datatypeName;
	}

	public void setDatatypeName(String datatypeName) {
		this.datatypeName = datatypeName;
	}

	public String getDefaultLength() {
		return defaultLength;
	}

	public void setDefaultLength(String defaultLength) {
		this.defaultLength = defaultLength;
	}

	public String getDefaultPrecision() {
		return defaultPrecision;
	}

	public void setDefaultPrecision(String defaultPrecision) {
		this.defaultPrecision = defaultPrecision;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isNotNull() {
		return isNotNull;
	}

	public void setNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

    @Override
    public String toString() {
        return "DomainInfo [name=" + name + ", datatypeName=" + datatypeName + ", defaultLength="
                + defaultLength + ", defaultPrecision=" + defaultPrecision + ", defaultValue="
                + defaultValue + ", isNotNull=" + isNotNull + ", definition=" + definition + "]";
    }
	
}