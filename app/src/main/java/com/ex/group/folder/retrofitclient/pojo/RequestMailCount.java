package com.ex.group.folder.retrofitclient.pojo;

import com.google.gson.annotations.SerializedName;

public class RequestMailCount {
    @SerializedName("userId")
    String userId;
    @SerializedName("platformCd")
    String platformCd;


   //response
    @SerializedName("mailCnt")
    public String mailCnt;
    @SerializedName("approvalCnt")
    public String approvalCnt;
    @SerializedName("result")
    public String result;
    @SerializedName("resultMsg")
    public String resultMsg;

    public RequestMailCount(String userId, String  platformCd){
        this.userId= userId;
        this.platformCd= platformCd;
    }
}
