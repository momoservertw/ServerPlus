package tw.momocraft.serverplus.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.event.ActionMap;
import tw.momocraft.serverplus.utils.event.ConditionMap;
import tw.momocraft.serverplus.utils.event.EventMap;

import java.util.*;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    private Map<String, String> customCmdProp;
    private boolean logDefaultNew;
    private boolean logDefaultZip;
    private boolean logCustomNew;
    private boolean logCustomZip;
    private String logCustomPath;
    private String logCustomName;

    private String menuIJ;
    private String menuType;
    private String menuName;

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

        menuIJ = ConfigHandler.getConfig("config.yml").getString("General.Menu.ItemJoin");
        menuType = ConfigHandler.getConfig("config.yml").getString("General.Menu.Item.Type");
        menuName = ConfigHandler.getConfig("config.yml").getString("General.Menu.Item.Name");
    }

    //  ============================================== //
    //         Setup Event.                            //
    //  ============================================== //
    private void setEvent() {
        event = ConfigHandler.getConfig("config.yml").getBoolean("Event.Enable");

        ConfigurationSection eventRegConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Settings.Features.Event");
        if (eventRegConfig != null) {
            for (String event : eventRegConfig.getKeys(false)) {
                eventRegisterProp.put(event, ConfigHandler.getConfig("config.yml").getBoolean("Event.Settings.Features.Event." + event));
            }
        }
        ConfigurationSection eventConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups");
        if (eventConfig != null) {
            EventMap eventMap;
            ConfigurationSection targetConfig;
            Map<String, List<String>> targetMap;

            ConfigurationSection conditionConfig;
            ConfigurationSection conditionValueConfig;
            List<ConditionMap> conditionMapList;
            List<ConditionMap> conditionValueMapList;
            ConditionMap conditionMap;
            ConditionMap conditionValueMap;

            ConfigurationSection actionConfig;
            ConfigurationSection actionValueConfig;
            List<ActionMap> actionMapList;
            List<ActionMap> actionValueMapList;
            ActionMap actionMap;
            ActionMap actionValueMap;
            for (String group : eventConfig.getKeys(false)) {
                eventMap = new EventMap();
                eventMap.setPriority(ConfigHandler.getConfig("config.yml").getLong("Event.Groups." + group + ".Priority"));
                eventMap.setEvents(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Events"));
                targetConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Targets");
                if (targetConfig != null) {
                    targetMap = new HashMap<>();
                    for (String key : targetConfig.getKeys(false)) {
                        targetMap.put(key, ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Targets." + key));
                    }
                    eventMap.setTargets(targetMap);
                }
                conditionConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Conditions");
                if (conditionConfig != null) {
                    conditionMapList = new ArrayList<>();
                    for (String key : conditionConfig.getKeys(false)) {
                        conditionMap = new ConditionMap();
                        switch (key) {
                            case "Placeholders":
                                conditionMap.setPlaceholders(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Holding-Menu":
                                conditionMap.setHoldingMenu(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Conditions." + key));
                                break;
                            case "Succeed":
                                conditionValueConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Conditions." + key);
                                if (conditionValueConfig == null) {
                                    break;
                                }
                                conditionValueMap = new ConditionMap();
                                conditionValueMapList = new ArrayList<>();
                                for (String key2 : conditionValueConfig.getKeys(false)) {
                                    if (key2.equals("Placeholders")) {
                                        conditionValueMap.setPlaceholders(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Conditions." + key + "." + key2));
                                    } else if (key2.equals("Holding-Menu")) {
                                        conditionValueMap.setHoldingMenu(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Conditions." + key + "." + key2));
                                    }
                                    conditionValueMapList.add(conditionValueMap);
                                }
                                conditionMap.setSucceedMap(conditionValueMapList);
                                break;
                            case "Failed":
                                conditionValueConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Conditions." + key);
                                if (conditionValueConfig == null) {
                                    break;
                                }
                                conditionValueMap = new ConditionMap();
                                conditionValueMapList = new ArrayList<>();
                                for (String key2 : conditionValueConfig.getKeys(false)) {
                                    if (key2.equals("Placeholders")) {
                                        conditionValueMap.setPlaceholders(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Conditions." + key + "." + key2));
                                    } else if (key2.equals("Holding-Menu")) {
                                        conditionValueMap.setHoldingMenu(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Conditions." + key + "." + key2));
                                    }
                                    conditionValueMapList.add(conditionValueMap);
                                }
                                conditionMap.setFailedMap(conditionValueMapList);
                                break;
                        }
                    }
                    eventMap.setConditions(conditionMapList);
                }
                actionConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Actions");
                if (actionConfig != null) {
                    actionMapList = new ArrayList<>();
                    for (String key : actionConfig.getKeys(false)) {
                        actionMap = new ActionMap();
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
                            case "Clean-Slot":
                                actionMap.setCleanSlots(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key));
                                break;
                            case "Clean-Slot-Target":
                                actionMap.setCleanSlotTarget(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key));
                                break;
                            case "Show-Pet-Info":
                                actionMap.setPetInfo(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key));
                                break;
                            case "Succeed":
                                actionValueConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Actions." + key);
                                if (actionValueConfig == null) {
                                    break;
                                }
                                actionValueMap = new ActionMap();
                                actionValueMapList = new ArrayList<>();
                                for (String key2 : actionValueConfig.getKeys(false)) {
                                    switch (key2) {
                                        case "Commands":
                                            actionMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Cancel":
                                            actionMap.setCancel(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Kill":
                                            actionMap.setKill(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Kill-Target":
                                            actionMap.setKillTarget(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Clean-Slot":
                                            actionMap.setCleanSlots(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Clean-Slot-Target":
                                            actionMap.setCleanSlotTarget(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Show-Pet-Info":
                                            actionMap.setPetInfo(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                    }
                                    actionValueMapList.add(actionValueMap);
                                }
                                actionMap.setSucceedMap(actionValueMapList);
                                break;
                            case "Failed":
                                actionValueConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Event.Groups." + group + ".Actions." + key);
                                if (actionValueConfig == null) {
                                    break;
                                }
                                actionValueMap = new ActionMap();
                                actionValueMapList = new ArrayList<>();
                                for (String key2 : actionValueConfig.getKeys(false)) {
                                    switch (key2) {
                                        case "Commands":
                                            actionMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Cancel":
                                            actionMap.setCancel(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Kill":
                                            actionMap.setKill(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Kill-Target":
                                            actionMap.setKillTarget(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Clean-Slot":
                                            actionMap.setCleanSlots(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Clean-Slot-Target":
                                            actionMap.setCleanSlotTarget(ConfigHandler.getConfig("config.yml").getStringList("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                        case "Show-Pet-Info":
                                            actionMap.setPetInfo(ConfigHandler.getConfig("config.yml").getString("Event.Groups." + group + ".Actions." + key + "." + key2));
                                            break;
                                    }
                                    actionValueMapList.add(actionValueMap);
                                }
                                actionMap.setFailedMap(actionValueMapList);
                                break;
                        }
                        actionMapList.add(actionMap);
                    }
                    eventMap.setActions(actionMapList);
                }
                // Add properties to all events.
                for (String event : eventMap.getEvents()) {
                    if (eventRegisterProp.get(event) == null) {
                        continue;
                    }
                    try {
                        eventProp.get(event).put(group, eventMap);
                    } catch (Exception ex) {
                        eventProp.put(event, new HashMap<>());
                        eventProp.get(event).put(group, eventMap);
                    }
                }
            }
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
                }
                sortMap = Utils.sortByValue(sortMap);
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
                if (groupEnable == null || groupEnable.equals("true")) {
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

    //  ============================================== //
    //         Mypet Settings                          //
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
