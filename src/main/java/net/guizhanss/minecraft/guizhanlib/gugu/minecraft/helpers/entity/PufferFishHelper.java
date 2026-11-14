package net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.entity;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.PufferFish;

import javax.annotation.Nonnull;

/**
 * {@link PufferFish} helper
 */
@UtilityClass
@SuppressWarnings({"unused"})
public final class PufferFishHelper {

    @Nonnull
    public static String getPuffState(int level) {
        return switch (level) {
            case 0 -> "Not Inflated";
            case 1 -> "Half Inflated";
            case 2 -> "Fully Inflated";
            default -> "Unknown";
        };
    }
}
