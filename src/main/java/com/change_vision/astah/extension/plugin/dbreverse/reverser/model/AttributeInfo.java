package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;


public class AttributeInfo {

	private String parentTable;

	private String name;

	private DatatypeInfo dataType;

	private DomainInfo domain;

	private String defaultValue;

	private String length;

	private String precision;

	private String definition;

	private boolean isPK;

	private boolean isFK;

	private boolean isNotNull;

	public AttributeInfo() {
		name = "";
		dataType = new DatatypeInfo();
		domain = new DomainInfo();
		defaultValue = "";
		length = "";
		precision = "";
		definition = "";
		isPK = false;
		isNotNull = false;
		parentTable = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DatatypeInfo getDataType() {
		return dataType;
	}

	public void setDataType(DatatypeInfo dataType) {
		this.dataType = dataType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}

	public boolean isNotNull() {
		return isNotNull;
	}

	public void setNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}

	public DomainInfo getDomain() {
		return domain;
	}

	public void setDomain(DomainInfo domain) {
		this.domain = domain;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getParentTable() {
		return parentTable;
	}

	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}

	public boolean isFK() {
		return isFK;
	}

	public void setFK(boolean isFK) {
		this.isFK = isFK;
	}
	
	public String getLengthPrecision(){
        StringBuilder lengthPrecision = new StringBuilder();
        lengthPrecision.append(length);
        lengthPrecision.append(("".equals(length) || "".equals(precision)) ? "" : ",");
        lengthPrecision.append(precision);
	    return lengthPrecision.toString();
	}

    @Override
    public String toString() {
        return "AttributeInfo [parentTable=" + parentTable + ", name=" + name + ", dataType="
                + dataType + ", domain=" + domain + ", defaultValue=" + defaultValue + ", length="
                + length + ", precision=" + precision + ", definition=" + definition + ", isPK="
                + isPK + ", isFK=" + isFK + ", isNotNull=" + isNotNull + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
        result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result + ((definition == null) ? 0 : definition.hashCode());
        result = prime * result + ((domain == null) ? 0 : domain.hashCode());
        result = prime * result + (isFK ? 1231 : 1237);
        result = prime * result + (isNotNull ? 1231 : 1237);
        result = prime * result + (isPK ? 1231 : 1237);
        result = prime * result + ((length == null) ? 0 : length.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parentTable == null) ? 0 : parentTable.hashCode());
        result = prime * result + ((precision == null) ? 0 : precision.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AttributeInfo other = (AttributeInfo) obj;
        if (dataType == null) {
            if (other.dataType != null) return false;
        } else if (!dataType.equals(other.dataType)) return false;
        if (defaultValue == null) {
            if (other.defaultValue != null) return false;
        } else if (!defaultValue.equals(other.defaultValue)) return false;
        if (definition == null) {
            if (other.definition != null) return false;
        } else if (!definition.equals(other.definition)) return false;
        if (domain == null) {
            if (other.domain != null) return false;
        } else if (!domain.equals(other.domain)) return false;
        if (isFK != other.isFK) return false;
        if (isNotNull != other.isNotNull) return false;
        if (isPK != other.isPK) return false;
        if (length == null) {
            if (other.length != null) return false;
        } else if (!length.equals(other.length)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (parentTable == null) {
            if (other.parentTable != null) return false;
        } else if (!parentTable.equals(other.parentTable)) return false;
        if (precision == null) {
            if (other.precision != null) return false;
        } else if (!precision.equals(other.precision)) return false;
        return true;
    }
	
}