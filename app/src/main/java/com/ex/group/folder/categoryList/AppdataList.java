package com.ex.group.folder.categoryList;

import android.graphics.drawable.Drawable;

import com.ex.group.folder.R;

public class AppdataList {


   private boolean isFixed;



    private String unCheckedCount;
   private int appCondition;
   private Drawable icon;
   private int drawableicon;
   private String appVer;
   private String appId;
   private String appNm;
   private String packageNm;
   private String hybridYn;
   private String hybridUrl;
   private String installUrl;
   private String  iconURl;
   private String needUpdate;


    public boolean isFixed() {
        return isFixed;
    }
    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }
    public String getUnCheckedCount() {
        return unCheckedCount;
    }
    public void setUnCheckedCount(String unCheckedCount) {
        this.unCheckedCount = unCheckedCount;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public String getEssentilaInstCd() {
        return essentilaInstCd;
    }
    public void setEssentilaInstCd(String essentilaInstCd) {
        this.essentilaInstCd = essentilaInstCd;
    }
    public String getEssentialYn() {
        return essentialYn;
    }
    public void setEssentialYn(String essentialYn) {
        this.essentialYn = essentialYn;
    }
    private String essentilaInstCd;
    private String essentialYn;
    public String getNeedUpdate() {
        return needUpdate;
    }
    public void setNeedUpdate(String needUpdate) {
        this.needUpdate = needUpdate;
    }
    public int getAppCondition() {
        return appCondition;
    }
    public void setAppCondition(int appCondition) {
        this.appCondition = appCondition;
    }
    public String getAppVer() {
        return appVer;
    }
    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }
    public int getDrawableicon(){return drawableicon;}
    public void setDrawableIcon(int drawableicon){this.drawableicon =drawableicon;}
    public String getInstallUrl() {
        return installUrl;
    }
    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }
    public String getIconURl() {
        return iconURl;
    }
    public void setIconURl(String iconURl) {
        this.iconURl = iconURl;
    }
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    private String iconUrl;
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getAppNm() {
        return appNm;
    }
    public void setAppNm(String appNm) {
        this.appNm = appNm;
    }
    public String getPackageNm() {
        return packageNm;
    }
    public void setPackageNm(String packageNm) {
        this.packageNm = packageNm;
    }
    public String getHybridYn() {
        return hybridYn;
    }
    public void setHybridYn(String hybridYn) {
        this.hybridYn = hybridYn;
    }
    public String getHybridUrl() {
        return hybridUrl;
    }
    public void setHybridUrl(String hybridUrl) {
        this.hybridUrl = hybridUrl;
    }
}
