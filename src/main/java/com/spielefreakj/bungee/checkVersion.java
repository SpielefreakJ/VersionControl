package com.spielefreakj.bungee;

import com.spielefreakj.BungeeMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class checkVersion extends Command {

    private final BungeeMain pluginConfig = BungeeMain.getInstance();

    public checkVersion(){
        super("checkversion", "VersionControl.check", "checkv");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Configuration config = pluginConfig.config;
        String pluginPrefix = config.getString("PluginPrefix");
        String[] messages = pluginConfig.messages_read;
            if (args.length == 1){
                boolean testfor=false;
                for (ServerInfo sInfo : ProxyServer.getInstance().getServers().values()) {
                    if (args[0].equalsIgnoreCase(sInfo.getName())){
                        testfor=true;
                        int minVerProt = Integer.parseInt(config.getString("Server." + sInfo.getName() + ".minVersion"));
                        int maxVerProt = Integer.parseInt(config.getString("Server." + sInfo.getName() + ".maxVersion"));

                        VersionConversion verConv = new VersionConversion();

                        if (minVerProt == maxVerProt) {
                            sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA + pluginPrefix + ChatColor.GREEN + " "+messages[16]+" " + ChatColor.GOLD + sInfo.getName() + ChatColor.GREEN + " "+messages[19]+" " + ChatColor.YELLOW + verConv.getVersionName(minVerProt)));
                        }else {
                            sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA + pluginPrefix + ChatColor.GREEN + " "+messages[16]+" " + ChatColor.GOLD + sInfo.getName() + ChatColor.GREEN + " "+messages[17]+" " + ChatColor.YELLOW + verConv.getVersionName(minVerProt)+ChatColor.GREEN+" "+messages[18]+" "+ChatColor.YELLOW+verConv.getVersionName(maxVerProt)+ChatColor.GREEN+"."));
                        }
                        break;
                    }
                }
                if (!testfor){
                    sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[4]+" "+ChatColor.AQUA+args[0]));
                    return;
                }

            }if (args.length>1){
                sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[3]+"\n"+ChatColor.AQUA+messages[0]+" "+ChatColor.YELLOW+messages[21]));
            }if (args.length<1){
                sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[2]+"\n"+ChatColor.AQUA+messages[0]+" "+ChatColor.YELLOW+messages[21]));
            }
    }
}
