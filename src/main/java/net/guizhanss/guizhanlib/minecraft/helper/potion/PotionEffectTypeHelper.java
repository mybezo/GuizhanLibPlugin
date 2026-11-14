package net.guizhanss.guizhanlib.minecraft.helper.potion;

import lombok.experimental.UtilityClass;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

/**
 * @deprecated Use {@link net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.potion.PotionEffectTypeHelper} instead
 */
@Deprecated(since = "2.0.0", forRemoval = true)
@UtilityClass
@SuppressWarnings("unused")
public final class PotionEffectTypeHelper {

    @Nonnull
    public static String getName(@Nonnull PotionEffectType potionEffectType) {
        return net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.potion.PotionEffectTypeHelper.getName(potionEffectType);
    }

    @Nonnull
    public static String getKey(@Nonnull PotionEffectType potionEffectType) {
        return net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.potion.PotionEffectTypeHelper.getKey(potionEffectType);
    }

    @Nonnull
    public static String getName(@Nonnull String potionEffectType) {
        return getName(potionEffectType, false);
    }

    @Nonnull
    public static String getName(@Nonnull String potionEffectType, boolean emptyString) {
        return net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.potion.PotionEffectTypeHelper.getName(potionEffectType, emptyString);
    }
}
