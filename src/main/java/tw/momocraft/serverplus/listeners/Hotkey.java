package tw.momocraft.serverplus.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotkey implements Listener {

    private final Map<String, Long> cdMap = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onOpenInventory(InventoryOpenEvent e) {
        ServerHandler.sendConsoleMessage("InventoryOpenEvent");
        if (!ConfigHandler.getConfigPath().isHotkeyMenu()) {
            return;
        }
        ServerHandler.sendConsoleMessage("29");
        Inventory inventory = e.getInventory();
        if (inventory.getType() == InventoryType.PLAYER) {
            ServerHandler.sendConsoleMessage("32");
            if (e.getPlayer() instanceof Player) {
                ServerHandler.sendConsoleMessage("34");
                Player player = (Player) e.getPlayer();
                if (player.isSneaking()) {
                    ServerHandler.sendConsoleMessage("35");
                    Map<Integer, MenuMap> hotkeyMenuProp = ConfigHandler.getConfigPath().getHotkeyMenuProp();
                    MenuMap menuMap;
                    List<String> newLores = new ArrayList<>();
                    for (int slot : hotkeyMenuProp.keySet()) {
                        ServerHandler.sendConsoleMessage("Slot: " + slot);
                        menuMap = hotkeyMenuProp.get(slot);
                        ItemStack itemStack = new ItemStack(menuMap.getId());
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(Utils.translateLayout(menuMap.getName(), player));
                        for (String lore : menuMap.getLores()) {
                            newLores.add(Utils.translateLayout(lore, player));
                        }
                        itemMeta.setLore(newLores);
                        itemStack.setItemMeta(itemMeta);
                        e.getPlayer().getOpenInventory().getTopInventory().setItem(slot, itemStack);
                        ServerHandler.sendConsoleMessage("52");
                    }
                }
            }
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
            if (onCooldown(player)) {
                if (ConfigHandler.getConfigPath().isHotkeyCooldownMsg() && ConfigHandler.getConfigPath().getHotkeyCooldownInt() != 5) {
                    Language.sendLangMessage("Message.cooldown", player);
                }
                ServerHandler.sendFeatureMessage("Hotkey", playerName, "cooldown", "return", "Keyboard",
                        new Throwable().getStackTrace()[0]);
                return;
            }
            addCD(player);
            CustomCommands.executeMultiCmdsList(player, keyboardMap.getCommands());
            e.setCancelled(keyboardMap.isCancel());
            ServerHandler.sendFeatureMessage("Hotkey", playerName, "final", "return", "Keyboard",
                    new Throwable().getStackTrace()[0]);
        }
    }

    private boolean onCooldown(Player player) {
        long playersCDList = 0L;
        if (cdMap.containsKey(player.getWorld().getName() + "." + player.getName())) {
            playersCDList = cdMap.get(player.getWorld().getName() + "." + player.getName());
        }
        return System.currentTimeMillis() - playersCDList < 100 && System.currentTimeMillis() - playersCDList < ConfigHandler.getConfigPath().getHotkeyCooldownInt() * 50;
    }

    private void addCD(Player player) {
        cdMap.put(player.getWorld().getName() + "." + player.getName(), System.currentTimeMillis());
    }
}
