package tw.momocraft.serverplus.listeners;

import net.craftersland.data.bridge.api.events.SyncCompleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;


public class SyncComplete implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onSyncCompleteEvent(SyncCompleteEvent e) {
        if (!ConfigHandler.getConfigPath().isMpdb()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isMpdbSyncComplete()) {
            return;
        }
        CorePlusAPI.getCommandManager().executeCmdList(ConfigHandler.getPrefix(), e.getPlayer(), ConfigHandler.getConfigPath().getMpdbSyncCompleteCmds(), true);
    }
}
