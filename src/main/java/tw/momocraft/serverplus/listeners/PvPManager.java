package tw.momocraft.serverplus.listeners;

import me.NoChance.PvPManager.Events.PlayerTagEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

public class PvPManager implements Listener {

    /*
    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerTagEvent(PlayerTagEvent e) {
        CorePlusAPI.getLangManager().sendBroadcastMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgPvpOff());
        e.getPlayer().getName();
        e.getPvPlayer()
        if (!ConfigHandler.getConfigPath().isMpdb()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isMpdbSyncComplete()) {
            return;
        }
        CorePlusAPI.getCommandManager().executeCmdList(ConfigHandler.getPrefix(), e.getPlayer(), ConfigHandler.getConfigPath().getMpdbSyncCompleteCmds(), true);
    }*/
}