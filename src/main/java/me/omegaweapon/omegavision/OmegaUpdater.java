package me.omegaweapon.omegavision;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public abstract class OmegaUpdater extends BukkitRunnable {

	private static int projectId;
	private static String latestVersion = "";


	public OmegaUpdater(int projectId) {
		OmegaUpdater.projectId = projectId;
	}

	@Override
	public void run() {
		try {
			final URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectId);
			final URLConnection con = url.openConnection();

			try (BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				latestVersion = r.readLine();
			}

			if (isUpdateAvailable())
				onUpdateAvailable();

		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

	public abstract void onUpdateAvailable();

	public static boolean isUpdateAvailable() {
		return !latestVersion.equals(OmegaVision.getInstance().getDescription().getVersion());
	}

	public static String[] getUpdateMessage() {
		String prefix = ChatColor.translateAlternateColorCodes('&', OmegaVision.getInstance().getMessagesConfig().getString("Prefix"));
		final PluginDescriptionFile pdf = OmegaVision.getInstance().getDescription();

		return new String[] {
			prefix + OmegaVision.getInstance().getDescription().getName() + " has been updated!",
			prefix + "Your current version is: " + OmegaVision.getInstance().getDescription().getVersion(),
			prefix + "The latest version is: " + getLatestVersion(),
			prefix + "You can grab the update here: https://spigotmc.org/resources/" + getProjectId()
		};
	}

	public static int getProjectId() {
		return projectId;
	}

	public static String getLatestVersion() {
		return latestVersion;
	}
}