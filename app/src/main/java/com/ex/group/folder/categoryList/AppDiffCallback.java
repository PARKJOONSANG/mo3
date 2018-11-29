package com.ex.group.folder.categoryList;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

public class AppDiffCallback extends DiffUtil.Callback {
    
    private final List<AppdataList> oldList;
    private final List<AppdataList> newList;
    
    public AppDiffCallback(List<AppdataList>old, List<AppdataList> newList){
        this.oldList = old;
        this.newList =newList;
    }
    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getPackageNm()== newList.get(newItemPosition).getPackageNm();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final AppdataList oldList_ = oldList.get(oldItemPosition);
        final AppdataList newList_ = newList.get(newItemPosition);


        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // implement method if you're going to use ItemAnimvator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
