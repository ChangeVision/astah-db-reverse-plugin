package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;


public class ERRelationshipInfo extends RelationshipInfo {

	private String verbPhraseParent;

	private String verbPhraseChild;

	private boolean isIdentifying;

	private boolean isNonIdentifying;

	private boolean isMultiToMulti;

	private boolean isParentRequired;

	private String erIndex;

	public ERRelationshipInfo() {
		init();
	}

	@Override
	protected void init() {
		super.init();
		verbPhraseParent = "";
		verbPhraseChild = "";
		isIdentifying = false;
		isNonIdentifying = false;
		isMultiToMulti = false;
		isParentRequired = false;
	}

	public String getVerbPhraseParent() {
		return verbPhraseParent;
	}

	public void setVerbPhraseParent(String verbPhraseParent) {
		this.verbPhraseParent = verbPhraseParent;
	}

	public String getVerbPhraseChild() {
		return verbPhraseChild;
	}

	public void setVerbPhraseChild(String verbPhraseChild) {
		this.verbPhraseChild = verbPhraseChild;
	}

	public boolean isIdentifying() {
		return isIdentifying;
	}

	public void setIdentifying(boolean isIdentifying) {
		this.isIdentifying = isIdentifying;
	}

	public boolean isNonIdentifying() {
		return isNonIdentifying;
	}

	public void setNonIdentifying(boolean isNonIdentifying) {
		this.isNonIdentifying = isNonIdentifying;
	}

	public boolean isMultiToMulti() {
		return isMultiToMulti;
	}

	public void setMultiToMulti(boolean isMultiToMulti) {
		this.isMultiToMulti = isMultiToMulti;
	}

	public boolean isParentRequired() {
		return isParentRequired;
	}

	public void setParentRequired(boolean isParentRequired) {
		this.isParentRequired = isParentRequired;
	}

	public String getErIndex() {
		return erIndex;
	}

	public void setErIndex(String erIndex) {
		this.erIndex = erIndex;
	}

    @Override
    public String toString() {
        return "ERRelationshipInfo [verbPhraseParent=" + verbPhraseParent + ", verbPhraseChild="
                + verbPhraseChild + ", isIdentifying=" + isIdentifying + ", isNonIdentifying="
                + isNonIdentifying + ", isMultiToMulti=" + isMultiToMulti + ", isParentRequired="
                + isParentRequired + ", erIndex=" + erIndex + ", name=" + name + ", parentTable="
                + parentTable + ", childTable=" + childTable + ", definition=" + definition
                + ", keys=" + keys + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((erIndex == null) ? 0 : erIndex.hashCode());
        result = prime * result + (isIdentifying ? 1231 : 1237);
        result = prime * result + (isMultiToMulti ? 1231 : 1237);
        result = prime * result + (isNonIdentifying ? 1231 : 1237);
        result = prime * result + (isParentRequired ? 1231 : 1237);
        result = prime * result + ((verbPhraseChild == null) ? 0 : verbPhraseChild.hashCode());
        result = prime * result + ((verbPhraseParent == null) ? 0 : verbPhraseParent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ERRelationshipInfo other = (ERRelationshipInfo) obj;
        if (erIndex == null) {
            if (other.erIndex != null) return false;
        } else if (!erIndex.equals(other.erIndex)) return false;
        if (isIdentifying != other.isIdentifying) return false;
        if (isMultiToMulti != other.isMultiToMulti) return false;
        if (isNonIdentifying != other.isNonIdentifying) return false;
        if (isParentRequired != other.isParentRequired) return false;
        if (verbPhraseChild == null) {
            if (other.verbPhraseChild != null) return false;
        } else if (!verbPhraseChild.equals(other.verbPhraseChild)) return false;
        if (verbPhraseParent == null) {
            if (other.verbPhraseParent != null) return false;
        } else if (!verbPhraseParent.equals(other.verbPhraseParent)) return false;
        return true;
    }
	
}