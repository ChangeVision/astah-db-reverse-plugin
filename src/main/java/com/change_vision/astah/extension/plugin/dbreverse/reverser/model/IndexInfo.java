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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + (isUnique ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parentEntity == null) ? 0 : parentEntity.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IndexInfo other = (IndexInfo) obj;
        if (attributes == null) {
            if (other.attributes != null) return false;
        } else if (!attributes.equals(other.attributes)) return false;
        if (isUnique != other.isUnique) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (parentEntity == null) {
            if (other.parentEntity != null) return false;
        } else if (!parentEntity.equals(other.parentEntity)) return false;
        return true;
    }
	
}