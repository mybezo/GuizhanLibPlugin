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
     * Returns "是" (yes) or "否" (no) based on the given boolean value
     *
     * @param value a boolean value
     * @return {@link String} "是" (yes) or "否" (no)
     */
    @Nonnull
    public static String yesOrNo(boolean value) {
        return value ? "是" : "否";
    }

    /**
     * Returns "已启用" (enabled) or "已禁用" (disabled) based on the given boolean value
     *
     * @param value a boolean value
     * @return {@link String} "已启用" (enabled) or "已禁用" (disabled)
     */
    @Nonnull
    public static String enabledOrDisabled(boolean value) {
        return value ? "已启用" : "已禁用";
    }
}
