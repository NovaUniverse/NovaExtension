package net.novauniverse.novaextension.skin;

import net.skinsrestorer.api.SkinVariant;

public enum SkinType {
	STEVE("steve"), SLIM("slim");

	private String typeName;

	private SkinType(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public SkinVariant toVariant() {
		switch (this) {
		case SLIM:
			return SkinVariant.SLIM;

		case STEVE:
			return SkinVariant.CLASSIC;

		default:
			return null;
		}
	}
}