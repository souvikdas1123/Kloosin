package com.kloosin.utility.chipview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.kloosin.R;

public class ChipView extends RelativeLayout{

    private FlexboxLayout flChips;
    private TextView etSearch;
    private ListView lvList;
    private ChipAdapter adapter;
    private SimpleSearchAdapter simpleSearchAdapter;
    private TextView topCount;

    public ChipView(Context context) {
        super(context);
        init(null);
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        View view = inflate(getContext(), R.layout.chip_view,this);
        flChips = view.findViewById(R.id.flChips);
        etSearch = view.findViewById(R.id.etSearch);
        lvList = view.findViewById(R.id.lvList);
        topCount    =   view.findViewById(R.id.topCount);
    }

    public void setAdapter(ChipAdapter adapter){
        this.adapter = adapter;
        adapter.setChipView(this);
        simpleSearchAdapter = new SimpleSearchAdapter(getContext(),adapter);
        lvList.setAdapter(simpleSearchAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                simpleSearchAdapter.getFilter().filter(editable.toString());
            }
        });
    }

    public void notifyDataSetChanged(){
        refreshFlexbox();
        simpleSearchAdapter.notifyDataSetChanged();
    }

    private void refreshFlexbox(){

        MarginLayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 20, 20);


        for(int i = flChips.getChildCount() - 1; i >= 0;i--){
            if(flChips.indexOfChild(etSearch) != i){
                flChips.removeViewAt(i);
            }
        }
        int _checkedCounter = 0;
        for(int i = 0;i < adapter.getCount();i++){
            if(adapter.isSelected(i)) {
                View v = adapter.createChip(getContext(), i);
                v.setLayoutParams( params );
                flChips.addView(v,0);
                _checkedCounter++;
            }
        }
        topCount.setText( String.valueOf( _checkedCounter ) );
    }
}
