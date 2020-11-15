package tw.momocraft.serverplus.utils;

import at.pcgamingfreaks.MarriageMaster.API.MarriageMasterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class MarriageMaster {
    public static MarriageMasterPlugin getMarriageMaster() {
        Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("MarriageMaster");
        if(!(bukkitPlugin instanceof MarriageMasterPlugin)) {
            // Do something if MarriageMaster is not available
            return null;
        }
        return (MarriageMasterPlugin) bukkitPlugin;
    }
}
