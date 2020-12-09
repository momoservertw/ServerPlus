package tw.momocraft.serverplus.utils;

public class ItemJoinMap {
    private String name;
    private String lore;
    private String itemNode;
    private boolean onlyOne;

    public String getItemNode() {
        return itemNode;
    }

    public String getLore() {
        return lore;
    }

    public String getName() {
        return name;
    }

    public void setItemNode(String itemNode) {
        this.itemNode = itemNode;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public void setName(String name) {
        this.name = name;
    }
}
