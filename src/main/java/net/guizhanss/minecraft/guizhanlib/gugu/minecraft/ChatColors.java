package net.guizhanss.minecraft.guizhanlib.gugu.minecraft;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

/**
 * Chat colors ({@link ChatColor}) with their Chinese names
 *
 * @author ybw0014
 * @see ChatColor
 */
@SuppressWarnings({"ConstantConditions", "deprecation"})
@Getter
@RequiredArgsConstructor
public enum ChatColors {
    /**
     * Aqua
     */
    AQUA(ChatColor.AQUA, "天蓝色"),
    /**
     * Black
     */
    BLACK(ChatColor.BLACK, "黑色"),
    /**
     * Blue
     */
    BLUE(ChatColor.BLUE, "蓝色"),
    /**
     * Bold
     */
    BOLD(ChatColor.BOLD, "加粗"),
    /**
     * Dark Aqua
     */
    DARK_AQUA(ChatColor.DARK_AQUA, "湖蓝色"),
    /**
     * Dark Blue
     */
    DARK_BLUE(ChatColor.DARK_BLUE, "深蓝色"),
    /**
     * Dark Gray
     */
    DARK_GRAY(ChatColor.DARK_GRAY, "深灰色"),
    /**
     * Dark Green
     */
    DARK_GREEN(ChatColor.DARK_GREEN, "深绿色"),
    /**
     * Dark Purple
     */
    DARK_PURPLE(ChatColor.DARK_PURPLE, "紫色"),
    /**
     * Dark Red
     */
    DARK_RED(ChatColor.DARK_RED, "深红色"),
    /**
     * Gold
     */
    GOLD(ChatColor.GOLD, "金色"),
    /**
     * Gray
     */
    GRAY(ChatColor.GRAY, "灰色"),
    /**
     * Green
     */
    GREEN(ChatColor.GREEN, "绿色"),
    /**
     * Italic
     */
    ITALIC(ChatColor.ITALIC, "斜体"),
    /**
     * Light Purple
     */
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, "粉红色"),
    /**
     * Magic
     */
    MAGIC(ChatColor.MAGIC, "随机"),
    /**
     * Red
     */
    RED(ChatColor.RED, "红色"),
    /**
     * Reset
     */
    RESET(ChatColor.RESET, "重置"),
    /**
     * Strikethrough
     */
    STRIKETHROUGH(ChatColor.STRIKETHROUGH, "删除线"),
    /**
     * Underline
     */
    UNDERLINE(ChatColor.UNDERLINE, "下划线"),
    /**
     * White
     */
    WHITE(ChatColor.WHITE, "白色"),
    /**
     * Yellow
     */
    YELLOW(ChatColor.YELLOW, "黄色");

    /**
     * Color code prefix
     */
    public static final char COLOR_CHAR = '§';

    private static final ChatColors[] valuesCache = values();
    private static final Map<ChatColor, ChatColors> colorLookup = new EnumMap<>(ChatColor.class);

    static {
        for (ChatColors color : valuesCache) {
            colorLookup.put(color.getColor(), color);
        }
    }

    private final ChatColor color;
    private final String chinese;

    /**
     * Returns the enum constant matching the given chat color
     *
     * @param chatColor {@link ChatColor} the chat color
     * @return the matching enum constant
     */
    @Nonnull
    public static ChatColors fromChatColor(@Nonnull ChatColor chatColor) {
        Preconditions.checkArgument(chatColor != null, "Chat color cannot be null");

        return colorLookup.get(chatColor);
    }

    /**
     * Gets the Chinese name
     *
     * @return the Chinese name
     */
    @Override
    public String toString() {
        return this.getChinese();
    }

    /**
     * Gets the Chinese color name prefixed with its color code
     *
     * @return the colored color name
     */
    public String toColoredString() {
        return this.getColor() + this.getChinese();
    }
}
