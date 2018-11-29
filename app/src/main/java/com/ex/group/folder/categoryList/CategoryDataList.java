package com.ex.group.folder.categoryList;

import java.util.ArrayList;

public class CategoryDataList {

    private String categoryTitle;
    private boolean categoryShowingState;
    private ArrayList<SingleItemDataList> allItemsInSection;

    public CategoryDataList(){

    }
    public CategoryDataList(String categoryTitle,ArrayList<SingleItemDataList>allItemInSection,boolean categoryShowingState){
        this.categoryTitle=categoryTitle;
        this.categoryShowingState=categoryShowingState;
        this.allItemsInSection=allItemInSection;
    }






    //getter setter

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public boolean isCategoryShowingState() {
        return categoryShowingState;
    }

    public void setCategoryShowingState(boolean categoryShowingState) {
        this.categoryShowingState = categoryShowingState;
    }

    public ArrayList<SingleItemDataList> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SingleItemDataList> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
