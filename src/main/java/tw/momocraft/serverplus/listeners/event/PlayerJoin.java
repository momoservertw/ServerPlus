package tw.momocraft.serverplus.listeners.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.MenuMap;
import tw.momocraft.serverplus.utils.Utils;
import tw.momocraft.serverplus.utils.event.EventMap;
import tw.momocraft.serverplus.utils.event.EventUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static tw.momocraft.serverplus.handlers.ConfigHandler.getConfigPath;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoinEvent(PlayerJoinEvent e) {
        ServerHandler.sendConsoleMessage(e.getEventName());
        if (!getConfigPath().isEvent()) {
            return;
        }
        // Checking the event is enabled.
        String eventName = e.getEventName();
        Map<String, EventMap> eventProp = ConfigHandler.getConfigPath().getEventProp().get(eventName);
        if (eventProp == null) {
            return;
        }
        Player player = e.getPlayer();
        if (EventUtils.checkEvent(player, null, null, null, eventProp, eventName)) {
            //e.setCancelled(true);
        }
    }
}
