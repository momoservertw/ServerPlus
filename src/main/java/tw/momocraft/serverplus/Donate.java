package tw.momocraft.serverplus;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.utils.DonateMap;

import java.util.UUID;

public class Donate {

    public static void promote(CommandSender sender, Player target, String updateGroup) {
        DonateMap donateMap = ConfigHandler.getConfigPath().getDonateProp().get(updateGroup);
        if (donateMap == null) {
            CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, ConfigHandler.getConfigPath().getMsgDonateGroupNotFound());
            return;
        }
        Player player;
        if (target != null) {
            player = target;
        } else {
            player = (Player) sender;
        }
        UUID uuid = player.getUniqueId();
        if (!donateMap.getGroup().equals(CorePlusAPI.getPlayerManager().getPrimaryGroup(uuid))) {
            CorePlusAPI.getCommandManager().executeCmdList(ConfigHandler.getPrefix(), player, donateMap.getFailedCommands(), true);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                    "Donate", player.getName(), "group", "fail", updateGroup, new Throwable().getStackTrace()[0]);
            return;
        }
        CorePlusAPI.getCommandManager().executeCmdList(ConfigHandler.getPrefix(), player, donateMap.getCommands(), true);
        CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(),
                "Donate", player.getName(), "group", "success", updateGroup, new Throwable().getStackTrace()[0]);
    }
}
