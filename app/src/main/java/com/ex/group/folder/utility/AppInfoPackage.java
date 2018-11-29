package com.ex.group.folder.utility;

import android.os.Parcel;
import android.os.Parcelable;


public class AppInfoPackage implements Parcelable {
    public String packageName;
    public String appName;
    public String apkName;

    public AppInfoPackage(){};
    public AppInfoPackage(Parcel src){readFromParcel(src);}

    public static final Creator<AppInfoPackage> CREATOR = new Creator<AppInfoPackage>() {
        @Override
        public AppInfoPackage createFromParcel(Parcel source) {return new AppInfoPackage(source);  }

        @Override
        public AppInfoPackage[] newArray(int size) {
            return new AppInfoPackage[size];
        }
    };

    public void readFromParcel(Parcel src){
        packageName = src.readString();
        appName = src.readString();
        apkName = src.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(appName);
        dest.writeString(apkName);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }
}
