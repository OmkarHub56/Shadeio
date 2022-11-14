package com.nwsapp.shadeiojava;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SharedMemory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG="OMK";
    ImageButton blueColorSel;
    ImageButton redColorSel;
    ImageButton yellowColorSel;
    ImageButton blackColorSel;

    ImageButton easyDiffSel;
    ImageButton medDiffSel;
    ImageButton hardDiffSel;
    ImageButton extDiffSel;

    ImageButton currSelColour,currSelDiff;
    int EASY_HORIZONTAL_BOX_COUNT=3;
    int MEDIUM_HORIZONTAL_BOX_COUNT=5;
    int HARD_HORIZONTAL_BOX_COUNT=6;
    int EXT_HORIZONTAL_BOX_COUNT=8;

    int EASY_VERTICAL_BOX_COUNT=2;
    int MEDIUM_VERTICAL_BOX_COUNT=3;
    int HARD_VERTICAL_BOX_COUNT=4;
    int EXT_VERTICAL_BOX_COUNT=5;
    int currRowCount=EASY_HORIZONTAL_BOX_COUNT,currColCount=EASY_VERTICAL_BOX_COUNT;
    int diff=1;

    GridView colorsGridView;

    List<MyCol> colorList;
    List<MyCol> sortedColorList;

    Button start_game_btn;

    // utility purposes
    int screenWidth,screenHeight;
    float screenDensity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight=getResources().getDisplayMetrics().heightPixels;
        screenDensity=getResources().getDisplayMetrics().density;

        // setting the colour change buttons
        blueColorSel=findViewById(R.id.blue_col);
        redColorSel=findViewById(R.id.red_col);
        yellowColorSel=findViewById(R.id.yellow_col);
        blackColorSel=findViewById(R.id.black_col);
        currSelColour=blueColorSel;

        // setting diff change buttons
        easyDiffSel=findViewById(R.id.easy_diff);
        medDiffSel=findViewById(R.id.med_diff);
        hardDiffSel=findViewById(R.id.hard_diff);
        extDiffSel=findViewById(R.id.ext_diff);
        currSelDiff=easyDiffSel;

        blueColorSel.setOnClickListener(this);
        redColorSel.setOnClickListener(this);
        yellowColorSel.setOnClickListener(this);
        blackColorSel.setOnClickListener(this);

        easyDiffSel.setOnClickListener(this);
        medDiffSel.setOnClickListener(this);
        hardDiffSel.setOnClickListener(this);
        extDiffSel.setOnClickListener(this);

        colorList=new ArrayList<>();
        sortedColorList=new ArrayList<>();

        colorsGridView=findViewById(R.id.colorsGridView);
        int marginLength= (int) (screenWidth/10F);
        ViewGroup.MarginLayoutParams params = (GridView.MarginLayoutParams) colorsGridView.getLayoutParams();
        params.leftMargin=marginLength;
        params.bottomMargin=marginLength;
        params.rightMargin=marginLength;
        params.topMargin=marginLength;

