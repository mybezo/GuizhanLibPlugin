package net.guizhanss.minecraft.guizhanlib.gugu.localization;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Minecraft localization resource manager
 */
@SuppressWarnings("ConstantConditions")
public final class MinecraftLocalization {

    private static final Gson GSON = new Gson();

    private static MinecraftLocalization instance;

    private final Map<String, String> lang;

    private MinecraftLocalization() {
        lang = new LocalizationLoader().getResult();
    }

    /**
     * Loads the Minecraft localization resources. Should only be called when GuizhanLibPlugin is enabled.
     */
    public static void load() {
        if (instance != null) {
            throw new IllegalStateException("MinecraftLocalization has already been initialized");
        }

        instance = new MinecraftLocalization();
    }

    /**
     * Gets the value of the given key from the localization file
     *
     * @param key {@link String} the key
     * @return the value, or null if it does not exist
     */
    @Nullable
    public static String getOrNull(@Nonnull String key) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(instance, "MinecraftLocalization has not been initialized");

        return instance.lang.get(key);
    }

    /**
     * Gets the value of the given key from the localization file
     *
     * @param key        {@link String} the key
     * @param defaultVal the default value
     * @return the value, or the default value if it does not exist
     */
    @Nonnull
    public static String getOrDefault(@Nonnull String key, @Nonnull String defaultVal) {
        Preconditions.checkNotNull(defaultVal);

        String lang = getOrNull(key);
        return lang != null ? lang : defaultVal;
    }

    /**
     * Gets the value of the given key from the localization file
     *
     * @param key {@link String} the key
     * @return the value, or the key itself if it does not exist
     */
    @Nonnull
    public static String getOrKey(@Nonnull String key) {
        return getOrDefault(key, key);
    }
}
