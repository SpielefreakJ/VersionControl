package com.spielefreakj;

import com.spielefreakj.bungee.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeeMain extends Plugin {
    private static BungeeMain instance;
    public static String pluginPrefix = "[VersionControl]";
    public String[] messages_read;
    private File file;
    public Configuration config;
    private int pluginId = 13574;

    @Override
    public void onEnable() {
        setInstance(this);
        // Start of create Config
        {
            try {
                if (!getDataFolder().exists()) {
                    boolean created_dir = getDataFolder().mkdir();
                    if (!created_dir){
                        getLogger().info(ChatColor.RED+"Plugin folder could NOT be created! "+ChatColor.GOLD+"Please check your write permissions IMMEDIATELY!");
                    }
                }
                file = new File(getDataFolder() + "/config.yml");
                if (!file.exists()) {
                    //file.createNewFile();
                    try (InputStream in = getResourceAsStream("config.yml")) {
                        Files.copy(in, file.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file); //Load File
                for (ServerInfo server: ProxyServer.getInstance().getServers().values()) {
                    if(config.getString("Server.Fallback").equals("")){
                        config.set("Server.Fallback", server.getName());
                        getLogger().info(ChatColor.AQUA+"Version Control: "+ChatColor.GOLD+"There was no Fallback Server. The new fallback server is: "+ChatColor.RED+server.getName());
                    }
                    if (config.getString("Server." + server.getName() + ".minVersion").equals("")||config.getString("Server." + server.getName() + ".maxVersion").equals("")) {
                        config.set("Server." + server.getName() + ".minVersion", "47");
                        config.set("Server." + server.getName() + ".maxVersion", "9999");
                    }
                }
                boolean testfor=false;
                for (ServerInfo sInfo: ProxyServer.getInstance().getServers().values()) {
                    if (config.getString("Server.Fallback").equalsIgnoreCase(sInfo.getName())){
                        testfor=true;
                    }
                }
                if (!testfor && !config.getString("Server.Fallback").equalsIgnoreCase("kick")){
                    config.set("Server.Fallback", "kick");
                    getLogger().info(ChatColor.AQUA+"Version Control: "+ChatColor.GOLD+"The Fallback Server did not exist. Players now get kicked if trying to connect!");
                }
                create_messages();
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);//Save File0
                messages_read = message_reader();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // END OF create Config

        //Commands
        getProxy().getPluginManager().registerCommand(this, new changeVersion());
        getProxy().getPluginManager().registerCommand(this, new checkVersion());
        getProxy().getPluginManager().registerCommand(this, new versioncontrol_cmd(getDescription().getVersion()));
        //Events
        getProxy().getPluginManager().registerListener(this, new Events());

        getLogger().info(ChatColor.GREEN +"Version Control Loaded! Made by SpielefreakJ");

        Metrics metrics = new Metrics(this, pluginId);
    }
    @Override
    public void onDisable(){
        getLogger().info(ChatColor.RED +"Version Control Disabled! Made by SpielefreakJ");
    }

    public static BungeeMain getInstance() {
        return instance;
    }

    private static void setInstance(BungeeMain instance) {
        BungeeMain.instance = instance;
    }

    public boolean reloadConf(){
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            boolean testfor=false;
            for (ServerInfo sInfo: ProxyServer.getInstance().getServers().values()) {
                if (config.getString("Server.Fallback").equalsIgnoreCase(sInfo.getName())){
                    testfor=true;
                }
            }
            if (!testfor){
                config.set("Server.Fallback", "kick");
            }

            create_messages();
            messages_read = message_reader();

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);//Save File0
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveConfig(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);//Save File0
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String[] message_reader(){
        String[] read = new String[50];
        read[0]=config.getString("Messages.usage.text");
        read[20]=config.getString("Messages.usage.changever");
        read[21]=config.getString("Messages.usage.checkver");
        read[25]=config.getString("Messages.usage.fallback");
        read[1]=config.getString("Messages.error.no_args");
        read[2]=config.getString("Messages.error.not_enough_args");
        read[3]=config.getString("Messages.error.too_many_args");
        read[4]=config.getString("Messages.error.no_server");
        read[5]=config.getString("Messages.error.no_server_casing");
        read[22]=config.getString("Messages.error.reload");
        read[6]=config.getString("Messages.connect.error_multi.after_servername");
        read[7]=config.getString("Messages.connect.error_multi.between_versions");
        read[8]=config.getString("Messages.connect.error_single");
        read[9]=config.getString("Messages.change.before_servername");
        read[10]=config.getString("Messages.change.after_servername");
        read[11]=config.getString("Messages.change.success_multi.min");
        read[12]=config.getString("Messages.change.success_multi.max");
        read[13]=config.getString("Messages.change.error.min_over_max");
        read[14]=config.getString("Messages.change.error.min_wrong");
        read[15]=config.getString("Messages.change.error.max_wrong");
        read[16]=config.getString("Messages.check.before_servername");
        read[17]=config.getString("Messages.check.multi.after_servername");
        read[18]=config.getString("Messages.check.multi.between_versions");
        read[19]=config.getString("Messages.check.single.after_servername");
        read[23]=config.getString("Messages.modify.fallback.set_to_server");
        read[24]=config.getString("Messages.modify.fallback.set_to_kick");
        read[26]=config.getString("Messages.fallback.kick_message.multi_part1");
        read[27]=config.getString("Messages.fallback.kick_message.multi_part2");
        read[28]=config.getString("Messages.fallback.kick_message.single");
        return read;
    }

    public void create_messages() {
        {
            if (config.getString("Messages.usage.text").equals("")) {
                config.set("Messages.usage.text", "Usage:");
            }
            if (config.getString("Messages.usage.changever").equals("")) {
                config.set("Messages.usage.changever", "/changeversion SERVER MinVersion MaxVersion");
            }
            if (config.getString("Messages.usage.checkver").equals("")) {
                config.set("Messages.usage.checkver", "/checkversion SERVER");
            }
            if (config.getString("Messages.usage.fallback").equals("")){
                config.set("Messages.usage.fallback", "/versioncontrol fallback SERVER (or kick to kick)");
            }
        }
        {
            if (config.getString("Messages.error.no_args").equals("")) {
                config.set("Messages.error.no_args", "You provided no arguments...");
            }
            if (config.getString("Messages.error.not_enough_args").equals("")) {
                config.set("Messages.error.not_enough_args", "You provided not enough arguments.");
            }
            if (config.getString("Messages.error.too_many_args").equals("")) {
                config.set("Messages.error.too_many_args", "You provided too many arguments.");
            }
            if (config.getString("Messages.error.no_server").equals("")) {
                config.set("Messages.error.no_server", "There is no Server called");
            }
            if (config.getString("Messages.error.no_server_casing").equals("")) {
                config.set("Messages.error.no_server_casing", "Please remember this command is case sensitive!");
            }
            if (config.getString("Messages.error.reload").equals("")) {
                config.set("Messages.error.reload", "Error during reload. Please read config file!");
            }
        }
        {
            if (config.getString("Messages.connect.error_multi.after_servername").equals("")) {
                config.set("Messages.connect.error_multi.after_servername", "is currently only available for Minecraft versions between");
            }
            if (config.getString("Messages.connect.error_multi.between_versions").equals("")) {
                config.set("Messages.connect.error_multi.between_versions", "and");
            }
            if (config.getString("Messages.connect.error_single").equals("")) {
                config.set("Messages.connect.error_single", "is currently only available for the Minecraft version");
            }
        }
        {
            if (config.getString("Messages.change.before_servername").equals("")) {
                config.set("Messages.change.before_servername", "Versions for the server");
            }
            if (config.getString("Messages.change.after_servername").equals("")) {
                config.set("Messages.change.after_servername", "has been changed to:");
            }
            if (config.getString("Messages.change.success_multi.min").equals("")) {
                config.set("Messages.change.success_multi.min", "Min:");
            }
            if (config.getString("Messages.change.success_multi.max").equals("")) {
                config.set("Messages.change.success_multi.max", "Max:");
            }
            if (config.getString("Messages.change.error.min_over_max").equals("")) {
                config.set("Messages.change.error.min_over_max", "The minimal version should NOT be bigger than the maximum version!");
            }
            if (config.getString("Messages.change.error.min_wrong").equals("")) {
                config.set("Messages.change.error.min_wrong", "The minimal Version was incorrect.");
            }
            if (config.getString("Messages.change.error.max_wrong").equals("")) {
                config.set("Messages.change.error.max_wrong", "The maximal Version was incorrect.");
            }
        }
        {
            if (config.getString("Messages.check.before_servername").equals("")) {
                config.set("Messages.check.before_servername", "The Server");
            }
            if (config.getString("Messages.check.multi.after_servername").equals("")) {
                config.set("Messages.check.multi.after_servername", "is set to the versions");
            }
            if (config.getString("Messages.check.multi.between_versions").equals("")) {
                config.set("Messages.check.multi.between_versions", "and");
            }
            if (config.getString("Messages.check.single.after_servername").equals("")) {
                config.set("Messages.check.single.after_servername", "is set to the version");
            }
        }
        {
            if (config.getString("Messages.modify.fallback.set_to_server").equals("")){
                config.set("Messages.modify.fallback.set_to_server", "The fallback server is now:");
            }
            if (config.getString("Messages.modify.fallback.set_to_kick").equals("")){
                config.set("Messages.modify.fallback.set_to_kick", "Set fallback event to kick");
            }
        }
        {
            if (config.getString("Messages.fallback.kick_message.multi_part1").equals("")){
                config.set("Messages.fallback.kick_message.multi_part1", "You got kicked from the Server, because the Server is only for Versions");
            }
            if (config.getString("Messages.fallback.kick_message.multi_part2").equals("")){
                config.set("Messages.fallback.kick_message.multi_part2", "and");
            }
            if (config.getString("Messages.fallback.kick_message.single").equals("")){
                config.set("Messages.fallback.kick_message.single", "You got kicked from the Server, because the Server is only for Version");
            }
        }
    }
}