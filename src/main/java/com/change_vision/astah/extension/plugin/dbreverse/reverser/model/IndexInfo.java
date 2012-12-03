package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

import java.util.ArrayList;
import java.util.List;

public class IndexInfo {

	private String name;

	private String parentEntity;

	private List<String> attributes;

	private boolean isUnique;

	public IndexInfo() {
		name = "";
		parentEntity = "";
		attributes = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(String parentEntity) {
		this.parentEntity = parentEntity;
	}

	public void addAttribute(String attrName) {
		attributes.add(attrName);
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public boolean isUnique() {
		return isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

    @Override
    public String toString() {
        return "IndexInfo [name=" + name + ", parentEntity=" + parentEntity + ", attributes="
                + attributes + ", isUnique=" + isUnique + ", getClass()=" + getClass()
                + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
	
}