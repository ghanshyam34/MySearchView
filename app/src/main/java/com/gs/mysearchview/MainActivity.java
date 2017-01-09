package com.gs.mysearchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.gs.mysearchviewlibrary.MySearchView;

import static android.R.attr.fragment;

public class MainActivity extends AppCompatActivity {

    MySearchView mySearchView;
    ImageButton titlebarRightSearchMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySearchView = (MySearchView) findViewById(R.id.custom_searchview);
        mySearchView.setVisibility(View.GONE);

        titlebarRightSearchMenu = (ImageButton)findViewById(R.id.title_bar_search_menu);

//        mySearchView.setAnimation(true);
        mySearchView.setOpenMenuButton(titlebarRightSearchMenu);
        mySearchView.setSearchviewListener(new MySearchView.SearchViewListener() {

            @Override
            public void onTextFound(CharSequence text, boolean textfound) {

                if (textfound) {


                } else{


                }
            }

            @Override
            public void onShow() {


            }

            @Override
            public void onHide() {


            }

            @Override
            public boolean onTextSubmit(String text,boolean textfound) {

                if (textfound) {


                } else {


                }
                return false;
            }
        });


    }
}
