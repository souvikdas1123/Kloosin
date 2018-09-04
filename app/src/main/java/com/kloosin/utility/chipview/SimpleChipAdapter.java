package com.kloosin.utility.chipview;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kloosin.R;
import com.kloosin.utility.chipview.modal.ContactChip;

import java.util.ArrayList;

public class SimpleChipAdapter extends ChipAdapter{

    ArrayList<ContactChip>search_data = new ArrayList<>();
    ArrayList<ContactChip>chips = new ArrayList<>();

    public SimpleChipAdapter(ArrayList<ContactChip>search_data){
        this.search_data = search_data;
        this.data = search_data;
    }

    @Override
    public ContactChip getItem(int pos) {
        return search_data.get(pos);
    }

    @Override
    public boolean isSelected(int pos) {
        if(chips.contains(search_data.get(pos))) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public View createSearchView(Context context, boolean is_checked, final int pos) {
        View view = View.inflate(context,R.layout.search,null);

        final ContactChip _data = search_data.get(pos);

        RelativeLayout _single_contact_layout = (RelativeLayout) view.findViewById(R.id._single_contact_layout);
        ImageView _imageView = view.findViewById( R.id.contact_profile_picture );
        TextView _contactText = view.findViewById( R.id.contact_name );
        TextView _contactNumber = view.findViewById( R.id.contact_number );
        CheckBox _contact_check = view.findViewById(R.id.contact_check);

        _contactText.setText( _data.getContactName() );
        _contactNumber.setText( _data.getContactNumber() );
        _contact_check.setChecked( _data.isCheck() );
        _single_contact_layout.setTag( _data.isCheck() );

        _single_contact_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _data.setCheck( !_data.isCheck() );
               if( _data.isCheck() ){
                    chips.add(search_data.get(pos));
                    refresh();
                }else{
                    chips.remove(search_data.get(pos));
                    refresh();
                }
            }
        });
        return view;
    }

    @Override
    public View createChip(Context context, final int pos) {
        View view = View.inflate(context,R.layout.chip_v2,null);
        TextView tvChip = view.findViewById(R.id.tvChip);
        tvChip.setText((String)search_data.get(pos).getContactName());
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chips.remove(search_data.get(pos));
                refresh();
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return search_data.size();
    }

}
