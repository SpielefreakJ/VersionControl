package com.spielefreakj.bungee;

import com.spielefreakj.BungeeMain;
import net.md_5.bungee.config.Configuration;

public class VersionConversion {

    private Integer protocolNumber = 0;
    private String versionName = "0.0";
    private final BungeeMain pluginConfig = BungeeMain.getInstance();
    private Configuration config;

    public VersionConversion(){
        config = pluginConfig.config;
    }

    public void setProtocolNumber(Integer protocolNr){
        protocolNumber=protocolNr;
        try {
            versionName=config.getString("Versions." + protocolNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setVersionName(String versionNm){
        versionName=versionNm;
    }

    public Integer getProtocolNumber(){
        return protocolNumber;
    }

    public String getVersionName(Integer protocolNr){
        try {
            versionName=config.getString("Versions." + protocolNr);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (versionName.equals("0.0") || versionName.equals("")) return "Protocol "+protocolNr.toString();

        return versionName;
    }
}
