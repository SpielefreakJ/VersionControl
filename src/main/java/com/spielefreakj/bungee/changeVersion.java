package com.spielefreakj.bungee;

import com.spielefreakj.BungeeMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class changeVersion extends Command {

    private final BungeeMain pluginConfig = BungeeMain.getInstance();
    private static int minVersion=47;
    private static int maxVersion=9999;

    public changeVersion() {
        super("changeversion", "VersionControl.change", "changever", "cv", "changev");
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        Configuration config = pluginConfig.config;
        String pluginPrefix = config.getString("PluginPrefix");
        String[] messages = pluginConfig.messages_read;
        boolean testfor = false;
        try {
            if (args.length==3){
                try {
                    minVersion = Integer.parseInt(args[1]);
                }catch (Exception e){
                    sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[14]));
                    testfor = true;
                }
                try {
                    maxVersion = Integer.parseInt(args[2]);
                }catch (Exception e){
                    sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[15]));
                    testfor = true;
                }
                if (testfor) return;

                if (minVersion>maxVersion){
                    sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[13]));
                    return;
                }

                for (ServerInfo sInfo : ProxyServer.getInstance().getServers().values()){
                    if (args[0].equalsIgnoreCase(sInfo.getName())){

                        config.set("Server."+sInfo.getName()+".minVersion", args[1]);
                        config.set("Server."+sInfo.getName()+".maxVersion", args[2]);
                        boolean savetest = pluginConfig.saveConfig();
                        if (savetest){
                            //sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.WHITE+" "+messages[23]));
                        }else {
                            sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.WHITE+ " Error during save. Please check logs!"));
                        }

                        VersionConversion verConv = new VersionConversion();

                        if (minVersion==maxVersion) {
                            sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.GOLD+" "+messages[9]+" "+ChatColor.AQUA+sInfo.getName()+ChatColor.GOLD+" "+messages[10]+" "+ChatColor.GREEN+verConv.getVersionName(minVersion)));
                        }else {
                            sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.GOLD+" "+messages[9]+" "+ChatColor.AQUA+sInfo.getName()+ChatColor.GOLD+" "+messages[10]+" "+ChatColor.DARK_AQUA+"\n"+messages[11]+" "+ChatColor.GREEN+verConv.getVersionName(minVersion)+ChatColor.DARK_AQUA+"\n"+messages[12]+" "+ChatColor.GREEN+verConv.getVersionName(maxVersion)));
                        }
                        return;
                    }
                }
                sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[4]+" "+ChatColor.AQUA+args[0]));
                return;
            }if (args.length>3){
                sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[3]+"\n"+ChatColor.AQUA+messages[0]+" "+ChatColor.YELLOW+messages[20]));
                return;
            }
            sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[2]+"\n"+ChatColor.AQUA+messages[0]+" "+ChatColor.YELLOW+messages[20]));

        }catch (Exception e){
            sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.RED+" "+messages[1]+"\n"+ChatColor.AQUA+messages[0]+" "+ChatColor.YELLOW+messages[20]));
            e.printStackTrace();
        }
    }
}
