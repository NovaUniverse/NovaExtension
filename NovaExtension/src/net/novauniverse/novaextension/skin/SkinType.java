package net.novauniverse.novaextension.skin;

public enum SkinType {
	STEVE("steve"), SLIM("slim");
	
	private String typeName;
	
	private SkinType(String typeName) {
		this.typeName = typeName;
	}
	
	public String getTypeName() {
		return typeName;
	}
}