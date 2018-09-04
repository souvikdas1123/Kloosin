package com.kloosin.activity;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.kloosin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoficationPage extends AppCompatActivity {


    String[] listviewTitle = new String[]{
            "ListView Title 1", "ListView Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };


    int[] listviewImage = new int[]{
            R.drawable.profile_img, R.drawable.profile_img, R.drawable.profile_img, R.drawable.profile_img,
            R.drawable.profile_img, R.drawable.profile_img, R.drawable.profile_img, R.drawable.profile_img,
    };

        String[] listviewShortDescription = new String[]{
                "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
                "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nofication_page);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
           hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));

            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.single_page_notification, from, to);
        ListView androidListView = (ListView) findViewById(R.id.id_list_view);
        androidListView.setAdapter(simpleAdapter);

        }


    }

