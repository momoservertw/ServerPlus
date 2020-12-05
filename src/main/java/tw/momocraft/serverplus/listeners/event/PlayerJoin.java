package tw.momocraft.serverplus.listeners.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.event.EventMap;
import tw.momocraft.serverplus.utils.event.EventUtils;

import java.util.Map;

import static tw.momocraft.serverplus.handlers.ConfigHandler.getConfigPath;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoinEvent(PlayerJoinEvent e) {
        ServerHandler.sendConsoleMessage(e.getEventName());
        if (!getConfigPath().isEvent()) {
            return;
        }
        ServerHandler.sendConsoleMessage("24");
        // Checking the event is enabled.
        Map<String, EventMap> eventProp = ConfigHandler.getConfigPath().getEventProp().get(e.getEventName());
        if (eventProp == null) {
            return;
        }
        ServerHandler.sendConsoleMessage("32");
        Player player = e.getPlayer();
        String triggerName = player.getName();
        ServerHandler.sendConsoleMessage(triggerName);
        if (EventUtils.checkEvent(triggerName, player, null, null, eventProp, e.getEventName())) {
            //e.setCancelled(true);
        }
    }
}
