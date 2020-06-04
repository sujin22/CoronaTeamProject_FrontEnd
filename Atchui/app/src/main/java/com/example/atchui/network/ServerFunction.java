package com.example.atchui.network;

import android.util.Log;
import android.widget.Toast;

import com.example.atchui.SettingActivity;
import com.example.atchui.database.AnalData;
import com.example.atchui.database.AnalResponse;
import com.example.atchui.database.PatientRouteData;
import com.example.atchui.database.PatientRouteResponse;
import com.example.atchui.database.SettingData;
import com.example.atchui.database.SettingResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerFunction {
    public static final int VAL_RADIUS = 0;
    public static final int VAL_PERIOD = 1;

    public ServiceAPI service;
    public PatientRouteResponse patientRouteResponse;

    public ArrayList<AnalData> lstAnal;
    public JSONArray jArray;

    public String user_id = "";
    private boolean bInit = false;

    public void Initialize(ServiceAPI service) {
        this.service = service;
        bInit = true;

        patientRouteResponse = new PatientRouteResponse();

        jArray = new JSONArray();
        lstAnal = new ArrayList<AnalData>();

    }
    public void SetUserID(String user_id) { this.user_id = user_id; }

    // 확진자 정보 관련
    public void GetLatestPatientRouteData() {
        if(!bInit)
            return;

        PatientRouteData data = new PatientRouteData();
        service.cnfPatientRoute(data).enqueue(new Callback<PatientRouteResponse>() {
            @Override
            public void onResponse(Call<PatientRouteResponse> call, Response<PatientRouteResponse> response) {
                patientRouteResponse.m_cnfID = response.body().m_cnfID;
                patientRouteResponse.m_cnfRouteID = response.body().m_cnfRouteID;
                patientRouteResponse.m_address = response.body().m_address;
                patientRouteResponse.m_latitude = response.body().m_latitude;
                patientRouteResponse.m_longitude = response.body().m_longitude;
                patientRouteResponse.m_locationName = response.body().m_locationName;
                patientRouteResponse.m_visitDatetime = response.body().m_visitDatetime;

                Log.e("GET RESPONSE", response.body().m_latitude + "");

               // Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Call<PatientRouteResponse> call, Throwable t) {
                Log.e("데이터 베이스 가져오기 실패", t.getMessage());
            }
        });
    }

    // 설정 관련
    public void SendUserOption(String user_id, int radius, int period) {
        if(!bInit)
            return;

        SettingData data = new SettingData(user_id, radius, period);
        service.userOption(data).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                SettingResponse result = response.body();

                Log.e("setting database 넣기 성공",result.getMessage());
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                Log.e("setting database 넣기 실패", t.getMessage());
            }
        });
    }

    public void UpdateUserOption(int VALUE_ID, int value) {
        if(!bInit)
            return;

        SettingData data = new SettingData();
        data.m_strUserID = user_id;

        switch (VALUE_ID){
            case VAL_RADIUS:
                data.m_iRadius = value;
                service.userOptionUpdateRad(data).enqueue(new Callback<SettingResponse>() {
                    @Override
                    public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                        SettingResponse result = response.body();
                        Log.e("Rad Update 성공",result.getMessage());
                    }

                    @Override
                    public void onFailure(Call<SettingResponse> call, Throwable t) {
                        Log.e("Rad Update 실패", t.getMessage());
                    }
                });
                break;
            case VAL_PERIOD:
                data.m_iPeriod = value;
                service.userOptionUpdatePeriod(data).enqueue(new Callback<SettingResponse>() {
                    @Override
                    public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                        SettingResponse result = response.body();
                        Log.e("Period Update 성공",result.getMessage());
                    }

                    @Override
                    public void onFailure(Call<SettingResponse> call, Throwable t) {
                        Log.e("Period Update 실패", t.getMessage());
                    }
                });
                break;
        }
    }

    // 분석결과(알림목록) 가져오기
    public void GetAnalysisList() {
        if(!bInit)
            return;

        AnalData data = new AnalData();
        service.GetAnalList(data).enqueue(new Callback<AnalResponse>() {
            @Override
            public void onResponse(Call<AnalResponse> call, Response<AnalResponse> response) {
                if(!lstAnal.isEmpty())
                    lstAnal.clear();

                Log.e("성공", response.body().strArray);
                try {
                    response.body().ConvertToData(lstAnal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AnalResponse> call, Throwable t) {
                Log.e("실패", "AnalList 가져오기");
            }
        });
    }

    // 싱글톤
    private static ServerFunction instance = null;

    public static synchronized ServerFunction getInstance() {
        if(null == instance)
            instance = new ServerFunction();
        return instance;
    }
}
