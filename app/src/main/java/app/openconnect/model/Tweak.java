
package app.openconnect.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tweak {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ModeHttp")
    @Expose
    private Boolean modeHttp;
    @SerializedName("UseCustom")
    @Expose
    private Boolean useCustom;
    @SerializedName("CustomProxy")
    @Expose
    private String customProxy;
    @SerializedName("Payload")
    @Expose
    private String payload;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getModeHttp() {
        return modeHttp;
    }

    public void setModeHttp(Boolean modeHttp) {
        this.modeHttp = modeHttp;
    }

    public Boolean getUseCustom() {
        return useCustom;
    }

    public void setUseCustom(Boolean useCustom) {
        this.useCustom = useCustom;
    }

    public String getCustomProxy() {
        return customProxy;
    }

    public void setCustomProxy(String customProxy) {
        this.customProxy = customProxy;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
