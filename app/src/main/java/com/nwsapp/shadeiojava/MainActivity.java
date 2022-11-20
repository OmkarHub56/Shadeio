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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // the four color selection buttons
    ImageButton blueColorSel;
    ImageButton redColorSel;
    ImageButton yellowColorSel;
    ImageButton blackColorSel;

    // the four difficulty selection buttons
    ImageButton easyDiffSel;
    ImageButton medDiffSel;
    ImageButton hardDiffSel;
    ImageButton extDiffSel;

    // curr selected color and difficulty
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

    Button start_game_btn;

    MyGridAdapter gridAdapter;

    LinearLayout startGame;

    ImageButton sound_adjust;
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

        blueColorSel=findViewById(R.id.blue_col);
        blueColorSel.getLayoutParams().width= (int) (screenHeight/35f);
        blueColorSel.getLayoutParams().height= (int) (screenHeight/35f);
        redColorSel=findViewById(R.id.red_col);
        redColorSel.getLayoutParams().width= (int) (screenHeight/35f);
        redColorSel.getLayoutParams().height= (int) (screenHeight/35f);
        yellowColorSel=findViewById(R.id.yellow_col);
        yellowColorSel.getLayoutParams().width= (int) (screenHeight/35f);
        yellowColorSel.getLayoutParams().height= (int) (screenHeight/35f);
        blackColorSel=findViewById(R.id.black_col);
        blackColorSel.getLayoutParams().width= (int) (screenHeight/35f);
        blackColorSel.getLayoutParams().height= (int) (screenHeight/35f);
        currSelColour=blueColorSel;

        // setting diff change buttons
        easyDiffSel=findViewById(R.id.easy_diff);
        easyDiffSel.getLayoutParams().width= (int) (screenHeight/35f);
        easyDiffSel.getLayoutParams().height= (int) (screenHeight/35f);
        medDiffSel=findViewById(R.id.med_diff);
        medDiffSel.getLayoutParams().width= (int) (screenHeight/35f);
        medDiffSel.getLayoutParams().height= (int) (screenHeight/35f);
        hardDiffSel=findViewById(R.id.hard_diff);
        hardDiffSel.getLayoutParams().width= (int) (screenHeight/35f);
        hardDiffSel.getLayoutParams().height= (int) (screenHeight/35f);
        extDiffSel=findViewById(R.id.ext_diff);
        extDiffSel.getLayoutParams().width= (int) (screenHeight/35f);
        extDiffSel.getLayoutParams().height= (int) (screenHeight/35f);
        currSelDiff=easyDiffSel;

        blueColorSel.setOnClickListener(this);
        redColorSel.setOnClickListener(this);
        yellowColorSel.setOnClickListener(this);
        blackColorSel.setOnClickListener(this);

        easyDiffSel.setOnClickListener(this);
        medDiffSel.setOnClickListener(this);
        hardDiffSel.setOnClickListener(this);
        extDiffSel.setOnClickListener(this);

        sound_adjust=findViewById(R.id.imageButton);
        sound_adjust.getLayoutParams().height= (int) (screenWidth/15f);
        sound_adjust.getLayoutParams().width= (int) (screenWidth/15f);


        // setting the margins and some other attributes for the gridview
        colorsGridView=findViewById(R.id.colorsGridView);
        int marginLength= (int) (screenWidth/10F);
        ViewGroup.MarginLayoutParams params = (GridView.MarginLayoutParams) colorsGridView.getLayoutParams();
        params.leftMargin=marginLength;
        params.bottomMargin=marginLength;
        params.rightMargin=marginLength;
        params.topMargin=marginLength;

        colorsGridView.setVerticalSpacing((int) (screenWidth/50F));
        colorsGridView.setHorizontalSpacing((int) (screenWidth/50F));

        // button to start the game
        start_game_btn=findViewById(R.id.start_game_btn);

        // linear_layout inside which there is start button
        startGame=findViewById(R.id.startgame_ll);
        int ml= (int) (screenWidth/40F);
        ViewGroup.MarginLayoutParams params1 = (LinearLayout.MarginLayoutParams) startGame.getLayoutParams();
        params1.leftMargin=ml;
        params1.bottomMargin=ml;
        params1.rightMargin=ml;

        LinearLayout ll=findViewById(R.id.set_linear);
        int ml2= (int) (screenWidth/30F);
        ViewGroup.MarginLayoutParams params2 = (LinearLayout.MarginLayoutParams) ll.getLayoutParams();
        params2.leftMargin=ml2;
        params2.topMargin=ml2;
        params2.rightMargin=ml2;

        TextView t1=findViewById(R.id.textView),t2=findViewById(R.id.textView1);
        t1.setPadding((int) (screenWidth/50f),0, (int) (screenWidth/50f),0);
        t2.setPadding((int) (screenWidth/50f),0, (int) (screenWidth/50f),0);

        // start the initial game
        startNewGame(start_game_btn);
    }


    List<MyCol> colorList=new ArrayList<>();
    List<MyCol> sortedColorList=new ArrayList<>();
    List<Integer> list=new ArrayList<>();
    public void startNewGame(View view){

        colorList.clear();
        sortedColorList.clear();
        list.clear();
        colorsGridView.setNumColumns(currColCount);

//         just for testing color differentiation
//         this shouldn't be deleted
//        int ptr=1;
//        for(int i=0;i<currRowCount*currColCount;i++){
//            Random rand=new Random();
////            int a=Math.abs(rand.nextInt(2));
//            if(ptr<=255){
//                colorList.add(new MyCol(255-ptr, 255-ptr,255-ptr));
//            }
//            else{
//                colorList.add(new MyCol(255-ptr, 255-ptr,255-ptr));
//            }
////            colorList.add(new MyCol(255-ptr,255-ptr,255-ptr));
//            ptr+=5;
//        }


        int numCount=currRowCount*currColCount;
        int minDist;
        if(numCount==6){
            minDist=30;
            if(currSelColour==blackColorSel){
                minDist=15;
            }
        }
        else if(numCount==15){
            minDist=15;
            if(currSelColour==blackColorSel){
                minDist=9;
            }
        }
        else if(numCount==24){
            minDist=9;
            if(currSelColour==blackColorSel){
                minDist=5;
            }
        }
        else{
            minDist=5;
            if(currSelColour==blackColorSel){
                minDist=2;
            }
        }

        // generates a random number out of the remaining indices
        Random rand=new Random();
        int []arr=new int[currSelColour==blackColorSel?258:513];
        int availableNumbers=currSelColour==blackColorSel?256:511;
        for(int i=0;i<numCount;i++){
            int a=rand.nextInt(availableNumbers)+1;
            int ptr=0;
            for(int j=1;j<=(currSelColour==blackColorSel?256:511);j++){
                if(arr[j]==0){
                    ptr++;
                }
                if(ptr==a){
                    for(int k=Math.max(1,j-minDist+1);k<=Math.min(currSelColour==blackColorSel?256:511,j+minDist-1);k++){
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

        if(currSelColour==blueColorSel){
            for(int i=0;i<numCount;i++){
                int num=list.get(i);
                if(num<=256){
                    colorList.add(new MyCol(0,256-num,255));
                }
                else{
                    colorList.add(new MyCol(0, 0,511-num));
                }
            }
        }
        else if(currSelColour==blackColorSel){
            for(int i=0;i<numCount;i++){
                int num=list.get(i);
                colorList.add(new MyCol(255-num,255-num,255-num));
            }
        }
        else{
            for(int i=0;i<numCount;i++){
                int num=list.get(i);
                if(num<=256){
                    colorList.add(new MyCol(255,256-num,0));
                }
                else{
                    colorList.add(new MyCol(511-num,0,0));
                }
            }
        }


        Collections.sort(list);
        if(currSelColour==blueColorSel){
            for(int i=0;i<numCount;i++){
                int num=list.get(i);
                if(num<=256){
                    sortedColorList.add(new MyCol(0,256-num,255));
                }
                else{
                    sortedColorList.add(new MyCol(0, 0,511-num));
                }
            }
        }
        else if(currSelColour==blackColorSel){
            for(int i=0;i<numCount;i++){
                int num=list.get(i);
                sortedColorList.add(new MyCol(255-num,255-num,255-num));
            }
        }
        else{
            for(int i=0;i<numCount;i++){
                int num=list.get(i);
                if(num<=256){
                    sortedColorList.add(new MyCol(255,256-num,0));
                }
                else{
                    sortedColorList.add(new MyCol(511-num,0,0));
                }
            }
        }

        gridAdapter=new MyGridAdapter(this,colorList,sortedColorList,currRowCount,currColCount,start_game_btn);
        colorsGridView.setAdapter(gridAdapter);

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

    int current_vol_status=1;
    public void toggleSound(View view){
        ImageButton img=findViewById(R.id.imageButton);
        if(current_vol_status==1){
            img.setImageResource(R.drawable.volume_off_icon);
            current_vol_status=0;
            gridAdapter.current_vol_status=0;
        }
        else{
            img.setImageResource(R.drawable.volume_on_icon);
            current_vol_status=1;
            gridAdapter.current_vol_status=1;
        }
    }

    public void pl(String tag,String log){
        Log.i(tag,log);
    }
    public void pl(String tag,Object log){
        Log.i(tag,String.valueOf(log));
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