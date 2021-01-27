package tw.momocraft.serverplus.utils;

import java.util.List;

public class ItemJoinFixMap {
    private List<String> names;
    private String itemNode;
    private String type;


    public String getItemNode() {
        return itemNode;
    }

    public List<String> getNames() {
        return names;
    }

    public String getType() {
        return type;
    }

    public void setItemNode(String itemNode) {
        this.itemNode = itemNode;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setType(String type) {
        this.type = type;
    }
}
