package tw.momocraft.serverplus.utils.event;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.CustomCommands;
import tw.momocraft.serverplus.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class EventUtils {

    public static boolean checkEvent(String triggerName, Player player, Entity targetEntity, Block targetBlock, Map<String, EventMap> eventProp, String eventName) {
        EventMap eventMap;
        for (String group : eventProp.keySet()) {
            eventMap = eventProp.get(group);
            // Target
            if (!EventUtils.checkTargets(targetEntity, targetBlock, eventMap.getTargets())) {
                ServerHandler.sendFeatureMessage("Event", triggerName, "Targets", "continue", eventName,
                        new Throwable().getStackTrace()[0]);
                continue;
            }
            // Conditions, Actions
            ActionMap actionMap;
            if (EventUtils.checkConditions(player, targetEntity, targetBlock, eventMap.getConditions(), eventName)) {
                actionMap = eventMap.getActions();
            } else {
                actionMap = eventMap.getActionsFailed();
            }
            if (executeActions(player, targetEntity, targetBlock, actionMap, eventName)) {
                return true;
            }
            ServerHandler.sendFeatureMessage("Event", triggerName, "final", "cancel", eventName,
                    new Throwable().getStackTrace()[0]);
            return false;
        }
        return false;
    }

    public static boolean checkTargets(Entity targetEntity, Block targetBlock, Map<String, List<String>> targetMap) {
        if (targetMap != null) {
            for (String type : targetMap.keySet()) {
                switch (type) {
                    case "Player":
                        if (targetEntity instanceof Player) {
                            try {
                                return true;
                            } catch (Exception ignored) {
                            }
                        }
                        break;
                    case "Blocks":
                        try {
                            if (targetMap.get(type).contains(targetBlock.getType().name())) {
                                return true;
                            }
                        } catch (Exception ignored) {
                        }
                        break;
                    case "Entities":
                        try {
                            if (targetMap.get(type).contains(targetEntity.getType().name())) {
                                return true;
                            }
                        } catch (Exception ignored) {
                        }
                        break;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * @param player       the player who triggered the event.
     * @param targetEntity the target Entity of this event.
     * @param targetBlock  the target Block of this event.
     * @param conditionMap the ConditionMap.
     * @return if the conditions are matched.
     */
    private static boolean checkConditions(Player player, Entity targetEntity, Block targetBlock, ConditionMap conditionMap, String eventName) {
        if (conditionMap == null) {
            return true;
        }
        List<String> placeholderList;
        if (conditionMap.getHoldingMenu() != null) {
            if (!checkHoldingMenu(player)) {
                return false;
            }
        }
        if (conditionMap.getBoimes() != null) {
            String boime = player.getLocation().getBlock().getBiome().name();
            if (Utils.containValue(boime, conditionMap.getBoimes(), conditionMap.getIgnoreBoimes())) {

            }
        }
        if (conditionMap.getReasons() != null) {
            String reason = "";
            if (Utils.containValue(reason, conditionMap.getReasons(), conditionMap.getIgnoreReasons())) {

            }
        }
        if (conditionMap.getLiquid() != null) {
            String reason = "";
            if (Utils.containValue(reason, conditionMap.getReasons(), conditionMap.getIgnoreReasons())) {

            }
        }
        // Checking the spawn location is "liquid" or not.
        if (!EntityUtils.isLiquid(block, entityMap.getLiquid())) {
            ServerHandler.sendFeatureMessage("Spawn", entityType, "Liquid", "continue", groupName,
                    new Throwable().getStackTrace()[0]);
            continue;
        }
        // Checking the spawn time is "Day" or not.
        if (!EntityUtils.isDay(loc.getWorld().getTime(), entityMap.getDay())) {
            ServerHandler.sendFeatureMessage("Spawn", entityType, "Day", "continue", groupName,
                    new Throwable().getStackTrace()[0]);
            continue;
        }
        placeholderList = conditionMap.getPlaceholders();
        if (placeholderList != null) {
            return checkPlaceholders(player, targetEntity, targetBlock, placeholderList, eventName);
        }
        return false;
    }

    private static boolean checkPlaceholders(Player player, Entity targetEntity, Block targetBlock, List<String> placeholderList, String eventName) {
        String[] placeholders;
        for (String placeholder : placeholderList) {
            placeholder = translateEventLayout(placeholder, player, targetEntity, targetBlock, eventName);
            ServerHandler.sendConsoleMessage(placeholder);
            if (placeholder.contains(";")) {
                placeholders = placeholder.split(";");
                for (String value : placeholders) {
                    if (!checkPlaceholderValue(value)) {
                        return false;
                    }
                }
            } else {
                if (!checkPlaceholderValue(placeholder)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkPlaceholderValue(String placeholder) {
        String[] values = placeholder.split("\\s+");
        if (values.length != 3) {
            ServerHandler.sendErrorMessage("There is an error occurred. Please check the \"Event - Conditions\" format.");
            ServerHandler.sendErrorMessage("Placeholder: " + placeholder);
            return true;
        }
        if (values[1].equals("=")) {
            return values[0].equals(values[2]);
        } else if (values[1].equals("!=")) {
            return !values[0].equals(values[2]);
        } else {
            return Utils.getCompare(values[1], Double.parseDouble(values[0]), Double.parseDouble(values[2]));
        }
    }

    /**
     * @param player       the player who triggered the event.
     * @param targetEntity the target Entity of this event.
     * @param targetBlock  the target Block of this event.
     * @param actionMap    the executing Actions.
     * @return if the event canceled.
     */
    private static boolean executeActions(Player player, Entity targetEntity, Block targetBlock, ActionMap actionMap, String eventName) {
        if (actionMap == null) {
            return false;
        }
        String playerName = player.getName();
        if (actionMap.getCommands() != null) {
            String command;
            for (String cmd : actionMap.getCommands()) {
                command = translateEventLayout(cmd, player, targetEntity, targetBlock, eventName);
                CustomCommands.executeMultipleCmds(player, command);
                ServerHandler.sendFeatureMessage("Lottery", playerName, "execute", "return", command,
                        new Throwable().getStackTrace()[0]);
            }
        }
        if (actionMap.getCleanSlots() != null) {
            cleanSlots(player, actionMap.getCleanSlots());
            ServerHandler.sendFeatureMessage("Lottery", playerName, "cleanSlots", "return",
                    new Throwable().getStackTrace()[0]);
        }
        return actionMap.getCancel() != null;
    }

    private static void cleanSlots(Player player, List<String> slots) {
        for (String slot : slots) {
            // Inventory
            if (slot.matches("0-9")) {
                try {
                    player.getInventory().setItem(Integer.parseInt(slot), null);
                } catch (Exception ex) {
                    ServerHandler.sendErrorMessage("Can not find the Slot type: " + slot);
                }
            }
            switch (slot) {
                // ALL
                case "ALL":
                    player.getInventory().clear();
                    break;
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

    private static boolean checkHoldingMenu(Player player) {
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

    private static String translateEventLayout(String input, Player player, Entity targetEntity, Block targetBlock, String eventName) {
        if (player != null) {
            input = Utils.translateLayout(input, player);
        }
        if (targetEntity != null) {
            if (targetEntity instanceof Player) {
                Player targetPlayer = (Player) targetEntity;
                input = translatePlayer(input, targetPlayer);
            }
            input = translateEntity(input, targetEntity);
        }
        if (targetBlock != null) {
            input = translateBlock(input, targetBlock);
        }
        // %event%
        try {
            input = input.replace("%event%", eventName);
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target% => CONSOLE
        if (targetEntity == null && targetBlock == null) {
            try {
                input = input.replace("%target%", "CONSOLE");
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        // %event%
        try {
            input = input.replace("%event%", eventName);
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %server_name%
        try {
            input = input.replace("%server_name%", Bukkit.getServer().getName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %localtime_time% => 2020/08/08 12:30:00
        try {
            input = input.replace("%localtime_time%", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %random_number%500%
        if (input.contains("%random_number%")) {
            try {
                String[] arr = input.split("%");
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equals("random_number") && arr[i + 1].matches("^[0-9]*$")) {
                        input = input.replace("%random_number%" + arr[i + 1] + "%", String.valueOf(new Random().nextInt(Integer.parseInt(arr[i + 1]))));
                    }
                }
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        // %random_player%
        if (input.contains("%random_player%")) {
            try {
                List<Player> playerList = new ArrayList(Bukkit.getOnlinePlayers());
                String randomPlayer = playerList.get(new Random().nextInt(playerList.size())).getName();
                input = input.replace("%random_player%", randomPlayer);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        // Translate color codes.
        input = ChatColor.translateAlternateColorCodes('&', input);
        // Translate PlaceHolderAPI's placeholders.
        if (ConfigHandler.getDepends().PlaceHolderAPIEnabled()) {
            try {
                return PlaceholderAPI.setPlaceholders(null, input);
            } catch (NoSuchFieldError e) {
                ServerHandler.sendDebugMessage("Error has occurred when setting the PlaceHolder " + e.getMessage() + ", if this issue persist contact the developer of PlaceholderAPI.");
                return input;
            }
        }
        return input;
    }

    private static String translateEntity(String input, Entity targetEntity) {
        // %target%
        try {
            input = input.replace("%target%", targetEntity.getName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_display_name%
        try {
            input = input.replace("%target_display_name%", targetEntity.getName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_uuid%
        try {
            input = input.replace("%target_uuid%", targetEntity.getUniqueId().toString());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_type%
        try {
            input = input.replace("%target_type%", targetEntity.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        Location loc = targetEntity.getLocation();
        // %target_world%
        if (input.contains("%target_world%")) {
            input = input.replace("%target_world%", loc.getWorld().getName());
        }
        // %target_loc%
        // %target_loc_x%, %player_loc_y%, %player_loc_z%
        // %target_loc_x_NUMBER%, %target_loc_y_NUMBER%, %target_loc_z_NUMBER%
        if (input.contains("%target_loc")) {
            try {
                String loc_x = String.valueOf(loc.getBlockX());
                String loc_y = String.valueOf(loc.getBlockY());
                String loc_z = String.valueOf(loc.getBlockZ());
                String[] arr = input.split("%");
                for (int i = 0; i < arr.length; i++) {
                    switch (arr[i]) {
                        case "target_loc_x":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_x%" + arr[i + 1] + "%", loc_x + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_y":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_y%" + arr[i + 1] + "%", loc_y + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_z":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_z%" + arr[i + 1] + "%", loc_z + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                    }
                }
                input = input.replace("%target_loc%", loc_x + ", " + loc_y + ", " + loc_z);
                input = input.replace("%target_loc_x%", loc_x);
                input = input.replace("%target_loc_y%", loc_y);
                input = input.replace("%target_loc_z%", loc_z);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        return input;
    }

    private static String translatePlayer(String input, Player targetPlayer) {
        // %target_display_name%
        try {
            input = input.replace("%target_display_name%", targetPlayer.getDisplayName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_interact%
        try {
            input = input.replace("%target_interact%", Utils.getNearbyPlayer(targetPlayer, 3));
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_money%
        if (ConfigHandler.getDepends().VaultEnabled()) {
            if (input.contains("%target_money%")) {
                input = input.replace("%target_money%", String.valueOf(ConfigHandler.getDepends().getVaultApi().getBalance(Bukkit.getOfflinePlayer(targetPlayer.getUniqueId()))));
            }
        }
        // %target_points%
        if (ConfigHandler.getDepends().PlayerPointsEnabled()) {
            if (input.contains("%target_points%")) {
                input = input.replace("%target_points%", String.valueOf(ConfigHandler.getDepends().getPlayerPointsApi().getBalance(Bukkit.getOfflinePlayer(targetPlayer.getUniqueId()))));
            }
        }
        // Translate PlaceHolderAPI's placeholders.
        if (ConfigHandler.getDepends().PlaceHolderAPIEnabled()) {
            try {
                return PlaceholderAPI.setPlaceholders(targetPlayer, input);
            } catch (NoSuchFieldError e) {
                ServerHandler.sendDebugMessage("Error has occurred when setting the PlaceHolder " + e.getMessage() + ", if this issue persist contact the developer of PlaceholderAPI.");
                return input;
            }
        }
        return input;
    }

    private static String translateBlock(String input, Block targetBlock) {
        // %target%
        try {
            input = input.replace("%target%", targetBlock.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_display_name%
        try {
            input = input.replace("%target_display_name%", targetBlock.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_type%
        try {
            input = input.replace("%target_type%", targetBlock.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_world%
        Location loc = targetBlock.getLocation();
        if (input.contains("%target_world%")) {
            input = input.replace("%target_world%", loc.getWorld().getName());
        }
        // %target_loc%
        // %target_loc_x%, %player_loc_y%, %player_loc_z%
        // %target_loc_x_NUMBER%, %target_loc_y_NUMBER%, %target_loc_z_NUMBER%
        if (input.contains("%target_loc")) {
            try {
                String loc_x = String.valueOf(loc.getBlockX());
                String loc_y = String.valueOf(loc.getBlockY());
                String loc_z = String.valueOf(loc.getBlockZ());
                String[] arr = input.split("%");
                for (int i = 0; i < arr.length; i++) {
                    switch (arr[i]) {
                        case "target_loc_x":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_x%" + arr[i + 1] + "%", loc_x + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_y":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_y%" + arr[i + 1] + "%", loc_y + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_z":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_z%" + arr[i + 1] + "%", loc_z + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                    }
                }
                input = input.replace("%target_loc%", loc_x + ", " + loc_y + ", " + loc_z);
                input = input.replace("%target_loc_x%", loc_x);
                input = input.replace("%target_loc_y%", loc_y);
                input = input.replace("%target_loc_z%", loc_z);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        return input;
    }

}
