package com.gs.mysearchviewlibrary;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Ghanshyam on 11/3/2016.
 */
public class MySearchView extends RelativeLayout implements View.OnClickListener {

    Context mContext;

    EditText searchEditText;
    ImageButton clearTextIB;
    LinearLayout childLayout1;
    ImageView image;
    View bottomHiddenView;

    SearchViewListener searchViewListener;

    boolean focusClearing;
    boolean isAnimation = false;

    public MySearchView(Context context) {
        super(context);

        mContext = context;

        initview(context);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initview(context);
    }

    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initview(context);
    }

    public void initview(Context context){

        LinearLayout rootview = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootview.setLayoutParams(layoutParams);
        rootview.setOrientation(LinearLayout.HORIZONTAL);

        childLayout1 = new LinearLayout(context);
        LinearLayout.LayoutParams chil2param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chil2param.gravity = Gravity.CENTER;
        childLayout1.setLayoutParams(chil2param);
        int px = dpToPx(12);
        childLayout1.setPadding(px,px,px,px);
        childLayout1.setGravity(Gravity.CENTER);

//        int resId = getResources().getIdentifier("cross","drawable",context.getPackageName());
//        Drawable d = context.getResources().getDrawable(resId);

        image = new ImageView(context);
        image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        image.setImageResource(R.mipmap.back_navigation);
        image.setBackgroundColor(Color.TRANSPARENT);


        LinearLayout childLayout2 = new LinearLayout(context);
        LinearLayout.LayoutParams layoutPara = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutPara.weight = 1f;
        childLayout2.setGravity(Gravity.CENTER);
        childLayout2.setLayoutParams(layoutPara);
        childLayout2.setOrientation(LinearLayout.VERTICAL);
        childLayout2.setPadding(0,dpToPx(4),0,dpToPx(4));


        RelativeLayout relativeLayout = new RelativeLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        relativeLayout.setLayoutParams(lp);
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL);



        searchEditText = new EditText(context);
        RelativeLayout.LayoutParams lpet = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpet.addRule(RelativeLayout.CENTER_IN_PARENT);
        searchEditText.setLayoutParams(lpet);
        searchEditText.setHint("Search");
        searchEditText.setPadding(dpToPx(4),dpToPx(4),dpToPx(4),dpToPx(4));
        searchEditText.setBackgroundColor(Color.parseColor("#00000000"));
//        editText.setTextColor(context.getResources().getColor(android.R.color.black));
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setSingleLine();
        searchEditText.setHintTextColor(Color.BLACK);


        clearTextIB = new ImageButton(context);
        RelativeLayout.LayoutParams lpetIv = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpetIv.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpetIv.addRule(RelativeLayout.CENTER_VERTICAL);
        lpetIv.rightMargin = dpToPx(5);
        clearTextIB.setLayoutParams(lpetIv);
        clearTextIB.setImageResource(R.mipmap.ic_action_navigation_close);
        clearTextIB.setBackgroundColor(Color.TRANSPARENT);
        clearTextIB.setVisibility(View.INVISIBLE);

        childLayout1.addView(image);

        relativeLayout.addView(searchEditText);
        relativeLayout.addView(clearTextIB);
        childLayout2.addView(relativeLayout);

        rootview.addView(childLayout1);
        rootview.addView(childLayout2);

        addView(rootview);

        childLayout1.setOnClickListener(this);
        clearTextIB.setOnClickListener(this);

        setVisibility(GONE);
    }

    public void setSearchviewListener(SearchViewListener listener){
        this.searchViewListener = listener;

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() >= 0) {
                    clearTextIB.setVisibility(VISIBLE);
                    Log.w(MySearchView.class.getSimpleName(),"search=  "+s);
                    searchViewListener.onTextFound(s,true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() <= 0) {
                    clearTextIB.setVisibility(View.INVISIBLE);
                    Log.w(MySearchView.class.getSimpleName(),"default=  "+s);
                    searchViewListener.onTextFound(s,false);
                }
            }
        });


        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });
    }

    private void onSubmitQuery() {
        CharSequence query = searchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (searchViewListener == null || !searchViewListener.onTextSubmit(query.toString(),true)) {
                searchEditText.setText(null);
//                hide();
            }
        }
    }



    public void setAnimation(boolean animation){
        this.isAnimation = animation;
    }

    public void setOpenMenuButton(View view){

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
    }

    public void show(){

        if(bottomHiddenView != null){
            bottomHiddenView.setVisibility(View.GONE);
        }

        if(isAnimation){
            getAnimation(this,getWidth(),0,0,0,500,true);
        }

        if(searchEditText != null){
            searchEditText.requestFocus();
            showKeyboard(searchEditText);
            searchEditText.setText("");
        }

        if(searchViewListener != null)
            searchViewListener.onShow();

        if(!isAnimation)
          setVisibility(VISIBLE);
    }

    public void hide(){

        if(bottomHiddenView != null){
            bottomHiddenView.setVisibility(View.VISIBLE);
        }

        if(isAnimation){
            getAnimation(this,0,getWidth(),0,0,500,false);
        }

        if(searchEditText != null){
            hideKeyboard(searchEditText);
            searchEditText.setText(null);
        }

        if(searchViewListener != null)
            searchViewListener.onHide();

        if(!isAnimation)
          setVisibility(GONE);
    }


    @Override
    public void onClick(View view) {

        if(view == childLayout1){
            hide();
        }else if(view == clearTextIB){
            searchEditText.setText(null);
        }
    }

    public void setHiddenBottomView(View view){
        this.bottomHiddenView = view;
    }


    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (focusClearing) return false;
        if (!isFocusable()) return false;
        return searchEditText.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        focusClearing = true;
        hideKeyboard(searchEditText);
        super.clearFocus();
        searchEditText.clearFocus();
        focusClearing = false;
    }

    public void setCursorDrawable(int drawable) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(searchEditText, drawable);
        } catch (Exception ignored) {}
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(View view) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public void setFont(String fontname){
        try {
            Typeface fontsStyle = Typeface.createFromAsset(mContext.getAssets(),fontname);
            searchEditText.setTypeface(fontsStyle);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setTextColor(int color){
        searchEditText.setTextColor(color);
    }

    public void setBackButton(int resid){
        image.setImageResource(resid);
    }

    public void setClearTextButtton(int resid){
        clearTextIB.setImageResource(resid);
    }

    public void setBackgroundColor(int color){
        setBackgroundColor(color);
    }

    public interface SearchViewListener{
        public void onTextFound(CharSequence text, boolean textfound);
        public void onShow();
        public void onHide();
        public boolean onTextSubmit(String text, boolean textfound);
    }

    public boolean isShowing(){
       return getVisibility() == VISIBLE ? true : false;
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    private void getAnimation(final View viewLayout, int x1, int x2,
                              int y1, int y2, int durationInMilisecond, final boolean isShowing) {

        Animation animation = new TranslateAnimation(x1, x2, y1, y2);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

//                viewLayout.setVisibility(VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewLayout.clearAnimation();
                if(isShowing)
                    viewLayout.setVisibility(VISIBLE);
                else
                    viewLayout.setVisibility(GONE);
            }
        });

        animation.setDuration(durationInMilisecond);
        viewLayout.startAnimation(animation);
//        viewLayout.setVisibility(View.VISIBLE);
    }
}