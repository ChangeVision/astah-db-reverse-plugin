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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((childTable == null) ? 0 : childTable.hashCode());
        result = prime * result + ((definition == null) ? 0 : definition.hashCode());
        result = prime * result + ((keys == null) ? 0 : keys.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parentTable == null) ? 0 : parentTable.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RelationshipInfo other = (RelationshipInfo) obj;
        if (childTable == null) {
            if (other.childTable != null) return false;
        } else if (!childTable.equals(other.childTable)) return false;
        if (definition == null) {
            if (other.definition != null) return false;
        } else if (!definition.equals(other.definition)) return false;
        if (keys == null) {
            if (other.keys != null) return false;
        } else if (!keys.equals(other.keys)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (parentTable == null) {
            if (other.parentTable != null) return false;
        } else if (!parentTable.equals(other.parentTable)) return false;
        return true;
    }
	
}