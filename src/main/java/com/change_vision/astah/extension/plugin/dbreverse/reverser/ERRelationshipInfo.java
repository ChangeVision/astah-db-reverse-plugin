package com.change_vision.astah.extension.plugin.dbreverse.reverser;

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
	
}