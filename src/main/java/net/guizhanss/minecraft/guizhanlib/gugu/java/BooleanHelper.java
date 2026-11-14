package net.guizhanss.minecraft.guizhanlib.gugu.java;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;

/**
 * {@link Boolean} helper
 *
 * @author ybw0014
 */
@UtilityClass
public final class BooleanHelper {

    /**
     * Returns "Yes" or "No" based on the given boolean value
     *
     * @param value a boolean value
     * @return {@link String} "Yes" or "No"
     */
    @Nonnull
    public static String yesOrNo(boolean value) {
        return value ? "Yes" : "No";
    }

    /**
     * Returns "Enabled" or "Disabled" based on the given boolean value
     *
     * @param value a boolean value
     * @return {@link String} "Enabled" or "Disabled"
     */
    @Nonnull
    public static String enabledOrDisabled(boolean value) {
        return value ? "Enabled" : "Disabled";
    }
}
