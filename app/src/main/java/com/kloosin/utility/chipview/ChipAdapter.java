package com.kloosin.utility.chipview;

import android.content.Context;
import android.view.View;

import com.kloosin.utility.chipview.modal.ContactChip;

import java.util.ArrayList;


public abstract class ChipAdapter {

    private ChipView chipView;

    public ArrayList<ContactChip> data = new ArrayList<>();
    public abstract ContactChip getItem(int pos);
    public abstract boolean isSelected(int pos);

    public abstract View createSearchView(Context context,boolean is_checked,int pos);
    public abstract View createChip(Context context,int pos);

    public void setChipView(ChipView chipView){
        this.chipView = chipView;
    }


    public void refresh(){
        chipView.notifyDataSetChanged();
    }

    public abstract int getCount();

}
