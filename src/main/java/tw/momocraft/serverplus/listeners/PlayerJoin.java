package tw.momocraft.serverplus.listeners;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.utils.ItemJoinMap;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoinEvent(PlayerJoinEvent e) {
        if (!ConfigHandler.getDepends().ItemJoinEnabled()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isItemjoin()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isIjFixOldItem()) {
            return;
        }
        Map<String, List<ItemJoinMap>> ijProp = ConfigHandler.getConfigPath().getIjProp();
        Set<String> ijKeys = ijProp.keySet();

        Player player = e.getPlayer();
        ItemStack slotItem;
        String itemType;
        ItemMeta itemMeta;
        ItemJoinAPI itemJoinAPI = new ItemJoinAPI();
        for (int i = 0; i <= 35; i++) {
            slotItem = player.getInventory().getItem(i);
            // If itemJoin is available.
            if (itemJoinAPI.isCustom(slotItem)) {
                continue;
            }
            itemType = slotItem.getType().name();
            if (!ijKeys.contains(slotItem.getType().name())) {
                continue;
            }
            itemMeta = slotItem.getItemMeta();
            for (ItemJoinMap ijMap : ijProp.get(itemType)) {
                try {
                    if (!itemMeta.getDisplayName().contains(ijMap.getName())) {
                        continue;
                    }
                    if (!itemMeta.getLore().contains(ijMap.getLore())) {
                        continue;
                    }
                } catch (Exception ex) {
                    continue;
                }
                slotItem.setType(Material.AIR);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ij get " + player.getName() + " " + ijMap.getItemNode());
            }
        }
    }
}
