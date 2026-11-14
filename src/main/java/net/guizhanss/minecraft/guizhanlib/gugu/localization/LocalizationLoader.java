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

    private static final String LANGUAGE = "en_us";
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

        localeFile = new File(GuizhanLib.getInstance().getDataFolder(), "minecraft-lang/" + fullVersion + "_" + LANGUAGE + ".json");

        prepareFile();
        loadFile();
    }

    public Map<String, String> getResult() {
        return Collections.unmodifiableMap(lang);
    }

    private void prepareFile() {
        logger.log(Level.INFO, () -> "Loading Minecraft localization file (" + LANGUAGE + ")");
        logger.log(Level.INFO, () -> "Current version: " + fullVersion);

        if (localeFile.exists()) {
            return;
        }

        // 1. The server jar ships the English language file of the running version.
        if (prepareFromServerJar()) {
            return;
        }

        // 2. Download it from the minecraft-assets mirror.
        final String remoteUrl = "https://cdn.jsdelivr.net/gh/InventivetalentDev/minecraft-assets@" + fullVersion + "/assets/minecraft/lang/" + LANGUAGE + ".json";

        try {
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
                throw new IOException("HTTP status " + status);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Thread was interrupted while downloading the localization file", ie);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while downloading Minecraft localization resources", e);
            logger.log(Level.INFO, () -> "You can download the localization file manually and place it at the location below, so the plugin can use it on the next load.");
            logger.log(Level.INFO, () -> "Download URL: " + remoteUrl);
            logger.log(Level.INFO, () -> "Place it at: " + localeFile.getAbsolutePath());
        }
    }

    private boolean prepareFromServerJar() {
        final String path = "assets/minecraft/lang/" + LANGUAGE + ".json";
        ClassLoader[] loaders = {
            Bukkit.getServer().getClass().getClassLoader(),
            Thread.currentThread().getContextClassLoader(),
            GuizhanLib.class.getClassLoader()
        };

        for (ClassLoader loader : loaders) {
            if (loader == null) {
                continue;
            }
            try {
                InputStream input = loader.getResourceAsStream(path);
                if (input != null) {
                    saveToFile(input);
                    logger.log(Level.INFO, "Loaded the localization file from the server jar");
                    return true;
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to read the localization file from the server jar", e);
            }
        }
        return false;
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
        if (!localeFile.exists()) {
            logger.log(Level.WARNING, "No Minecraft localization file available, translation keys will be used as names");
            return;
        }

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
