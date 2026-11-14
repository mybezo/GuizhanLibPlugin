package net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.entity;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

/**
 * {@link Entity} Chinese name lookup
 *
 * @author ybw0014
 */
@UtilityClass
@SuppressWarnings({"ConstantConditions", "unused", "deprecation"})
public final class EntityHelper {

    @Nonnull
    public static String getDisplayName(@Nonnull Entity instance) {
        Preconditions.checkNotNull(instance);
        return instance.getCustomName() == null ? getName(instance) : instance.getCustomName();
    }

    @Nonnull
    public static String getName(@Nonnull Entity instance) {
        Preconditions.checkNotNull(instance);
        return EntityTypeHelper.getName(instance.getType());
    }
}
