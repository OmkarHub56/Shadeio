package com.nwsapp.shadeiojava;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    GridView gdv;
    Button start_game_btn;
//    MediaPlayer wrong_answer_buzzer,correct_answer_tick;
    int ptr=0;
    public MyGridAdapter(Context context, List<MyCol> listOfColor, List<MyCol> sortedListOfColor, int rowCount, int colCount, Button start_game_btn){
        this.context=context;
        this.listOfColor=listOfColor;
        this.rowCount=rowCount;
        this.colCount=colCount;
        this.sortedListOfColor=sortedListOfColor;
//        wrong_answer_buzzer=MediaPlayer.create(context,R.raw.mixkit_wrong_electricity_buzz_955);
//        correct_answer_tick=MediaPlayer.create(context,R.raw.correct_choice_43861);

        this.start_game_btn=start_game_btn;
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
        lf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("str","idhar aya");
                lf.setStrokeColor(Color.GREEN);
            }
        });
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setTextBack();
            }
        },2000);

        ImageButton img=lf.findViewById(R.id.imgBtn);
        img.setBackgroundColor(Color.rgb(listOfColor.get(i).r,listOfColor.get(i).g,listOfColor.get(i).b));
        img.setOnClickListener(this);
        int width=context.getResources().getDisplayMetrics().widthPixels;
        int gridWidth=viewGroup.getWidth();
        int gridHeight=viewGroup.getHeight();
        lf.getLayoutParams().width=(gridWidth-(int)(width/50f)*(colCount-1))/colCount;
        lf.getLayoutParams().height=(gridHeight-(int)(width/50f)*(rowCount-1))/rowCount;
        lf.setVisibility(View.GONE);
        return lf;
    }

    public void setTextBack(){
        start_game_btn.setText("Start New Game");
        start_game_btn.setClickable(true);
        start_game_btn.setEnabled(true);
    }


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
//            if(correct_answer_tick.isPlaying()){
//                correct_answer_tick.stop();
//            }
//            view.setVisibility(View.GONE);
            ((CardView)view.getParent()).setVisibility(View.GONE);
            MediaPlayer mp=MediaPlayer.create(context,R.raw.correct_choice_43861);
            mp.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.release();
                }
            },2000);
//            MaterialCardView crdv= (MaterialCardView) view.getParent();
//            crdv.setStrokeColor(Color.GREEN);
            ptr++;
            cv.setOnClickListener(null);
            view.setOnClickListener(null);
//            view.setVisibility(View.INVISIBLE);
//            gdv.getPo
            Animation anim = new ScaleAnimation(
                    1f, 0f, // Start and end values for the X axis scaling
                    1f, 0f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(1000);
            cv.startAnimation(anim);
        }
        else{
//            if(wrong_answer_buzzer.isPlaying()){
//                wrong_answer_buzzer.stop();
//            }
            Log.i("het","yu");
            MediaPlayer mp=MediaPlayer.create(context,R.raw.mixkit_wrong_electricity_buzz_955);
            mp.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.release();
                }
            },2000);
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

        }
    }
}
