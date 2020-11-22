package tw.momocraft.serverplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    private Map<String, String> customCmdProp;

    //  ============================================== //
    //         MyPet Settings                        //
    //  ============================================== //
    private boolean mypet;
    private boolean mypetSkillAuto;
    private Map<String, List<String>> skillProp;

    //  ============================================== //
    //         ItemJoin Settings                        //
    //  ============================================== //
    private boolean itemjoin;
    private boolean ijFixOldItem;
    private boolean ijOneMenu;
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
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setupGeneral();
        setupMyPet();
        setupItemJoin();
        setupMorphTool();
    }

    private void setupGeneral() {
        ConfigurationSection cmdConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Custom-Commands");
        if (cmdConfig != null) {
            customCmdProp = new HashMap<>();
            for (String group : cmdConfig.getKeys(false)) {
                customCmdProp.put(group, ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands." + group));
            }
        }
    }

    private void setupMyPet() {
        mypet = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Enable");
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

    private void setupItemJoin() {
        itemjoin = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Enable");
        ijFixOldItem = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Fix-Old-Item.Enable");
        ijOneMenu = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Fix-Old-Item.Settings.Enable");
        ijOneMenuName = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Settings.Name");
        ijOneMenuType = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Settings.Type");
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

    private void setupMorphTool() {
        morphtool = ConfigHandler.getConfig("config.yml").getBoolean("MorphTool.Enable");
        morphtoolNetherite = ConfigHandler.getConfig("config.yml").getBoolean("MorphTool.Prvent-Update-Netherite");
        try {
            morphtoolName = Utils.translateColorCode(ConfigHandler.getConfig("config.yml").getString("MorphTool.ToolName"));
        } catch (Exception e) {}
    }

    public Map<String, String> getCustomCmdProp() {
        return customCmdProp;
    }

    public boolean isMypet() {
        return mypet;
    }

    public boolean isMypetSkillAuto() {
        return mypetSkillAuto;
    }

    public Map<String, List<String>> getSkillProp() {
        return skillProp;
    }

    public boolean isItemjoin() {
        return itemjoin;
    }

    public boolean isIjFixOldItem() {
        return ijFixOldItem;
    }

    public Map<String, List<ItemJoinMap>> getIjProp() {
        return ijProp;
    }

    public boolean isMorphtool() {
        return morphtool;
    }

    public boolean isMorphtoolNetherite() {
        return morphtoolNetherite;
    }

    public String getMorphtoolName() {
        return morphtoolName;
    }

    public boolean isIjOneMenu() {
        return ijOneMenu;
    }

    public String getIjOneMenuName() {
        return ijOneMenuName;
    }

    public String getIjOneMenuType() {
        return ijOneMenuType;
    }
}
