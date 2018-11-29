package com.ex.group.folder.retrofitclient.pojo;

import com.google.gson.annotations.SerializedName;

public class RequestLogin {
    @SerializedName("userId")
    String userId;
    @SerializedName("pwd")
    String pwd;
    @SerializedName("platformCd")
    String platformCd;
    @SerializedName("mdn")
    String mdn;


   //response
    @SerializedName("userNm")
    public String userNm;
    @SerializedName("emailAddr")
    public String emailAddr;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("deptCd")
    public String deptCd;
    @SerializedName("photoUrl")
    public String photoUrl;
    @SerializedName("userType")
    public String userType;
    @SerializedName("authResult")
    public String authResult;
    @SerializedName("result")
    public String result;
    @SerializedName("resultMsg")
    public String resultMsg;

    @SerializedName("lastLoginDt")
    public String lastLoginDt;

    @SerializedName("secretKey")
    public String secretKey;

    @SerializedName("encPwd")
    public String encPwd;

    @SerializedName("nonceUpdateDt")
    public String nonceUpdateDt;

    @SerializedName("nonce")
    public String nonce;

    public RequestLogin(String userId, String pwd){
        this.userId= userId;
        this.pwd = pwd;
    }
}
