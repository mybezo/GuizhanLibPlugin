package net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.entity;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.common.utils.StringUtil;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Cat.Type;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * {@link Cat} helper
 */
@UtilityClass
@SuppressWarnings({"ConstantConditions", "unused"})
public final class CatHelper {

    private static final Map<Type, String> TYPE_MAP = Map.ofEntries(
        Map.entry(Type.ALL_BLACK, "Black Cat"),
        Map.entry(Type.BLACK, "Tuxedo Cat"),
        Map.entry(Type.BRITISH_SHORTHAIR, "British Shorthair"),
        Map.entry(Type.CALICO, "Calico Cat"),
        Map.entry(Type.JELLIE, "Jellie"),
        Map.entry(Type.PERSIAN, "Persian Cat"),
        Map.entry(Type.RAGDOLL, "Ragdoll Cat"),
        Map.entry(Type.RED, "Red Tabby Cat"),
        Map.entry(Type.SIAMESE, "Siamese Cat"),
        Map.entry(Type.TABBY, "Tabby Cat"),
        Map.entry(Type.WHITE, "White Cat")
    );

    @Nonnull
    public static String getTypeName(@Nonnull Type type) {
        Preconditions.checkNotNull(type);
        return TYPE_MAP.getOrDefault(type, "Unknown");
    }

    @Nonnull
    public static String getTypeName(@Nonnull String type) {
        Preconditions.checkNotNull(type);
        try {
            Type inst = Type.valueOf(StringUtil.dehumanize(type));
            return getTypeName(inst);
        } catch (Exception ex) {
            return StringUtil.humanize(type);
        }
    }
}
