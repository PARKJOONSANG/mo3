package com.ex.group.folder.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.Space;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.folder.categoryList.CategoryDataList;

import java.util.ArrayList;

import com.ex.group.folder.R;

/*▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄

//   mainActivity 의 category의 리사이클러뷰의 어댑터 이다.//그룹웨어 현장업무, 행정업무, 사내편의에 들어갈 내용을 뿌려준다.
 *   Created by PARKJOOONSANG
  ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄*/


public class MainCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MainCategoryRecyclerViewAdapter.ItemRowHolder> {


    private ArrayList<CategoryDataList> categoryList;
    private Context context;

    public MainCategoryRecyclerViewAdapter(Context context, ArrayList<CategoryDataList> categoryList){
        this.context= context;
        this.categoryList=categoryList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_layout,null);
        ItemRowHolder myHolder = new ItemRowHolder(contentView);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, int position) {
        int[]drawable={R.drawable.main_category_title_groupware,R.drawable.main_category_title_adminwork,
                R.drawable.main_category_title_fieldwork,R.drawable.main_category_title_intracomfort};
        final String sectionName =categoryList.get(position).getCategoryTitle();
        ArrayList SingleItemDataList = categoryList.get(position).getAllItemsInSection();
        holder.main_category_tittle_TextView.setText(sectionName);
        for(int i=0;i<=position;i++){
            holder.main_category_tittle_LinearLayout.setBackgroundResource(drawable[i%4]);
        }
        holder.main_category_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    holder.main_category_LinearLayout_big.setVisibility(View.GONE);
                    holder.main_category_ViewPager_LinearLayout_small.setVisibility(View.VISIBLE);
                }else{
                    holder.main_category_LinearLayout_big.setVisibility(View.VISIBLE);
                    holder.main_category_ViewPager_LinearLayout_small.setVisibility(View.GONE);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return (null !=categoryList ? categoryList.size():0);
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder{
        protected LinearLayout main_category_tittle_LinearLayout;
        protected TextView main_category_tittle_TextView;
        protected View main_category_space1;
        protected LinearLayout main_category_ViewPager_LinearLayout_small;
        protected ViewPager main_category_ViewPager;
        protected LinearLayout main_category_LinearLayout_big;
        protected LinearLayout main_category_gridview_LinearLayout;
        protected GridView main_category_GridView;
        protected LinearLayout main_category_Button_LinerLayout;
        protected LinearLayout main_category_Button_reset,main_category_Button_commit;
        protected View main_category_space2;
        protected View main_category_space3;
        protected LinearLayout main_category_CheckBox_LinearLayout;
        protected CheckBox main_category_CheckBox;


        public ItemRowHolder(View view){
            super(view);
            this.main_category_tittle_LinearLayout=(LinearLayout)view.findViewById(R.id.main_category_tittle_LinearLayout);
            this.main_category_tittle_TextView=(TextView)view.findViewById(R.id.main_category_tittle_TextView);
            this.main_category_space1=(View)view.findViewById(R.id.main_category_space1);
            this.main_category_ViewPager_LinearLayout_small=(LinearLayout)view.findViewById(R.id.main_category_ViewPager_LinearLayout_small);
            this.main_category_ViewPager=(ViewPager)view.findViewById(R.id.main_category_ViewPager);
            this.main_category_LinearLayout_big=(LinearLayout)view.findViewById(R.id.main_category_LinearLayout_big);
            this.main_category_gridview_LinearLayout=(LinearLayout)view.findViewById(R.id.main_category_gridview_LinearLayout);
            this.main_category_GridView=(GridView)view.findViewById(R.id.main_category_GridView);
            this.main_category_Button_LinerLayout=(LinearLayout)view.findViewById(R.id.main_category_Button_LinerLayout);
            this.main_category_Button_reset=(LinearLayout)view.findViewById(R.id.main_category_Button_reset);
            this.main_category_Button_commit=(LinearLayout)view.findViewById(R.id.main_category_Button_commit);
            this.main_category_space2=(View)view.findViewById(R.id.main_category_space2);
            this.main_category_space3=(View)view.findViewById(R.id.main_category_space3);
            this.main_category_CheckBox_LinearLayout=(LinearLayout)view.findViewById(R.id.main_category_CheckBox_LinearLayout);
            this.main_category_CheckBox=(CheckBox)view.findViewById(R.id.main_category_CheckBox);
        }
    }



}
