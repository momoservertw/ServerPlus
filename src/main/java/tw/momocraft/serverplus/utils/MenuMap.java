package tw.momocraft.serverplus.utils;

import org.bukkit.Material;

import java.util.List;

public class MenuMap {

    private String name;
    private Material id;
    private List<String> lores;
    private List<String> commands;


    public List<String> getLores() {
        return lores;
    }

    public Material getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLores(List<String> lores) {
        this.lores = lores;
    }

    public void setId(Material id) {
        this.id = id;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
