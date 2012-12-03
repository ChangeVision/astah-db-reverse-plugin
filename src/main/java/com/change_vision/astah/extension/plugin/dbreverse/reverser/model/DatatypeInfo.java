package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

public class DatatypeInfo {

	private String name;

	private String lengthConstraint;

	private String precisionConstraint;

	private String defaultLength;

	private String defaultPrecision;

	private String description;

	public DatatypeInfo() {
		name = "";
		lengthConstraint = "None";
		precisionConstraint = "None";
		defaultLength = "";
		defaultPrecision = "";
		description = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLengthConstraint() {
		return lengthConstraint;
	}

	public void setLengthConstraint(String lengthConstraint) {
		this.lengthConstraint = lengthConstraint;
	}

	public String getPrecisionConstraint() {
		return precisionConstraint;
	}

	public void setPrecisionConstraint(String precisionConstraint) {
		this.precisionConstraint = precisionConstraint;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
    @Override
    public String toString() {
        return "DatatypeInfo [name=" + name + ", lengthConstraint=" + lengthConstraint
                + ", precisionConstraint=" + precisionConstraint + ", defaultLength="
                + defaultLength + ", defaultPrecision=" + defaultPrecision + ", description="
                + description + "]";
    }

}