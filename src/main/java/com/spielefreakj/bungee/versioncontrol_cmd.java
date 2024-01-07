package com.spielefreakj.bungee;

import com.spielefreakj.BungeeMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class versioncontrol_cmd extends Command {

    private static String pluginPrefix, pluginVersion;
    private BungeeMain pluginConfig = BungeeMain.getInstance();

    public versioncontrol_cmd(String pversion){
        super("versioncontrol", null, "versionc", "vcontrol");
        pluginVersion=pversion;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Configuration config = pluginConfig.config;
        pluginPrefix=config.getString("PluginPrefix");
        String[] messages = pluginConfig.messages_read;
        try {
            if (args.length > 0){
                if (args[0].equalsIgnoreCase("fallback")){
                    if (args.length==1){
                        commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA + pluginPrefix + ChatColor.RED+messages[0]+" "+messages[25]));
                        return;
                    }
                    if (commandSender.hasPermission("versioncontrol.fallback")){
                        boolean testfor = false;
                        for (ServerInfo sInfo: ProxyServer.getInstance().getServers().values()) {
                            if (args[1].equalsIgnoreCase(sInfo.getName())){
                                testfor=true;

                                config.set("Server.Fallback", sInfo.getName());
                                boolean savetest = pluginConfig.saveConfig();
                                if (savetest){
                                    commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+pluginPrefix+ChatColor.WHITE+" "+messages[23]+" "+ChatColor.GOLD+sInfo.getName()));
                                }else {
                                    commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+pluginPrefix+ChatColor.WHITE+ " Error during save. Please check logs!"));
                                }
                                break;
                            }
                        }
                        if (!testfor){
                            if (args[1].equalsIgnoreCase("kick")){
                                config.set("Server.Fallback", "kick");
                                boolean savetest = pluginConfig.saveConfig();
                                if (savetest){
                                    commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+pluginPrefix+ChatColor.WHITE+" "+messages[24]));
                                }else {
                                    commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+pluginPrefix+ChatColor.WHITE+ " Error during save. Please check logs!"));
                                }
                            }else {
                                commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA + pluginPrefix + ChatColor.WHITE + " " + messages[4] + ChatColor.GOLD + " " + args[1]+"\n"+ChatColor.RED+messages[0]+" "+messages[25]));
                            }
                        }
                    }
                    return;
                }if (args[0].equalsIgnoreCase("reload")){
                    if (commandSender.hasPermission("versioncontrol.reload")){
                        boolean testfor = pluginConfig.reloadConf();
                        if (testfor){
                            commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+pluginPrefix+ChatColor.WHITE+ " Successfully reloaded."));
                            return;
                        }else {
                            commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+pluginPrefix+ChatColor.WHITE+ " Error during reload. Please check config file and logs!"));
                        }
                    }
                    return;
                }else{
                    commandSender.sendMessage(new TextComponent(" Please visit https://spiele.fr/versioncontrolhelp"));
                    return;
                }
            }else {
                commandSender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+pluginPrefix+ChatColor.WHITE+" Version: "+ pluginVersion));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
