package com.ex.group.folder.retrofitclient.pojo;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class  RequestInitInfo {
    @SerializedName("userId")
    String userId;
    @SerializedName("userType")
    String userType;
    @SerializedName("platformCd")
    String platformCd;
    @SerializedName("deviceType")
    String deviceType;


   //response

    @SerializedName("result")
    public String result;

    @SerializedName("resultMsg")
    public String resultMsg;

    @SerializedName("appInfoList")
    public List<AppInfoList> appInfoList = new ArrayList<>();

    @SerializedName("noticeList")
    public List<NoticeList> noticeList =new ArrayList<>();



    //public class  AppInfoList implements Comparable<AppInfoList> {
    public class  AppInfoList{

        public String getNeedUpdate() {
            return needUpdate;
        }

        public void setNeedUpdate(String needUpdate) {
            this.needUpdate = needUpdate;
        }

        String needUpdate;


        @SerializedName("fileSize")
        String fileSize;

        @SerializedName("hybridYn")
        String hybridYn;

        @SerializedName("installUrl")
        String installUrl;

        @SerializedName("appNm")
        String appNm;

        @SerializedName("appId")
        String appId;

        @SerializedName("appVer")
        String appVer;

        @SerializedName("downCount")
        String downCount;

        @SerializedName("packageNm")
        String packageNm;

        @SerializedName("iconUrl")
        String iconUrl;

        @SerializedName("hybridUrl")
        String hybridUrl;

        @SerializedName("vpnYn")
        String vpnYn;

        @SerializedName("appType")
        String appType;






        public String getVpnYn() {
            return vpnYn;
        }

        public void setVpnYn(String vpnYn) {
            this.vpnYn = vpnYn;
        }

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getHybridYn() {
            return hybridYn;
        }

        public void setHybridYn(String hybridYn) {
            this.hybridYn = hybridYn;
        }

        public String getInstallUrl() {
            return installUrl;
        }

        public void setInstallUrl(String installUrl) {
            this.installUrl = installUrl;
        }

        public String getAppNm() {
            return appNm;
        }

        public void setAppNm(String appNm) {
            this.appNm = appNm;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppVer() {
            return appVer;
        }

        public void setAppVer(String appVer) {
            this.appVer = appVer;
        }

        public String getDownCount() {
            return downCount;
        }

        public void setDownCount(String downCount) {
            this.downCount = downCount;
        }

        public String getPackageNm() {
            return packageNm;
        }

        public void setPackageNm(String packageNm) {
            this.packageNm = packageNm;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getHybridUrl() {
            return hybridUrl;
        }

        public void setHybridUrl(String hybridUrl) {
            this.hybridUrl = hybridUrl;
        }

/*

        @Override
        public int compareTo(@NonNull AppInfoList appInfoList) {
            AppInfoList target = appInfoList;
            int targetDownCount =Integer.parseInt(target.getDownCount());
            if(Integer.parseInt(this.downCount)<targetDownCount){
                return -1;
            }else if(Integer.parseInt(this.downCount)>targetDownCount){
                return 1;
            }else{
                return 0;
            }

        }
*/
    }

    public class NoticeList{

        @SerializedName("noticeId")
        String noticeId;

        @SerializedName("noticeType")
        String noticeType;

        @SerializedName("noticeTitle")
        String noticeTitle;

        @SerializedName("noticeContent")
        String noticeContent;

        public String getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(String noticeId) {
            this.noticeId = noticeId;
        }

        public String getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        public String getNoticeTitle() {
            return noticeTitle;
        }

        public void setNoticeTitle(String noticeTitle) {
            this.noticeTitle = noticeTitle;
        }

        public String getNoticeContent() {
            return noticeContent;
        }

        public void setNoticeContent(String noticeContent) {
            this.noticeContent = noticeContent;
        }
    }


    public RequestInitInfo(String userId, String userType, String  platformCd){
        this.userId= userId;
        this.userType =userType;
        this.platformCd= platformCd;
    }




}