//        int marginTop=(int) (screenHeight/3F);
//        params.topMargin=marginTop;

        colorsGridView.setVerticalSpacing((int) (screenWidth/50F));
        colorsGridView.setHorizontalSpacing((int) (screenWidth/50F));

        start_game_btn=findViewById(R.id.start_game_btn);
    }

    List<Integer> list=new ArrayList<>();

    public void startNewGame(View view){
        start_game_btn.setEnabled(false);
        start_game_btn.setClickable(false);

        // to remove if any current game is On
        //Todo code




        colorList.clear();
        sortedColorList.clear();
        list.clear();
        colorsGridView.setNumColumns(currColCount);

        // just for testing color differentiation
//        int ptr=1;
//        for(int i=0;i<currRowCount*currColCount;i++){
//            Random rand=new Random();
////            int a=Math.abs(rand.nextInt(2));
//            if(ptr<=255){
//                colorList.add(new MyCol(0, 255-ptr,255));
//            }
//            else{
//                colorList.add(new MyCol(0, 0,510-ptr));
//            }
////            colorList.add(new MyCol(255-ptr,255-ptr,255-ptr));
//            ptr+=35;
//        }
        start_game_btn.setText("Generating new game...");


        int numCount=currRowCount*currColCount;
        int minDist;
        if(numCount==6){
            minDist=40;
        }
        else if(numCount==15){
            minDist=25;
        }
        else if(numCount==24){
            minDist=15;
        }
        else{
            minDist=5;
        }
        list.clear();
        pl("minDist : "+minDist);
        pl("numCount : "+numCount);
        int []arr=new int[513];
        Arrays.fill(arr,0);
        Random rand=new Random();
        int availableNumbers=511;
        for(int i=0;i<numCount;i++){
            pl("availableNums : "+availableNumbers);
            int a=rand.nextInt(availableNumbers)+1;
            int ptr=0;
            for(int j=1;j<=511;j++){
                if(arr[j]==0){
                    ptr++;
                }
                if(ptr==a){
                    for(int k=Math.max(1,j-minDist+1);k<=Math.min(511,j+minDist-1);k++){
                        if(arr[k]==0){
                            arr[k]=1;
                            availableNumbers--;
                        }
                    }
                    list.add(j);
                    break;
                }
            }
        }


//        String a="";
//        for(int i=0;i<numCount;i++){
//            a=a+list.get(i);
//            a=a+" ";
//        }
//        pl(a);

        for(int i=0;i<numCount;i++){
            int num=list.get(i);
            if(num<=256){
                colorList.add(new MyCol(0,256-num,255));
            }
            else{
                colorList.add(new MyCol(0, 0,511-num));
            }
        }

        Collections.sort(list);
        for(int i=0;i<numCount;i++){
            int num=list.get(i);
            if(num<=256){
                sortedColorList.add(new MyCol(0,256-num,255));
            }
            else{
                sortedColorList.add(new MyCol(0, 0,511-num));
            }
        }
        colorsGridView.setAdapter(new MyGridAdapter(this,colorList,sortedColorList,currRowCount,currColCount,start_game_btn));

    }

    @Override
    public void onClick(View view) {

        if(view==blueColorSel){
            removeALlSelectionsColour();
            blueColorSel.setBackgroundResource(R.drawable.blue_colour_sel_selected);
            currSelColour=blueColorSel;
        }
        else if(view==redColorSel){
            removeALlSelectionsColour();
            redColorSel.setBackgroundResource(R.drawable.red_colour_sel_selected);
            currSelColour=redColorSel;
        }
        else if(view==yellowColorSel){
            removeALlSelectionsColour();
            yellowColorSel.setBackgroundResource(R.drawable.yellow_colour_sel_selected);
            currSelColour=yellowColorSel;
        }
        else if(view==blackColorSel){
            removeALlSelectionsColour();
            blackColorSel.setBackgroundResource(R.drawable.black_colour_sel_selected);
            currSelColour=blackColorSel;
        }
        else if(view==easyDiffSel){
            removeALlSelectionsDifficulty();
            easyDiffSel.setBackgroundResource(R.drawable.yellow_colour_sel_selected);
            currSelDiff=easyDiffSel;
            currRowCount=EASY_HORIZONTAL_BOX_COUNT;currColCount=EASY_VERTICAL_BOX_COUNT;
            diff=1;
        }
        else if(view==medDiffSel){
            removeALlSelectionsDifficulty();
            medDiffSel.setBackgroundResource(R.drawable.orange_colour_sel_selected);
            currSelDiff=medDiffSel;
            currRowCount=MEDIUM_HORIZONTAL_BOX_COUNT;currColCount=MEDIUM_VERTICAL_BOX_COUNT;
            diff=2;
        }
        else if(view==hardDiffSel){
            removeALlSelectionsDifficulty();
            hardDiffSel.setBackgroundResource(R.drawable.red_colour_sel_selected);
            currSelDiff=hardDiffSel;
            currRowCount=HARD_HORIZONTAL_BOX_COUNT;currColCount=HARD_VERTICAL_BOX_COUNT;
            diff=3;
        }
        else if(view==extDiffSel){
            removeALlSelectionsDifficulty();
            extDiffSel.setBackgroundResource(R.drawable.dark_red_colour_sel_selected);
            currSelDiff=extDiffSel;
            currRowCount=EXT_HORIZONTAL_BOX_COUNT;currColCount=EXT_VERTICAL_BOX_COUNT;
            diff=4;
        }
    }

    public void removeALlSelectionsColour(){
        if(currSelColour==blueColorSel){
            blueColorSel.setBackgroundResource(R.drawable.blue_colour_sel);
        }
        else if(currSelColour==redColorSel){
            redColorSel.setBackgroundResource(R.drawable.red_colour_sel);
        }
        else if(currSelColour==yellowColorSel){
            yellowColorSel.setBackgroundResource(R.drawable.yellow_colour_sel);
        }
        else if(currSelColour==blackColorSel){
            blackColorSel.setBackgroundResource(R.drawable.black_colour_sel);
        }
    }

    public void removeALlSelectionsDifficulty(){
        if(currSelDiff==easyDiffSel){
            easyDiffSel.setBackgroundResource(R.drawable.yellow_colour_sel);
        }
        else if(currSelDiff==medDiffSel){
            medDiffSel.setBackgroundResource(R.drawable.orange_colour_sel);
        }
        else if(currSelDiff==hardDiffSel){
            hardDiffSel.setBackgroundResource(R.drawable.red_colour_sel);
        }
        else if(currSelDiff==extDiffSel){
            extDiffSel.setBackgroundResource(R.drawable.dark_red_colour_sel);
        }
    }


    public void pl(String log){
        Log.i(TAG,log);
    }
    public void pl(Object log){
        Log.i(TAG,String.valueOf(log));
    }
}

class MyCol{
    int r,g,b;
    public MyCol(int r,int g,int b) {
        this.r=r;
        this.g=g;
        this.b=b;
    }
}