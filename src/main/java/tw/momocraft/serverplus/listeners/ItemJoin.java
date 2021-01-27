package tw.momocraft.serverplus.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.ServerPlus;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.utils.ItemJoinFixMap;

import java.util.List;

public class ItemJoin implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onFixItem(InventoryClickEvent e) {
        if (!ConfigHandler.getConfigPath().isItemjoin()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isItemjoinFix()) {
            return;
        }
        if (!CorePlusAPI.getDependManager().ItemJoinEnabled()) {
            return;
        }
        Inventory inventory = e.getClickedInventory();
        try {
            if (inventory.getType() != InventoryType.CHEST && inventory.getType() != InventoryType.ENDER_CHEST &&
                    inventory.getType() != InventoryType.SHULKER_BOX && inventory.getType() != InventoryType.PLAYER) {
                return;
            }
        } catch (Exception ex) {
            return;
        }
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null) {
            return;
        }
        ItemStack newItemStack = itemJoinFix((Player) e.getWhoClicked(), itemStack);
        if (itemStack.equals(newItemStack)) {
            return;
        }
        e.setCurrentItem(newItemStack);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerJoinEvent(PlayerJoinEvent e) {
        if (!ConfigHandler.getConfigPath().isItemjoin()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isItemjoinFix()) {
            return;
        }
        if (!CorePlusAPI.getDependManager().ItemJoinEnabled()) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = e.getPlayer();
                Inventory inventory = player.getInventory();
                ItemStack itemStack;
                ItemStack newItemStack;
                for (int i = 0; i <= 35; i++) {
                    itemStack = inventory.getItem(i);
                    if (itemStack == null) {
                        continue;
                    }
                    newItemStack = itemJoinFix(player, inventory.getItem(i));
                    if (itemStack.equals(newItemStack)) {
                        continue;
                    }
                    inventory.setItem(i, newItemStack);
                }
            }
        }.runTaskLater(ServerPlus.getInstance(), 120);
    }

    public static ItemStack itemJoinFix(Player player, ItemStack itemStack) {
        ItemMeta itemMeta;
        String itemName;
        try {
            itemMeta = itemStack.getItemMeta();
            itemName = itemMeta.getDisplayName();
        } catch (Exception ex) {
            return itemStack;
        }
        // Skip custom items.
        if (CorePlusAPI.getUtilsManager().isMenu(itemStack)) {
            return itemStack;
        }
        String itemType = itemStack.getType().name();
        List<ItemJoinFixMap> ijProp = ConfigHandler.getConfigPath().getItemjoinFixProp().get(itemType);
        if (ijProp == null) {
            return itemStack;
        }
        for (ItemJoinFixMap itemJoinMap : ijProp) {
            if (!itemJoinMap.getType().equals(itemType)) {
                continue;
            }
            if (!itemJoinMap.getNames().contains(itemName)) {
                continue;
            }
            String itemNode = itemJoinMap.getItemNode();
            // Replace the old item to new one.
            ItemStack newItemStack = CorePlusAPI.getUtilsManager().getItemJoinItemStack(player, itemNode);
            newItemStack.setAmount(itemStack.getAmount());
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "ItemFix, [ItemFix] " + player.getName() + " - " + itemNode + " x" + itemStack.getAmount());
            CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPluginName(),
                    "ItemJoin", player.getName(), "Fix-Old-Item", "success", itemNode, new Throwable().getStackTrace()[0]);
            return newItemStack;
        }
        return itemStack;
    }

    public static void itemJoinFixConfig() {
        ConfigurationSection config = ConfigHandler.getConfig("temporary.yml").getConfigurationSection("items");
        for (String itemNode : config.getKeys(false)) {
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "ServerPlus,      " + itemNode + ":");
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "ServerPlus,        ItemNode: " + itemNode);
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(),
                    "ServerPlus,        Type: " + ConfigHandler.getConfig("temporary.yml").getString("items." + itemNode + ".id"));
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(),
                    "ServerPlus,        Names:");
            CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(),
                    "ServerPlus,          - \"" + ConfigHandler.getConfig("temporary.yml").getString("items." + itemNode + ".name") + "\"");
        }
    }
}
