package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

import java.util.HashMap;
import java.util.Map;

public abstract class RelationshipInfo {

	protected String name;

	protected String parentTable;

	protected String childTable;

	protected String definition;

	protected Map<String, String> keys;

	protected void init() {
		name = "";
		parentTable = "";
		childTable = "";
		definition = "";
		keys = new HashMap<String, String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentTable() {
		return parentTable;
	}

	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}

	public String getChildTable() {
		return childTable;
	}

	public void setChildTable(String childTable) {
		this.childTable = childTable;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void addKey(String parentKey, String childKey) {
		keys.put(parentKey, childKey);
	}

	public void removeKey(String parentKey) {
		keys.remove(parentKey);
	}

	public Map<String, String> getKeys() {
		return keys;
	}

    @Override
    public String toString() {
        return "RelationshipInfo [name=" + name + ", parentTable=" + parentTable + ", childTable="
                + childTable + ", definition=" + definition + ", keys=" + keys + "]";
    }
	
	
}