package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

import java.util.ArrayList;
import java.util.List;


public class TableInfo {

	private String name;

	//Type for ER Entity, it should be "Event", "Resource","Summary" or ""
	private String type;

	private String definition;

	private List<AttributeInfo> attributes;

	private List<IndexInfo> indexes;

	public TableInfo() {
		name = "";
		type = "";
		definition = "";
		attributes = new ArrayList<AttributeInfo>();
		indexes = new ArrayList<IndexInfo>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void addAttribute(AttributeInfo info) {
		info.setParentTable(name);
		attributes.add(info);
	}

	public void addAttributes(List<AttributeInfo> attributes) {
		for (AttributeInfo attributeInfo : attributes) {
			addAttribute(attributeInfo);
		}
	}

	public List<AttributeInfo> getAttributes() {
		return attributes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setIndexes(List<IndexInfo> indexes) {
		this.indexes = indexes;
	}

	public void addIndex(IndexInfo index) {
		indexes.add(index);
	}

	public List<IndexInfo> getIndexes() {
		return indexes;
	}

    @Override
    public String toString() {
        return "TableInfo [name=" + name + ", type=" + type + ", definition=" + definition
                + ", attributes=" + attributes + ", indexes=" + indexes + "]";
    }
	
	
}