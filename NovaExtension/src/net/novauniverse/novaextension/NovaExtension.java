package net.novauniverse.novaextension;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.novauniverse.novaextension.skin.SkinManager;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.abstraction.NovaCoreAbstraction;
import net.zeeraa.novacore.spigot.module.ModuleManager;

public class NovaExtension extends JavaPlugin implements Listener {
	private static NovaExtension instance;
	private List<NovaExtensionFeature> features;

	public static NovaExtension getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		NovaExtension.instance = this;
	}

	public boolean hasFeature(NovaExtensionFeature feature) {
		return features.contains(feature);
	}

	@Override
	public void onEnable() {
		features = new ArrayList<>();

		String nmsVersion = NovaCoreAbstraction.getNMSVersion();
		Log.info("NovaExtension", "Server version: " + nmsVersion);

		if (Bukkit.getServer().getPluginManager().getPlugin("SkinsRestorer") != null) {
			Log.info("NovaExtension", "Found SkinsRestorer. Enabling SkinManager");
			ModuleManager.loadModule(this, SkinManager.class, true);
		}

		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll((Plugin) this);
	}
}