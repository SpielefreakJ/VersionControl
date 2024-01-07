package com.spielefreakj.bungee;

import com.spielefreakj.BungeeMain;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

    ProxyServer bungee = ProxyServer.getInstance();
    private static int minVersion=47;
    private static int maxVersion=9999;
    private final BungeeMain pluginConfig = BungeeMain.getInstance();
    ViaAPI Vapi = Via.getAPI();

    public Events(){
    }

    @EventHandler
    public void onJoin(ServerConnectEvent evt) {
        ProxiedPlayer player = evt.getPlayer();
        Configuration config = pluginConfig.config;
        String pluginPrefix = config.getString("PluginPrefix");
        String fallback = config.getString("Server.Fallback");
        String[] messages = pluginConfig.messages_read;
        //messages=pluginConfig.messages_read;

        //Add configfile
        {
            //file = new File(ProxyServer.getInstance().getPluginsFolder() + "/Version_Control/config.yml");
            try{
                minVersion=Integer.parseInt(config.getString("Server." + evt.getTarget().getName() + ".minVersion"));
                maxVersion=Integer.parseInt(config.getString("Server." + evt.getTarget().getName() + ".maxVersion"));
                fallback =config.getString("Server.Fallback");
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        int version = Vapi.getPlayerVersion(player);
        //756: 1.17.1
        //755: 1.17
        //if (version >= maxVersion || version <= minVersion) {//reversed
        VersionConversion verConv = new VersionConversion();

        if (!evt.getPlayer().hasPermission("VersionControl.bypass.all")&&!evt.getPlayer().hasPermission("VersionControl.bypass."+evt.getTarget().getName())) {
            if (version > maxVersion || version < minVersion) {//test if user has higher or lower version than server is set to
                if (player.getServer() == null) {//when player is connecting
                    if (fallback.equalsIgnoreCase("kick")){
                        if (minVersion==maxVersion){//kick with single protoc message
                            evt.getPlayer().disconnect(new TextComponent(ChatColor.WHITE+messages[28]+" "+ChatColor.GOLD+verConv.getVersionName(minVersion)));
                            return;
                        }else {//kick with multi protoc message
                            evt.getPlayer().disconnect(new TextComponent(ChatColor.WHITE+messages[26]+" "+ChatColor.GOLD+verConv.getVersionName(minVersion)+" "+ChatColor.WHITE+messages[27]+" "+ChatColor.GOLD+verConv.getVersionName(maxVersion)));
                            return;
                        }
                    }else {//when fallback was not kick, force connect to server provided
                        evt.setTarget(bungee.getServerInfo(fallback));
                    }
                    return;
                }
                if (minVersion == maxVersion) {//Only for one specific version
                    player.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.GOLD+" " + evt.getTarget().getName() + ChatColor.RED + " "+messages[8]+" " + ChatColor.GOLD + verConv.getVersionName(minVersion) + ChatColor.RED + "!"));
                } else {//Only for multiple specific versions
                    player.sendMessage(new TextComponent(ChatColor.DARK_AQUA+ pluginPrefix +ChatColor.GOLD+" " + evt.getTarget().getName() + ChatColor.RED + " "+messages[6]+" " + ChatColor.GOLD + verConv.getVersionName(minVersion) + ChatColor.RED + " "+messages[7]+" " + ChatColor.GOLD + verConv.getVersionName(maxVersion) + ChatColor.RED + "!"));
                }
                evt.setCancelled(true);
            }
        }
    }
}
