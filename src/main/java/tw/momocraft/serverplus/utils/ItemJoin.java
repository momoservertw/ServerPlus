package tw.momocraft.serverplus.utils;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemJoin {
    public static void itemJoinFix(Player player, boolean msg) {
        Map<String, List<ItemJoinMap>> ijProp = ConfigHandler.getConfigPath().getIjProp();
        Set<String> ijKeys = ijProp.keySet();

        ItemStack slotItem;
        String itemType;
        ItemMeta itemMeta;
        int fixAmount = 0;
        String itemNode;
        int amount;
        boolean oneMenu = ConfigHandler.getConfigPath().isIjOneMenu();
        boolean hasMenu = false;
        for (int i = 0; i <= 35; i++) {
            slotItem = player.getInventory().getItem(i);
            try {
                itemMeta = slotItem.getItemMeta();
            } catch (Exception ex) {
                continue;
            }
            itemType = slotItem.getType().name();
            // If itemJoin is available.
            if (CorePlusAPI.getDependManager().ItemJoinEnabled()) {
                if (CorePlusAPI.getUtilsManager().isCustomItem(slotItem)) {
                    if (oneMenu) {
                        if (CorePlusAPI.getUtilsManager().isMenu(slotItem)) {
                            try {
                                if (hasMenu) {
                                    player.getInventory().setItem(i, null);
                                    continue;
                                }
                                hasMenu = true;
                            } catch (Exception ex) {
                                continue;
                            }
                        }
                        continue;
                    }
                }
            }
            // Contains replace itemType.
            if (!ijKeys.contains(slotItem.getType().name())) {
                continue;
            }
            for (ItemJoinMap ijMap : ijProp.get(itemType)) {
                try {
                    // Check item name.
                    if (!itemMeta.getDisplayName().equals(ijMap.getName())) {
                        continue;
                    }
                } catch (Exception ex) {
                    continue;
                }
                amount = slotItem.getAmount();
                fixAmount += amount;
                // Remove item and get the new item again.
                player.getInventory().setItem(i, null);
                itemNode = ijMap.getItemNode();
                if (CorePlusAPI.getUtilsManager().isMenuNode(itemNode)) {
                    if (hasMenu) {
                        player.getInventory().setItem(i, null);
                        continue;
                    }
                    hasMenu = true;
                }
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ij get " + itemNode + " " + player.getName() + " " + amount);
                CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPlugin(), "ItemJoin", itemNode, "give", "continue",
                        new Throwable().getStackTrace()[0]);
            }
        }
        String[] placeHolders = CorePlusAPI.getLangManager().newString();
        placeHolders[6] = String.valueOf(fixAmount); // %amount%
        if (msg) {
            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgItemJoinFix(), player, placeHolders);
        }
    }
}
