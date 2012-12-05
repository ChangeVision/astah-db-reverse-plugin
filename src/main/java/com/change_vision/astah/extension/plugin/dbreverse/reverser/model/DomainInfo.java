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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((datatypeName == null) ? 0 : datatypeName.hashCode());
        result = prime * result + ((defaultLength == null) ? 0 : defaultLength.hashCode());
        result = prime * result + ((defaultPrecision == null) ? 0 : defaultPrecision.hashCode());
        result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result + ((definition == null) ? 0 : definition.hashCode());
        result = prime * result + (isNotNull ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DomainInfo other = (DomainInfo) obj;
        if (datatypeName == null) {
            if (other.datatypeName != null) return false;
        } else if (!datatypeName.equals(other.datatypeName)) return false;
        if (defaultLength == null) {
            if (other.defaultLength != null) return false;
        } else if (!defaultLength.equals(other.defaultLength)) return false;
        if (defaultPrecision == null) {
            if (other.defaultPrecision != null) return false;
        } else if (!defaultPrecision.equals(other.defaultPrecision)) return false;
        if (defaultValue == null) {
            if (other.defaultValue != null) return false;
        } else if (!defaultValue.equals(other.defaultValue)) return false;
        if (definition == null) {
            if (other.definition != null) return false;
        } else if (!definition.equals(other.definition)) return false;
        if (isNotNull != other.isNotNull) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
	
}