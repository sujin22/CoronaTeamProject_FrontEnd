package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class SettingData {

    @SerializedName("user_id")
    public String m_strUserID;

    @SerializedName("radius_setting")
    public int m_iRadius;

    @SerializedName("period_setting")
    public int m_iPeriod;

    public SettingData() {}
    public SettingData(String strUserID, int iRadius, int iPeriod)
    {
        this.m_strUserID = strUserID;
        this.m_iRadius = iRadius;
        this.m_iPeriod = iPeriod;
    }
}