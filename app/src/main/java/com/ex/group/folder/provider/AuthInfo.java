package com.ex.group.folder.provider;

import android.util.Log;

import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.util.EncryptUtil;

/*
*
* This class was made as the basis for AUTH class
* author : ParkJoonSang
*
*
* */

public class AuthInfo {

    String TAG = "[com.ex.group.folder.provider.AuthInfo] :::";
    String companyCd;
    String companyNm;
    String encPwd;
    String secretKey;
    String nonce;
    String id;
    String pwd;
    String timestamp;


    public AuthInfo(){


    }


    public String getAuthKey(String mdn, String appId,String secretKey) throws SKTException {
        Log.i("Auth", "===============getAuthKEY================");

        StringBuffer sb = new StringBuffer();
        sb.append("MDN=" + mdn + ":");//
        sb.append("APPID=" + appId + ":");
        sb.append("NONCE=" + getNonce());
        SKTUtil.log(3, "Auth-getAuthKey", sb.toString());
        String ret = null;

        try {
            ret = EncryptUtil.generateHMac("HmacSHA1", secretKey, sb.toString());
            return ret;
        } catch (Exception var6) {
            throw new SKTException(var6, "E002");
        }
    }




    public String getCompanyCd() {
        return companyCd;
    }

    public void setCompanyCd(String companyCd) {
        this.companyCd = companyCd;
    }

    public String getCompanyNm() {
        return companyNm;
    }

    public void setCompanyNm(String compayNm) {
        this.companyNm = compayNm;
    }

    public String getEncPwd() {
        return encPwd;
    }

    public void setEncPwd(String encPwd) {
        this.encPwd = encPwd;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
