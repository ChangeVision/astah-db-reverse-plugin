package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

public class SubtypeRelationshipInfo extends RelationshipInfo {

	private String discriminatorAttribute;

	private boolean isConclusive;

	public SubtypeRelationshipInfo() {
		init();
	}

	@Override
	protected void init() {
		super.init();
		discriminatorAttribute = "";
		isConclusive = false;
	}

	public String getDiscriminatorAttribute() {
		return discriminatorAttribute;
	}

	public void setDiscriminatorAttribute(String discriminatorAttribute) {
		this.discriminatorAttribute = discriminatorAttribute;
	}

	public boolean isConclusive() {
		return isConclusive;
	}

	public void setConclusive(boolean isConclusive) {
		this.isConclusive = isConclusive;
	}

    @Override
    public String toString() {
        return "SubtypeRelationshipInfo [discriminatorAttribute=" + discriminatorAttribute
                + ", isConclusive=" + isConclusive + ", name=" + name + ", parentTable="
                + parentTable + ", childTable=" + childTable + ", definition=" + definition
                + ", keys=" + keys + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((discriminatorAttribute == null) ? 0 : discriminatorAttribute.hashCode());
        result = prime * result + (isConclusive ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        SubtypeRelationshipInfo other = (SubtypeRelationshipInfo) obj;
        if (discriminatorAttribute == null) {
            if (other.discriminatorAttribute != null) return false;
        } else if (!discriminatorAttribute.equals(other.discriminatorAttribute)) return false;
        if (isConclusive != other.isConclusive) return false;
        return true;
    }
	
}