package net.novauniverse.novaextension.skin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.entity.Player;

import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinVariant;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.property.IProperty;
import net.zeeraa.novacore.spigot.module.NovaModule;

public class SkinManager extends NovaModule {
	private static SkinManager instance;

	private SkinsRestorerAPI skinsRestorerAPI;
	private Map<String, IProperty> cache;

	public static SkinManager getInstance() {
		return instance;
	}

	public SkinManager() {
		super("NovaExtension.SkinManager");
	}

	@Override
	public void onLoad() {
		SkinManager.instance = this;
		cache = new HashMap<>();
		skinsRestorerAPI = null;
	}

	@Override
	public void onDisable() throws Exception {
		cache.clear();
	}

	@Override
	public void onEnable() throws Exception {
		cache.clear();
		skinsRestorerAPI = SkinsRestorerAPI.getApi();
	}

	private IProperty getSkinProperty(String url, @Nullable SkinType type) throws SkinRequestException {
		String typeString = null;
		SkinVariant variant = null;
		String hash = null;

		if (type != null) {
			typeString = type.getTypeName();
			hash = DigestUtils.md5Hex(url) + "|" + typeString;
			variant = type.toVariant();
		} else {
			hash = DigestUtils.md5Hex(url);
		}

		if (cache.containsKey(hash)) {
			return cache.get(hash);
		} else {
			IProperty prop = skinsRestorerAPI.genSkinUrl(url, variant);

			cache.put(hash, prop);

			return prop;
		}
	}

	public void preloadSkin(String url) throws SkinRequestException {
		this.preloadSkin(url, null);
	}

	public void setSkin(Player player, String url) throws SkinRequestException {
		this.setSkin(player, url, null);
	}

	public void preloadSkin(String url, @Nullable SkinType type) throws SkinRequestException {
		this.getSkinProperty(url, type);
	}

	public void setSkin(Player player, String url, @Nullable SkinType type) throws SkinRequestException {
		IProperty props = getSkinProperty(url, type);
		skinsRestorerAPI.applySkin(new PlayerWrapper(player), props);
	}
}
