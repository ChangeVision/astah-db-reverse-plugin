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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((defaultLength == null) ? 0 : defaultLength.hashCode());
        result = prime * result + ((defaultPrecision == null) ? 0 : defaultPrecision.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((lengthConstraint == null) ? 0 : lengthConstraint.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((precisionConstraint == null) ? 0 : precisionConstraint.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DatatypeInfo other = (DatatypeInfo) obj;
        if (defaultLength == null) {
            if (other.defaultLength != null) return false;
        } else if (!defaultLength.equals(other.defaultLength)) return false;
        if (defaultPrecision == null) {
            if (other.defaultPrecision != null) return false;
        } else if (!defaultPrecision.equals(other.defaultPrecision)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (lengthConstraint == null) {
            if (other.lengthConstraint != null) return false;
        } else if (!lengthConstraint.equals(other.lengthConstraint)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (precisionConstraint == null) {
            if (other.precisionConstraint != null) return false;
        } else if (!precisionConstraint.equals(other.precisionConstraint)) return false;
        return true;
    }
    
    

}