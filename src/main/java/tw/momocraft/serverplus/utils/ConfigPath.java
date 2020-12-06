package tw.momocraft.serverplus.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.blocksutils.BlocksUtils;
import tw.momocraft.serverplus.utils.event.ActionMap;
import tw.momocraft.serverplus.utils.event.ConditionMap;
import tw.momocraft.serverplus.utils.event.EventMap;
import tw.momocraft.serverplus.utils.locationutils.LocationUtils;

import java.util.*;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    private Map<String, String> customCmdProp;

    private LocationUtils locationUtils;
    private BlocksUtils blocksUtils;

    private boolean logDefaultNew;
    private boolean logDefaultZip;
    private boolean logCustomNew;
    private boolean logCustomZip;
    private String logCustomPath;
    private String logCustomName;

    private String menuIJ;
    private String menuType;
    private String menuName;

    private String vanillaTrans;

    //  ============================================== //
    //         Hotkey Settings                         //
    //  ============================================== //
    private boolean hotkey;
    private boolean hotkeyMenu;
    private boolean hotkeyKeyboard;
    private boolean hotkeyCooldown;
    private int hotkeyCooldownInt;
    private boolean hotkeyCooldownMsg;
    private final Map<Integer, MenuMap> hotkeyMenuProp = new HashMap<>();
    private final Map<Integer, KeyboardMap> hotkeyKeyboardProp = new HashMap<>();

    //  ============================================== //
    //         Event Settings                        //
    //  ============================================== //
    private boolean event;
    private final Map<String, Boolean> eventRegisterProp = new HashMap<>();
    private final Map<String, Map<String, EventMap>> eventProp = new HashMap<>();

    //  ============================================== //
    //         MyPet Settings                        //
    //  ============================================== //
    private boolean myPet;
    private boolean mypetSkillAuto;
    private Map<String, List<String>> skillProp;

    //  ============================================== //
    //         MySQLPlayerDataBridge Settings          //
    //  ============================================== //
    private boolean mpdb;
    private boolean mpdbSyncComplete;
    private List<String> mpdbSyncCompleteCmds;

    //  ============================================== //
    //         ItemJoin Settings                        //
    //  ============================================== //
    private boolean itemjoin;
    private boolean ijFixOldItem;
    private boolean ijOneMenu;
    private String ijOneMenuNode;
    private String ijOneMenuName;
    private String ijOneMenuType;
    private Map<String, List<ItemJoinMap>> ijProp;

    //  ============================================== //
    //         MorphTool Settings                        //
    //  ============================================== //
    private boolean morphtool;
    private boolean morphtoolNetherite;
    private String morphtoolName;

    //  ============================================== //
    //         AuthMe Settings                        //
    //  ============================================== //
    private boolean authMe;
    private boolean authMeMail;

    //  ============================================== //
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setGeneral();
        setHotKey();
        setEvent();
        setMyPet();
        setMpdb();
        setItemJoin();
        setMorphTool();
        setAuthMe();
    }


    //  ============================================== //
    //         Setup General.                          //
    //  ============================================== //
    private void setGeneral() {
        ConfigurationSection cmdConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Custom-Commands");
        if (cmdConfig != null) {
            customCmdProp = new HashMap<>();
            for (String group : cmdConfig.getKeys(false)) {
                customCmdProp.put(group, ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands." + group));
            }
        }
        locationUtils = new LocationUtils();
        blocksUtils = new BlocksUtils();

        menuIJ = ConfigHandler.getConfig("config.yml").getString("General.Menu.ItemJoin");
        menuType = ConfigHandler.getConfig("config.yml").getString("General.Menu.Item.Type");
        menuName = ConfigHandler.getConfig("config.yml").getString("General.Menu.Item.Name");

        vanillaTrans = ConfigHandler.getConfig("config.yml").getString("General.Vanilla-Translate.Local");
    }

    //  ============================================== //
    //         Setup HotKey.                           //
    //  ============================================== //
    private void setHotKey() {
        hotkey = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Enable");
        if (hotkey) {
            hotkeyMenu = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Menu.Enable");
            hotkeyKeyboard = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Enable");
        }
        hotkeyCooldown = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Settings.Cooldown.Enable");
        hotkeyCooldownInt = ConfigHandler.getConfig("config.yml").getInt("HotKey.Keyboard.Settings.Cooldown.Interval");
        if (hotkeyCooldownInt <= 5) {
            hotkeyCooldownInt = 5;
        }
        hotkeyCooldownMsg = ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Settings.Cooldown.Message");

        ConfigurationSection hotkeyMenuConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("HotKey.Menu");
        if (hotkeyMenuConfig != null) {
            MenuMap menuMap;
            Material id;
            for (String group : hotkeyMenuConfig.getKeys(false)) {
                if (!Utils.isEnable(ConfigHandler.getConfig("config.yml").getString("HotKey.Menu." + group + ".Enable"), true)) {
                    continue;
                }
                menuMap = new MenuMap();
                try {
                    id = Material.getMaterial(ConfigHandler.getConfig("config.yml").getString("HotKey.Menu." + group + ".Item.id"));
                } catch (Exception ex) {
                    id = Material.STONE;
                }
                menuMap.setId(id);
                menuMap.setName(ConfigHandler.getConfig("config.yml").getString("HotKey.Menu." + group + ".Item.name"));
                menuMap.setLores(ConfigHandler.getConfig("config.yml").getStringList("HotKey.Menu." + group + ".Item.lore"));
                menuMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("HotKey.Menu." + group + ".Commands"));
                hotkeyMenuProp.put(ConfigHandler.getConfig("config.yml").getInt("HotKey.Menu." + group + ".Slot"), menuMap);
            }
        }
        ConfigurationSection hotkeyKeyboardConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("HotKey.Keyboard.Groups");
        if (hotkeyMenuConfig != null) {
            KeyboardMap keyboardMap;
            for (String group : hotkeyKeyboardConfig.getKeys(false)) {
                if (!Utils.isEnable(ConfigHandler.getConfig("config.yml").getString("HotKey.Keyboard.Groups." + group + ".Enable"), true)) {
                    continue;
                }
                keyboardMap = new KeyboardMap();
                keyboardMap.setCancel(ConfigHandler.getConfig("config.yml").getBoolean("HotKey.Keyboard.Groups." + group + ".Cancel"));
                keyboardMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("HotKey.Keyboard.Groups." + group + ".Commands"));
                hotkeyKeyboardProp.put(ConfigHandler.getConfig("config.yml").getInt("HotKey.Keyboard.Groups." + group + ".Slot"), keyboardMap);
            }
        }
    }

    //  ============================================== //
    //         Setup Event.                            //
    //  ============================================== //
    private void setEvent() {
        event = ConfigHandler.getConfig("config.yml").getBoolean("Event.Enable");

        ConfigurationSection eventRegConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Settings.Features.Register");
        if (eventRegConfig != null) {
            for (String event : eventRegConfig.getKeys(false)) {
                eventRegisterProp.put(event, ConfigHandler.getConfig("config.yml").getBoolean("Event.Settings.Features.Register." + event));
            }
        }
        ConfigurationSection eventConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups");
        if (eventConfig != null) {
            EventMap eventMap;
            // Targets
            ConfigurationSection targetConfig;
            Map<String, List<String>> targetMap;
            // Conditions
            ConfigurationSection conditionConfig;
            ConditionMap conditionMap;
            // Actions
            ConfigurationSection actionConfig;
            ActionMap actionMap;
            for (String group : eventConfig.getKeys(false)) {
                if (!Utils.isEnable(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Enable"), true)) {
                    continue;
                }
                ServerHandler.sendConsoleMessage("for (String group - " + group);
                eventMap = new EventMap();
                eventMap.setPriority(ConfigHandler.getConfig("config.yml").getLong("Event.Groups." + group + ".Priority"));
                eventMap.setEvents(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Events"));
                // Targets
                targetConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Targets");
                if (targetConfig != null) {
                    ServerHandler.sendConsoleMessage("targetConfig != null)");
                    targetMap = new HashMap<>();
                    for (String key : targetConfig.getKeys(false)) {
                        targetMap.put(key, ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Targets." + key));
                    }
                    eventMap.setTargets(targetMap);
                }
                // Conditions
                conditionConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Conditions");
                if (conditionConfig != null) {
                    ServerHandler.sendConsoleMessage("conditionConfig != null)");
                    conditionMap = new ConditionMap();
                    for (String key : conditionConfig.getKeys(false)) {
                        switch (key) {
                            case "Placeholders":
                                conditionMap.setPlaceholders(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Holding-Menu":
                                conditionMap.setHoldingMenu(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Hand-Slots":
                                conditionMap.setHandSlots(ConfigHandler.getConfig("config.yml").getIntegerList("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Sneak":
                                conditionMap.setSneak(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Fly":
                                conditionMap.setFly(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Boimes":
                                conditionMap.setBoimes(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Ignore-Boimes":
                                conditionMap.setIgnoreBoimes(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Day":
                                conditionMap.setDay(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Liquid":
                                conditionMap.setLiquid(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Location":
                                conditionMap.setLocMaps(locationUtils.getSpeLocMaps("config.yml", "Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Blocks":
                                conditionMap.setBlocksMaps(blocksUtils.getSpeBlocksMaps("config.yml", "Event.Groups." + group + ".Conditions." + key));
                                break;
                        }
                    }
                    eventMap.setConditions(conditionMap);
                }
                // Actions
                actionConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Actions");
                if (actionConfig != null) {
                    ServerHandler.sendConsoleMessage("actionConfig != null");
                    actionMap = new ActionMap();
                    for (String key : actionConfig.getKeys(false)) {
                        switch (key) {
                            case "Commands":
                                actionMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key));
                                break;
                            case "Cancel":
                                actionMap.setCancel(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key));
                                break;
                            case "Kill":
                                actionMap.setKill(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key));
                                break;
                            case "Kill-Target":
                                actionMap.setKillTarget(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key));
                                break;
                            case "Show-Pet-Info":
                                actionMap.setPetInfo(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key));
                                break;
                        }
                    }
                    eventMap.setActions(actionMap);
                }
                actionConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Actions-Failed");
                if (actionConfig != null) {
                    ServerHandler.sendConsoleMessage("actionConfig != null");
                    actionMap = new ActionMap();
                    for (String key : actionConfig.getKeys(false)) {
                        switch (key) {
                            case "Commands":
                                actionMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions-Failed." + key));
                                break;
                            case "Cancel":
                                actionMap.setCancel(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions-Failed." + key));
                                break;
                            case "Kill":
                                actionMap.setKill(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions-Failed." + key));
                                break;
                            case "Kill-Target":
                                actionMap.setKillTarget(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions-Failed." + key));
                                break;
                            case "Show-Pet-Info":
                                actionMap.setPetInfo(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions-Failed." + key));
                                break;
                        }
                    }
                    eventMap.setActionsFailed(actionMap);
                }
                ServerHandler.sendConsoleMessage("256");
                // Add properties to all events.
                for (String event : eventMap.getEvents()) {
                    if (eventRegisterProp.get(event) == null) {
                        continue;
                    }
                    try {
                        ServerHandler.sendConsoleMessage("eventProp.get(event).put(group, eventMap) - " + event + ": " + group);
                        eventProp.get(event).put(group, eventMap);
                    } catch (Exception ex) {
                        eventProp.put(event, new HashMap<>());
                        ServerHandler.sendConsoleMessage("eventProp.get(event).put(group, eventMap) - " + event + ": " + group);
                        eventProp.get(event).put(group, eventMap);
                    }
                }
            }
            ServerHandler.sendConsoleMessage("272 " + eventProp.keySet());
            Iterator<String> i = eventProp.keySet().iterator();
            Map<String, Long> sortMap;
            Map<String, EventMap> newEnMap;
            String event;
            while (i.hasNext()) {
                event = i.next();
                sortMap = new HashMap<>();
                newEnMap = new LinkedHashMap<>();
                for (String group : eventProp.get(event).keySet()) {
                    sortMap.put(group, eventProp.get(event).get(group).getPriority());
                    ServerHandler.sendConsoleMessage("sortMap.put(group, eventProp.get(event).get(group).getPriority()) - " + event + ": " + group);
                }
                sortMap = Utils.sortByValue(sortMap);
                ServerHandler.sendConsoleMessage("sortMap.keySet() " + sortMap.keySet());
                for (String group : sortMap.keySet()) {
                    ServerHandler.sendFeatureMessage("Event", event, "setup", "continue", group,
                            new Throwable().getStackTrace()[0]);
                    newEnMap.put(group, eventProp.get(event).get(group));
                }
                eventProp.replace(event, newEnMap);
            }
        }

    }

    //  ============================================== //
    //         Setup MyPet.                            //
    //  ============================================== //
    private void setMyPet() {
        myPet = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Enable");
        mypetSkillAuto = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Skilltree-Auto-Select.Enable");
        ConfigurationSection skillConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("MyPet.Skilltree-Auto-Select.Groups");
        if (skillConfig != null) {
            skillProp = new HashMap<>();
            List<String> commands;
            String groupEnable;
            for (String group : skillConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                groupEnable = ConfigHandler.getConfig("config.yml").getString("MyPet.Skilltree-Auto-Select.Groups." + group + ".Enable");
                if (Utils.isEnable(groupEnable, true)) {
                    commands = ConfigHandler.getConfig("config.yml").getStringList("MyPet.Skilltree-Auto-Select.Groups." + group + ".Commands");
                    if (group.equals("Default")) {
                        skillProp.put("Default", commands);
                    }
                    for (String type : ConfigHandler.getConfig("config.yml").getStringList("MyPet.Skilltree-Auto-Select.Groups." + group + ".Types")) {
                        skillProp.put(type, commands);
                    }
                }
            }
        }
    }

    //  ============================================== //
    //         MySQLPlayerDataBridge Settings          //
    //  ============================================== //
    private void setMpdb() {
        mpdb = ConfigHandler.getConfig("config.yml").getBoolean("MySQLPlayerDataBridge.Enable");
        mpdbSyncComplete = ConfigHandler.getConfig("config.yml").getBoolean("MySQLPlayerDataBridge.SyncComplete.Enable");
        mpdbSyncCompleteCmds = ConfigHandler.getConfig("config.yml").getStringList("MySQLPlayerDataBridge.SyncComplete.Commands");
    }

    //  ============================================== //
    //         ItemJoin Settings                       //
    //  ============================================== //
    private void setItemJoin() {
        itemjoin = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Enable");
        ijFixOldItem = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Fix-Old-Item.Enable");
        ijOneMenu = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Fix-Old-Item.Settings.One-Menu.Enable");
        ijOneMenuNode = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Settings.One-Menu.ItemNode");
        ijOneMenuName = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Settings.One-Menu.Name");
        ijOneMenuType = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Settings.One-Menu.Type");
        ConfigurationSection ijConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("ItemJoin.Fix-Old-Item.Groups");
        if (ijConfig != null) {
            ijProp = new HashMap<>();
            ItemJoinMap ijMap;
            String itemType;
            for (String group : ijConfig.getKeys(false)) {
                ijMap = new ItemJoinMap();
                ijMap.setItemNode(group);
                ijMap.setName(Utils.translateColorCode(ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Groups." + group + ".Name")));
                itemType = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Groups." + group + ".Type");
                //ijMap.setLore(Utils.translateColorCode(ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Groups." + group + ".Lore")));
                try {
                    ijProp.get(itemType).add(ijMap);
                } catch (Exception ex) {
                    ijProp.put(itemType, new ArrayList<>());
                    ijProp.get(itemType).add(ijMap);
                }
            }
        }
    }

    //  ============================================== //
    //         MorphTool Settings                      //
    //  ============================================== //
    private void setMorphTool() {
        morphtool = ConfigHandler.getConfig("config.yml").getBoolean("MorphTool.Enable");
        morphtoolNetherite = ConfigHandler.getConfig("config.yml").getBoolean("MorphTool.Prvent-Update-Netherite");
        morphtoolName = Utils.translateColorCode(ConfigHandler.getConfig("config.yml").getString("MorphTool.ToolName"));
    }

    //  ============================================== //
    //         AuthMe Settings          //
    //  ============================================== //
    private void setAuthMe() {
        authMe = ConfigHandler.getConfig("config.yml").getBoolean("AuthMe.Enable");
        authMeMail = ConfigHandler.getConfig("config.yml").getBoolean("AuthMe.Mail-Warning.Enable");
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    public Map<String, String> getCustomCmdProp() {
        return customCmdProp;
    }

    public boolean isLogDefaultNew() {
        return logDefaultNew;
    }

    public boolean isLogDefaultZip() {
        return logDefaultZip;
    }

    public boolean isLogCustomNew() {
        return logCustomNew;
    }

    public boolean isLogCustomZip() {
        return logCustomZip;
    }

    public String getLogCustomName() {
        return logCustomName;
    }

    public String getLogCustomPath() {
        return logCustomPath;
    }

    public String getMenuIJ() {
        return menuIJ;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public String getVanillaTrans() {
        return vanillaTrans;
    }

    public LocationUtils getLocationUtils() {
        return locationUtils;
    }

    public BlocksUtils getBlocksUtils() {
        return blocksUtils;
    }

    //  ============================================== //
    //         Hotkey Settings                         //
    //  ============================================== //
    public boolean isHotkey() {
        return hotkey;
    }

    public boolean isHotkeyMenu() {
        return hotkeyMenu;
    }

    public boolean isHotkeyKeyboard() {
        return hotkeyKeyboard;
    }

    public boolean getHotkeyCooldown() {
        return hotkeyCooldown;
    }

    public boolean isHotkeyCooldownMsg() {
        return hotkeyCooldownMsg;
    }

    public int getHotkeyCooldownInt() {
        return hotkeyCooldownInt;
    }

    public Map<Integer, MenuMap> getHotkeyMenuProp() {
        return hotkeyMenuProp;
    }

    public Map<Integer, KeyboardMap> getHotkeyKeyboardProp() {
        return hotkeyKeyboardProp;
    }

    //  ============================================== //
    //         MyPet Settings                          //
    //  ============================================== //
    public boolean isEvent() {
        return event;
    }

    public Map<String, Boolean> getEventRegisterProp() {
        return eventRegisterProp;
    }

    public Map<String, Map<String, EventMap>> getEventProp() {
        return eventProp;
    }

    //  ============================================== //
    //         MyPet Settings                          //
    //  ============================================== //
    public boolean isMyPet() {
        return myPet;
    }

    public boolean isMypetSkillAuto() {
        return mypetSkillAuto;
    }

    public Map<String, List<String>> getSkillProp() {
        return skillProp;
    }

    //  ============================================== //
    //         MySQLPlayerDataBridge Settings          //
    //  ============================================== //
    public boolean isMpdb() {
        return mpdb;
    }

    public boolean isMpdbSyncComplete() {
        return mpdbSyncComplete;
    }

    public List<String> getMpdbSyncCompleteCmds() {
        return mpdbSyncCompleteCmds;
    }

    //  ============================================== //
    //         ItemJoin Settings                       //
    //  ============================================== //
    public boolean isItemjoin() {
        return itemjoin;
    }

    public boolean isIjFixOldItem() {
        return ijFixOldItem;
    }

    public boolean isIjOneMenu() {
        return ijOneMenu;
    }

    public String getIjOneMenuNode() {
        return ijOneMenuNode;
    }

    public String getIjOneMenuName() {
        return ijOneMenuName;
    }

    public String getIjOneMenuType() {
        return ijOneMenuType;
    }

    public Map<String, List<ItemJoinMap>> getIjProp() {
        return ijProp;
    }

    //  ============================================== //
    //         MorphTool Settings                      //
    //  ============================================== //
    public boolean isMorphtool() {
        return morphtool;
    }

    public boolean isMorphtoolNetherite() {
        return morphtoolNetherite;
    }

    public String getMorphtoolName() {
        return morphtoolName;
    }

    //  ============================================== //
    //         AuthMe Settings                      //
    //  ============================================== //
    public boolean isAuthMe() {
        return authMe;
    }

    public boolean isAuthMeMail() {
        return authMeMail;
    }

    //  ============================================== //
    //         Others                                  //
    //  ============================================== //
    public List<String> getTypeList(String file, String path, String listType) {
        List<String> list = new ArrayList<>();
        List<String> customList;
        for (String type : ConfigHandler.getConfig(file).getStringList(path)) {
            try {
                if (listType.equals("Entities")) {
                    list.add(EntityType.valueOf(type).name());
                } else if (listType.equals("Materials")) {
                    list.add(Material.valueOf(type).name());
                }
            } catch (Exception e) {
                customList = ConfigHandler.getConfig("groups.yml").getStringList(listType + "." + type);
                if (customList.isEmpty()) {
                    continue;
                }
                // Add Custom Group.
                for (String customType : customList) {
                    try {
                        if (listType.equals("Entities")) {
                            list.add(EntityType.valueOf(customType).name());
                        } else if (listType.equals("Materials")) {
                            list.add(Material.valueOf(customType).name());
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return list;
    }
}
