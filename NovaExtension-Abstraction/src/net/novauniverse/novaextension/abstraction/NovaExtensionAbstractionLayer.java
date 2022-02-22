package net.novauniverse.novaextension.abstraction;

import org.bukkit.plugin.Plugin;

public abstract class NovaExtensionAbstractionLayer {
	private Plugin ownerPlugin;
	
	public Plugin getOwnerPlugin() {
		return ownerPlugin;
	}
	
	public void setOwnerPlugin(Plugin ownerPlugin) {
		this.ownerPlugin = ownerPlugin;
	}
}