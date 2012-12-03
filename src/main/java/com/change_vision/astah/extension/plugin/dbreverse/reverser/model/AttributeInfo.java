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

    @Override
    public String toString() {
        return "AttributeInfo [parentTable=" + parentTable + ", name=" + name + ", dataType="
                + dataType + ", domain=" + domain + ", defaultValue=" + defaultValue + ", length="
                + length + ", precision=" + precision + ", definition=" + definition + ", isPK="
                + isPK + ", isFK=" + isFK + ", isNotNull=" + isNotNull + "]";
    }
	
	
}