package tw.momocraft.serverplus.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.*;

import java.util.HashMap;
import java.util.Map;

public class Hotkey implements Listener {

    private final Map<String, Long> cdMap = new HashMap<>();

    private final Map<String, Long> cdDuplicateMap = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
        if (!ConfigHandler.getConfigPath().isHotkeyKeyboard()) {
            return;
        }
        Player player = e.getPlayer();
        if (!player.isSneaking()) {
            return;
        }
        String playerName = player.getName();
        KeyboardMap keyboardMap = ConfigHandler.getConfigPath().getHotkeyKeyboardProp().get(-1);
        if (keyboardMap != null) {
            String onCD = onCD(player);
            if (onCD.equals("true")) {
                if (onCDDuplicate(player)) {
                    e.setCancelled(keyboardMap.isCancel());
                    return;
                }
                addCDDuplicate(player);
                if (ConfigHandler.getConfigPath().isHotkeyCooldownMsg()) {
                    Language.sendLangMessage("Message.cooldown", player);
                }
                e.setCancelled(keyboardMap.isCancel());
                ServerHandler.sendFeatureMessage("Hotkey", playerName, "cooldown", "return", "Keyboard",
                        new Throwable().getStackTrace()[0]);
                return;
            } else if (onCD.equals("")) {
                e.setCancelled(keyboardMap.isCancel());
                return;
            }
            addCD(player);
            CustomCommands.executeMultiCmdsList(player, keyboardMap.getCommands());
            e.setCancelled(keyboardMap.isCancel());
            ServerHandler.sendFeatureMessage("Hotkey", playerName, "final", "return", "Keyboard",
                    new Throwable().getStackTrace()[0]);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        if (!ConfigHandler.getConfigPath().isHotkeyKeyboard()) {
            return;
        }
        Player player = e.getPlayer();
        if (!player.isSneaking()) {
            return;
        }
        String playerName = player.getName();
        int newSlot = e.getNewSlot();
        int previousSlot = e.getPreviousSlot();
        if (newSlot == previousSlot - 1 || newSlot == previousSlot + 1) {
            return;
        } else if (previousSlot == 0 && newSlot == 9 || previousSlot == 9 && newSlot == 0) {
            return;
        }
        KeyboardMap keyboardMap = ConfigHandler.getConfigPath().getHotkeyKeyboardProp().get(newSlot);
        if (keyboardMap != null) {
            String onCD = onCD(player);
            if (onCD.equals("true")) {
                if (onCDDuplicate(player)) {
                    e.setCancelled(keyboardMap.isCancel());
                    return;
                }
                addCDDuplicate(player);
                if (ConfigHandler.getConfigPath().isHotkeyCooldownMsg()) {
                    Language.sendLangMessage("Message.cooldown", player);
                }
                e.setCancelled(keyboardMap.isCancel());
                ServerHandler.sendFeatureMessage("Hotkey", playerName, "cooldown", "return", "Keyboard",
                        new Throwable().getStackTrace()[0]);
                return;
            } else if (onCD.equals("")) {
                e.setCancelled(keyboardMap.isCancel());
                return;
            }
            addCD(player);
            CustomCommands.executeMultiCmdsList(player, keyboardMap.getCommands());
            e.setCancelled(keyboardMap.isCancel());
            ServerHandler.sendFeatureMessage("Hotkey", playerName, "final", "return", "Keyboard",
                    new Throwable().getStackTrace()[0]);
        }
    }

    private String onCD(Player player) {
        long playersCDList = 0L;
        if (cdMap.containsKey(player.getWorld().getName() + "." + player.getName())) {
            playersCDList = cdMap.get(player.getWorld().getName() + "." + player.getName());
        }
        long cdLast = System.currentTimeMillis() - playersCDList;
        // The minimum cd (Not send message)
        if (cdLast < 100) {
            return "";
        }
        if (cdLast < ConfigHandler.getConfigPath().getHotkeyCooldownInt()) {
            return "true";
        } else {
            return "false";
        }
    }

    private void addCD(Player player) {
        cdMap.put(player.getWorld().getName() + "." + player.getName(), System.currentTimeMillis());
    }

    // The minimum cd (Not send message)
    private boolean onCDDuplicate(Player player) {
        long playersCDList = 0L;
        if (cdDuplicateMap.containsKey(player.getWorld().getName() + "." + player.getName())) {
            playersCDList = cdDuplicateMap.get(player.getWorld().getName() + "." + player.getName());
        }
        long cdLast = System.currentTimeMillis() - playersCDList;
        return cdLast < ConfigHandler.getConfigPath().getHotkeyCooldownInt();
    }

    private void addCDDuplicate(Player player) {
        cdDuplicateMap.put(player.getWorld().getName() + "." + player.getName(), System.currentTimeMillis());
    }
}
