package tw.momocraft.serverplus.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.SmithingInventory;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.util.HashMap;
import java.util.Map;


public class MorphTool implements Listener {

    private Map<String, Long> cdList = new HashMap<>();

    /**
     * Update the MorphTool.
     *
     * @param e PrepareSmithingEvent
     */
    @EventHandler(priority = EventPriority.HIGH)
    private void onPrepareSmithingEvent(PrepareSmithingEvent e) {
        if (!ConfigHandler.getDepends().MorphToolEnabled()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isMorphtool()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isMorphtoolNetherite()) {
            return;
        }
        // Bypass not finish event.
        SmithingInventory inv = e.getInventory();
        if (inv.getItem(2) == null) {
            return;
        }
        // Item name equal MorphTool
        String itemName;
        try {
            itemName = inv.getItem(0).getItemMeta().getDisplayName();
            if (!itemName.equals(ConfigHandler.getConfigPath().getMorphtoolName())) {
                return;
            }
        } catch (Exception ex) {
            return;
        }
        // Update Item equal NETHERITE_INGOT
        Player player = (Player) e.getView().getPlayer();
        try {
            if (inv.getItem(1).getType().equals(Material.NETHERITE_INGOT)) {
                e.setResult(null);
                if (onCD(player)) {
                    return;
                }
                addCD(player);
                CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.MorphToolNetherite", player);
                CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPrefix(), "MorphTool", player.getName(), "update", "cancel",
                        new Throwable().getStackTrace()[0]);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Rename the MorphTool.
     *
     * @param e PrepareAnvilEvent
     */
    @EventHandler(priority = EventPriority.HIGH)
    private void onPrepareAnvilEvent(PrepareAnvilEvent e) {
        if (!ConfigHandler.getDepends().MorphToolEnabled()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isMorphtool()) {
            return;
        }
        // Bypass not finish event.
        AnvilInventory inv = e.getInventory();
        if (inv.getItem(2) == null) {
            return;
        }
        String itemName;
        try {
            itemName = inv.getItem(0).getItemMeta().getDisplayName();
        } catch (Exception ex) {
            return;
        }
        if (!itemName.equals(ConfigHandler.getConfigPath().getMorphtoolName())) {
            return;
        }
        // Item name equal MorphTool
        Player player = (Player) e.getView().getPlayer();
        try {
            if (!itemName.equals(inv.getItem(2).getItemMeta().getDisplayName()) ||
                    inv.getItem(2).getItemMeta().getDisplayName().equals("")) {
                e.setResult(null);
                if (onCD(player)) {
                    return;
                }
                addCD(player);
                CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.MorphToolRename", player);
                CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPrefix(), "MorphTool", player.getName(), "rename", "cancel",
                        new Throwable().getStackTrace()[0]);
            }
        } catch (Exception ex) {
            e.setResult(null);
            if (onCD(player)) {
                return;
            }
            addCD(player);
            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.MorphToolRename", player);
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPrefix(), "MorphTool", player.getName(), "rename", "cancel",
                    new Throwable().getStackTrace()[0]);
        }
    }

    private boolean onCD(Player player) {
        long playersCD = 0L;
        if (cdList.containsKey(player.getWorld().getName() + "." + player.getName())) {
            playersCD = cdList.get(player.getWorld().getName() + "." + player.getName());
        }
        // Tick(3) * cdMillis(50)
        return System.currentTimeMillis() - playersCD < 200;
    }

    private void addCD(Player player) {
        cdList.put(player.getWorld().getName() + "." + player.getName(), System.currentTimeMillis());
    }
}
