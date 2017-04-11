package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

import java.util.ArrayList;
import java.util.List;


public class TableInfo {
    
    private String catalog;
    
    private String schema;

	private String name;

	//Type for ER Entity, it should be "Event", "Resource","Summary" or ""
	private String type;

	private String definition;

	private List<AttributeInfo> attributes;

	private List<IndexInfo> indexes;

	public TableInfo(String catalog,String schema, String name) {
	    this.catalog = catalog;
	    this.schema = schema;
		this.name = name;
		type = "";
		definition = "";
		attributes = new ArrayList<AttributeInfo>();
		indexes = new ArrayList<IndexInfo>();
	}

	public String getCatalog() {
        return catalog;
    }
	
	public String getSchema() {
        return schema;
    }
	
	public String getName() {
		return name;
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

	public AttributeInfo getAttributeInfo(String name) {
        for (AttributeInfo attributeInfo : attributes) {
            if (attributeInfo.getName().equals(name)) {
                return attributeInfo;
            }
        }
        return null;
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
        return "TableInfo [category=" + catalog + ", name=" + name + ", type=" + type
                + ", definition=" + definition + ", attributes=" + attributes + ", indexes="
                + indexes + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
        result = prime * result + ((definition == null) ? 0 : definition.hashCode());
        result = prime * result + ((indexes == null) ? 0 : indexes.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TableInfo other = (TableInfo) obj;
        if (attributes == null) {
            if (other.attributes != null)
                return false;
        } else if (!attributes.equals(other.attributes))
            return false;
        if (catalog == null) {
            if (other.catalog != null)
                return false;
        } else if (!catalog.equals(other.catalog))
            return false;
        if (definition == null) {
            if (other.definition != null)
                return false;
        } else if (!definition.equals(other.definition))
            return false;
        if (indexes == null) {
            if (other.indexes != null)
                return false;
        } else if (!indexes.equals(other.indexes))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }


	
}