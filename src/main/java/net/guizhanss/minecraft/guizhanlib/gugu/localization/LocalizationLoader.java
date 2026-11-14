package net.guizhanss.minecraft.guizhanlib.gugu.localization;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.guizhanss.minecraft.guizhanlib.GuizhanLib;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Localization file loader
 *
 * @author ybw0014
 */
public final class LocalizationLoader {

    private static final Gson GSON = new Gson();
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();

    private final Map<String, String> lang = new HashMap<>();
    private final Logger logger;
    private final String fullVersion;
    private final File localeFile;

    public LocalizationLoader() {
        logger = GuizhanLib.getInstance().getLogger();
        fullVersion = Bukkit.getServer().getMinecraftVersion();

        // create minecraft-lang folder if not exists
        File langFolder = new File(GuizhanLib.getInstance().getDataFolder(), "minecraft-lang");
        if (!langFolder.exists()) {
            boolean created = langFolder.mkdirs();
            if (!created && !langFolder.exists()) {
                logger.log(Level.WARNING, () -> "Failed to create directory: " + langFolder.getAbsolutePath());
            }
        }

        localeFile = new File(GuizhanLib.getInstance().getDataFolder(), "minecraft-lang/" + fullVersion + ".json");

        prepareFile();
        loadFile();
    }

    public Map<String, String> getResult() {
        return Collections.unmodifiableMap(lang);
    }

    private void prepareFile() {
        logger.log(Level.INFO, () -> "Loading Minecraft localization file");
        logger.log(Level.INFO, () -> "Current version: " + fullVersion);

        final String remoteUrl = "https://cdn.jsdelivr.net/gh/InventivetalentDev/minecraft-assets@" + fullVersion + "/assets/minecraft/lang/zh_cn.json";

        try {
            if (!localeFile.exists()) {
                logger.log(Level.INFO, () -> "Localization file for the current version does not exist, downloading (times out after 15 seconds)");

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(remoteUrl))
                    .GET()
                    .timeout(Duration.ofSeconds(15))
                    .build();

                HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());

                int status = response.statusCode();
                if (status >= 200 && status < 300) {
                    try (InputStream inputStream = response.body()) {
                        saveToFile(inputStream);
                    }

                    logger.log(Level.INFO, () -> "Downloaded the localization file for the current version");
                } else {
                    throw new RuntimeException("HTTP status " + status);
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Thread was interrupted while downloading, trying the backup localization file", ie);
            prepareBackupFile();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while loading Minecraft localization resources, trying the fallback", e);
            logger.log(Level.INFO, () -> "You can download the localization file manually and place it at the location below, so the plugin can use it on the next load.");
            logger.log(Level.INFO, () -> "Download URL: " + remoteUrl);
            logger.log(Level.INFO, () -> "Place it at: " + localeFile.getAbsolutePath());
            prepareBackupFile();
        }
    }

    private void prepareBackupFile() {
        logger.log(Level.INFO, "Loading the bundled backup Minecraft localization file (may not be the latest for the current version)");
        try {
            final String[] versionParts = fullVersion.split("\\.");
            int majorVersion = Integer.parseInt(versionParts[0]);
            int minorVersion = versionParts.length > 1 ? Integer.parseInt(versionParts[1]) : 0;

            InputStream input;
            String loadedVersion = null;

            // Year-based versions (26.x+)
            if (majorVersion >= 26) {
                int yearVersion = majorVersion;
                while (yearVersion >= 26) {
                    String versionToTry = yearVersion + ".1";
                    final String filename = "/minecraft-lang/" + versionToTry + "/zh_cn.json";
                    input = GuizhanLib.getInstance().getClass().getResourceAsStream(filename);
                    if (input != null) {
                        saveToFile(input);
                        loadedVersion = versionToTry;
                        break;
                    }
                    yearVersion--;
                }

                if (loadedVersion == null) {
                    minorVersion = 21;
                }
            }

            // Legacy versions (1.x)
            if (loadedVersion == null) {
                int mcVersion = (majorVersion == 1) ? minorVersion : 21;
                while (mcVersion >= 18) {
                    final String filename = "/minecraft-lang/1." + mcVersion + "/zh_cn.json";
                    input = GuizhanLib.getInstance().getClass().getResourceAsStream(filename);
                    if (input != null) {
                        saveToFile(input);
                        loadedVersion = "1." + mcVersion;
                        break;
                    }
                    mcVersion--;
                }
            }

            if (loadedVersion != null) {
                logger.log(Level.INFO, "Loaded backup localization file: " + loadedVersion);
            } else {
                logger.log(Level.WARNING, "No usable backup localization file found");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load the bundled backup Minecraft localization resources", e);
        }
    }

    private void saveToFile(InputStream inputStream) throws IOException {
        // ensure parent folder exists
        File parent = localeFile.getParentFile();
        if (parent != null && !parent.exists()) {
            boolean created = parent.mkdirs();
            if (!created && !parent.exists()) {
                logger.log(Level.WARNING, () -> "Failed to create the parent directory of the localization file: " + parent.getAbsolutePath());
            }
        }

        try (InputStream in = inputStream; FileOutputStream output = new FileOutputStream(localeFile)) {
            byte[] data = new byte[1024];
            int read;

            while ((read = in.read(data, 0, data.length)) != -1) {
                output.write(data, 0, read);
            }
            output.flush();
        }
    }

    private void loadFile() {
        try (FileInputStream stream = new FileInputStream(localeFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                 stream, StandardCharsets.UTF_8
             ))) {
            // @formatter:off
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            // @formatter:on
            lang.putAll(GSON.fromJson(reader, type));

            logger.log(Level.INFO, "Loaded successfully");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "An error occurred while loading the Minecraft localization file", ex);
        }
    }
}
