
package app.openconnect.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("ConfigVersion")
    @Expose
    private Double configVersion;
    @SerializedName("ConfigUpdateMsg")
    @Expose
    private String configUpdateMsg;
    @SerializedName("SquidPort")
    @Expose
    private String squidPort;
    @SerializedName("ServHttp")
    @Expose
    private String servHttp;
    @SerializedName("ServSsl")
    @Expose
    private String servSsl;
    @SerializedName("Servers")
    @Expose
    private List<Server> servers = null;
    @SerializedName("Tweaks")
    @Expose
    private List<Tweak> tweaks = null;

    public Double getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(Double configVersion) {
        this.configVersion = configVersion;
    }

    public String getConfigUpdateMsg() {
        return configUpdateMsg;
    }

    public void setConfigUpdateMsg(String configUpdateMsg) {
        this.configUpdateMsg = configUpdateMsg;
    }

    public String getSquidPort() {
        return squidPort;
    }

    public void setSquidPort(String squidPort) {
        this.squidPort = squidPort;
    }

    public String getServHttp() {
        return servHttp;
    }

    public void setServHttp(String servHttp) {
        this.servHttp = servHttp;
    }

    public String getServSsl() {
        return servSsl;
    }

    public void setServSsl(String servSsl) {
        this.servSsl = servSsl;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public List<Tweak> getTweaks() {
        return tweaks;
    }

    public void setTweaks(List<Tweak> tweaks) {
        this.tweaks = tweaks;
    }

}
