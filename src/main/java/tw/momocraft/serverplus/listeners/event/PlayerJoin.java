package tw.momocraft.serverplus.listeners.event;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.Utils;
import tw.momocraft.serverplus.utils.event.ActionMap;
import tw.momocraft.serverplus.utils.event.ConditionMap;
import tw.momocraft.serverplus.utils.event.EventMap;


import java.util.List;
import java.util.Map;

import static tw.momocraft.serverplus.handlers.ConfigHandler.getConfigPath;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoinEvent(PlayerJoinEvent e) {
        if (!getConfigPath().isEvent()) {
            return;
        }
        Map<String, EventMap> eventProp = ConfigHandler.getConfigPath().getEventProp().get("PlayerJoinEvent");
        if (eventProp == null) {
            return;
        }
        Player player = e.getPlayer();
        String playerName = player.getName();

        String targetName = null;
        Block targetBlock = null;
        Entity targetEntity = null;

        EventMap eventMap;
        Map<String, List<String>> targetMap;
        eventFor:
        for (String group : eventProp.keySet()) {
            eventMap = eventProp.get(group);
            targetMap = eventMap.getTargets();
            if (targetMap != null) {
                targetFor:
                for (String type : targetMap.keySet()) {
                    switch (type) {
                        case "Player":
                            if (targetEntity instanceof Player) {
                                try {
                                    targetName = ((Player) targetEntity).getName();
                                } catch (Exception ignored) {
                                }
                                continue targetFor;
                            }
                            break;
                            /*
                        case "MyPet":
                            if (ConfigHandler.getDepends().MyPetEnabled()) {
                                MyPetApi.getPlayerManager().get
                                continue targetFor;
                            }
                            break;
                             */
                        case "Blocks":
                            try {
                                if (targetMap.get(type).contains(targetBlock.getType().name())) {
                                    targetName = type;
                                    continue targetFor;
                                }
                            } catch (Exception ex) {
                                continue eventFor;
                            }
                            break;
                        case "Entities":
                            try {
                                if (targetMap.get(type).contains(targetEntity.getType().name())) {
                                    targetName = type;
                                    continue targetFor;
                                }
                            } catch (Exception ex) {
                                continue eventFor;
                            }
                            break;
                    }
                }
            }
            // Checking conditions.
            String condition = checkConditions(player, targetName, eventMap.getConditions());
            if (condition.equals("false")) {
                continue;
            }
            // Executing actions.
            List<ActionMap> actionMapList = eventMap.getActions();
            if (actionMapList == null) {
                ServerHandler.sendErrorMessage("Can not find the Action settings: " + group);
                return;
            }
            for (ActionMap actionMap : actionMapList) {
                if (actionMap.getCancel() != null) {
                    // e.setCancelled(true);
                }
                if (actionMap.getCleanSlots() != null) {
                    for (String slot : actionMap.getCleanSlots()) {
                        // Inventory
                        if (slot.matches("0-9")) {
                            try {
                                player.getInventory().setItem(Integer.parseInt(slot), null);
                            } catch (Exception ex) {
                                ServerHandler.sendErrorMessage("Can not find the Slot type: " + slot);
                            }
                        }
                        switch (slot) {
                            // Equipment
                            case "HEAD":
                                player.getInventory().setHelmet(null);
                                break;
                            case "CHEST":
                                player.getInventory().setChestplate(null);
                                break;
                            case "LEGS":
                                player.getInventory().setLeggings(null);
                                break;
                            case "FEET":
                                player.getInventory().setBoots(null);
                                break;
                            case "HAND":
                                player.getInventory().setItemInMainHand(null);
                                break;
                            case "OFF_HAND":
                                player.getInventory().setItemInOffHand(null);
                                break;
                            //
                            case "CRAFTING[1]":
                            case "CRAFTING[2]":
                            case "CRAFTING[3]":
                            case "CRAFTING[4]":
                                try {
                                    player.getOpenInventory().getTopInventory().setItem(Integer.parseInt(slot.replace("CRAFTING[", "").replace("]", "")), null);
                                } catch (Exception ex) {
                                    ServerHandler.sendErrorMessage("Can not find the Slot type: " + slot);
                                }
                            default:
                                ServerHandler.sendErrorMessage("Can not find the Slot type: " + slot);
                        }
                    }
                }
            }
        }
    }

    private String checkConditions(Player player, String targetName, List<ConditionMap> conditionMapList) {
        if (conditionMapList == null) {
            return "";
        }
        List<ConditionMap> conditionValueMapsList;
        for (ConditionMap conditionMap : conditionMapList) {
            if (conditionMap.getHoldingMenu() != null) {
                if (!checkHoldingMenu(player)) {
                    continue;
                }
            }
            if (conditionMap.getPlaceholders() != null) {
                if (!checkPlaceholders(player, targetName)) {
                    continue;
                }
            }
            String condition;
            conditionValueMapsList = conditionMap.getSucceedMap();
            if (conditionValueMapsList != null) {
                condition = checkConditions(player, targetName, conditionValueMapsList);
                if (condition.equals("") || condition.equals("true")) {
                    return "true";
                }
            }
            conditionValueMapsList = conditionMap.getFailedMap();
            if (conditionValueMapsList != null) {
                condition = checkConditions(player, targetName, conditionValueMapsList);
                if (condition.equals("") || condition.equals("true")) {
                    return "false";
                }
            }
            return "";
        }
        return "";
    }

    private boolean checkHoldingMenu(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        // Holding ItemJoin menu.
        if (ConfigHandler.getDepends().ItemJoinEnabled()) {
            ItemJoinAPI itemJoinAPI = new ItemJoinAPI();
            String menuIJ = ConfigHandler.getConfigPath().getMenuIJ();
            if (!menuIJ.equals("")) {
                if (itemJoinAPI.getNode(itemStack) != null) {
                    return itemJoinAPI.getNode(itemStack).equals(menuIJ);
                }
                return false;
            }
        }
        // Holding a menu item.
        if (itemStack.getType().name().equals(ConfigHandler.getConfigPath().getMenuType())) {
            String itemName;
            try {
                itemName = itemStack.getItemMeta().getDisplayName();
            } catch (Exception ex) {
                itemName = "";
            }
            String menuName = ConfigHandler.getConfigPath().getMenuName();
            return menuName.equals("") || itemName.equals(Utils.translateColorCode(menuName));
        }
        return false;
    }
}
