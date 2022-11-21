package com.nwsapp.shadeiojava;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Random;

public class MyGridAdapter extends BaseAdapter implements View.OnClickListener {
    int colCount,rowCount;
    Context context;
    List<MyCol> listOfColor;
    List<MyCol> sortedListOfColor;
    Button start_game_btn;
    GameOverListener gm;
    LinearLayout heart_life;
    int heart_count;
    int ptr=0;
    public MyGridAdapter(Context context, List<MyCol> listOfColor, List<MyCol> sortedListOfColor, int rowCount, int colCount, Button start_game_btn, GameOverListener gm, LinearLayout heart_life,int heart_count){
        this.context=context;
        this.listOfColor=listOfColor;
        this.rowCount=rowCount;
        this.colCount=colCount;
        this.sortedListOfColor=sortedListOfColor;
        this.start_game_btn=start_game_btn;
        this.gm=gm;
        this.heart_life=heart_life;
        this.heart_count=heart_count;

        heartSetup();
    }

    public void heartSetup(){
        for(int i=0;i<heart_count;i++){
            ImageView hrt= (ImageView) LayoutInflater.from(context).inflate(R.layout.heart_imageview,heart_life,false);
            heart_life.addView(hrt);
        }
    }


    @Override
    public int getCount() {
        return colCount*rowCount;
    }

    @Override
    public Object getItem(int i) {
        return listOfColor.get(i);
    }
    @Override
    public long getItemId(int i) {
        return 100;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MaterialCardView lf= (MaterialCardView) LayoutInflater.from(context).inflate(R.layout.one_grid_item,viewGroup,false);
        int width=context.getResources().getDisplayMetrics().widthPixels;
        lf.setStrokeWidth((int) (width/150f));
        lf.setTag(i);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lf.setVisibility(View.VISIBLE);
                Animation anim = new ScaleAnimation(
                        0f, 1f, // Start and end values for the X axis scaling
                        0f, 1f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(500);
                lf.startAnimation(anim);
            }
        },Math.abs(new Random().nextInt(500)));

        ImageButton img=lf.findViewById(R.id.imgBtn);
        img.setBackgroundColor(Color.rgb(listOfColor.get(i).r,listOfColor.get(i).g,listOfColor.get(i).b));
        img.setOnClickListener(this);
        int gridWidth=viewGroup.getWidth();
        int gridHeight=viewGroup.getHeight();
        lf.getLayoutParams().width=(gridWidth-(int)(width/50f)*(colCount-1))/colCount;
        lf.getLayoutParams().height=(gridHeight-(int)(width/50f)*(rowCount-1))/rowCount;
        lf.setVisibility(View.GONE);
        return lf;
    }

    int current_vol_status=1;


    @Override
    public void onClick(View view) {
        ColorDrawable cd= (ColorDrawable) view.getBackground();
        int col=cd.getColor();
        int red=Color.red(col);
        int green=Color.green(col);
        int blue=Color.blue(col);

        int reqRed=sortedListOfColor.get(ptr).r;
        int reqGreen=sortedListOfColor.get(ptr).g;
        int reqBlue=sortedListOfColor.get(ptr).b;

        MaterialCardView cv= (MaterialCardView) view.getParent();
        if(red==reqRed && blue==reqBlue && green==reqGreen){
            ((CardView)view.getParent()).setVisibility(View.GONE);
            if(current_vol_status==1){
                MediaPlayer mp=MediaPlayer.create(context,R.raw.correct_choice_43861);
                mp.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mp.release();
                    }
                },2000);
            }

            ptr++;
            cv.setOnClickListener(null);
            view.setOnClickListener(null);
            Animation anim = new ScaleAnimation(
                    1f, 0f, // Start and end values for the X axis scaling
                    1f, 0f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(1000);
            cv.startAnimation(anim);
            if(ptr==colCount*rowCount){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gm.gameOverWithWin();
                    }
                },500);
            }
        }
        else{
            Log.i("het","yu");
            if(current_vol_status==1){
                MediaPlayer mp=MediaPlayer.create(context,R.raw.mixkit_wrong_electricity_buzz_955);
                mp.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mp.release();
                    }
                },2000);
            }

            int tag= (int) cv.getTag();
            tag++;
            Log.i("hi",String.valueOf(tag));
            if(tag%colCount==0){
                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_right_edge);
                cv.startAnimation(shake);
            }
            else if((tag-1)%colCount==0){
                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_left_edge);
                cv.startAnimation(shake);
            }
            else{
                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                cv.startAnimation(shake);
            }
            Toast.makeText(context, "The inv is wrong", Toast.LENGTH_SHORT).show();

            heart_life.removeViewAt(0);
            heart_life.addView(LayoutInflater.from(context).inflate(R.layout.empty_heart_imageview,heart_life,false));
            heart_count--;
            if(heart_count==0){
                heart_life.removeAllViews();
                gm.gameOverWithLose();
            }
        }
    }
}
