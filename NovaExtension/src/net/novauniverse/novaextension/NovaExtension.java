package net.novauniverse.novaextension;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.novauniverse.novaextension.abstraction.NovaExtensionAbstractionLayer;
import net.novauniverse.novaextension.skin.SkinManager;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.abstraction.NovaCoreAbstraction;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependantLoader;
import net.zeeraa.novacore.spigot.module.ModuleManager;

public class NovaExtension extends JavaPlugin implements Listener {
	private static NovaExtension instance;
	private NovaExtensionAbstractionLayer abstractionLayer;
	private List<NovaExtensionFeature> features;

	public static NovaExtension getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		NovaExtension.instance = this;
	}

	public NovaExtensionAbstractionLayer getAbstractionLayer() {
		return abstractionLayer;
	}

	public boolean hasFeature(NovaExtensionFeature feature) {
		return features.contains(feature);
	}

	@Override
	public void onEnable() {
		features = new ArrayList<>();

		String nmsVersion = NovaCoreAbstraction.getNMSVersion();
		Log.info("NovaExtension", "Server version: " + nmsVersion);

		try {
			Class<?> clazz = Class.forName("net.novauniverse.novaextension.version." + nmsVersion + ".AbstractionLayer");
			if (NovaExtensionAbstractionLayer.class.isAssignableFrom(clazz)) {
				this.abstractionLayer = (NovaExtensionAbstractionLayer) clazz.getConstructor().newInstance();
				this.abstractionLayer.setOwnerPlugin(this);
			} else {
				throw new InvalidClassException(clazz.getName() + " is not assignable from " + VersionIndependantLoader.class.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.fatal("NovaExtension", "Could not find support for this CraftBukkit version.");

			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		if (Bukkit.getServer().getPluginManager().getPlugin("SkinsRestorer") != null) {
			Log.info("NovaExtension", "Found SkinsRestorer. Enabling SkinManager");
			ModuleManager.loadModule(SkinManager.class, true);
		}

		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll((Plugin) this);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().split(" ")[0].equalsIgnoreCase("applyskin")) {
			try {
				Player player = Bukkit.getServer().getPlayer(e.getMessage().split(" ")[1]);

				((SkinManager) ModuleManager.getModule(SkinManager.class)).setSkin(player, e.getMessage().split(" ")[2]);
			} catch (Exception ex) {
				e.getPlayer().sendMessage(ChatColor.RED + ex.getClass().getName() + " " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}