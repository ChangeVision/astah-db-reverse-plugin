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
}