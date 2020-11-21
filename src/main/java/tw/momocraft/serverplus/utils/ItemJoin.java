package tw.momocraft.serverplus.utils;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemJoin {
    public static void itemJoinFix(Player player, boolean msg) {
        Map<String, List<ItemJoinMap>> ijProp = ConfigHandler.getConfigPath().getIjProp();
        Set<String> ijKeys = ijProp.keySet();

        ItemJoinAPI itemJoinAPI = new ItemJoinAPI();
        ItemStack slotItem;
        String itemType;
        ItemMeta itemMeta;
        int fixAmount = 0;
        String itemNode;
        int amount;
        for (int i = 0; i <= 35; i++) {
            slotItem = player.getInventory().getItem(i);
            // If itemJoin is available.
            if (itemJoinAPI.isCustom(slotItem)) {
                continue;
            }
            try {
                itemType = slotItem.getType().name();
            } catch (Exception ex) {
                continue;
            }
            // Contains replace itemType.
            if (!ijKeys.contains(slotItem.getType().name())) {
                continue;
            }
            itemMeta = slotItem.getItemMeta();
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
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ij get " + itemNode + " " + player.getName() + " " + amount);
                ServerHandler.sendFeatureMessage("ItemJoin", itemNode, "give", "continue",
                        new Throwable().getStackTrace()[0]);
            }
        }
        String[] placeHolders = Language.newString();
        placeHolders[6] = String.valueOf(fixAmount);
        if (msg) {
            Language.sendLangMessage("Message.ServerPlus.ItemJoinFix", player, placeHolders);
        }
    }
}
