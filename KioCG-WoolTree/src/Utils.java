import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    // 存储树苗位置的blockKey、羊毛种类列表
    public static final Map<Long, List<Material>> treeWools = new HashMap<>();

    public static @Nullable Material dye2wool(final @NotNull Material material) {
        return Material.getMaterial(material.toString().replace("_DYE", "_WOOL"));
    }
}
