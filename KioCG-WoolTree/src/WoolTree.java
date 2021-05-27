import org.bukkit.plugin.java.JavaPlugin;

public class WoolTree extends JavaPlugin {
    @SuppressWarnings("unused")
    public static WoolTree instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
