package com.sofia.oppi.animationengine;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.annotations.SerializedName;


/**
 * It maps to the elements in the image array in each Frame in in scene.json(s)
 */
public class FrameImage extends ModuleElement{

    final static String TAG = "FrameImage";


    @SerializedName("pos_x")
    private String mPos_x;

    @SerializedName("pos_y")
    private String mPos_y;

    private int mYPos=0;
    private int mXPos=0;

    @SerializedName("file")
    private String mBitmapName="";



    public FrameImage(String bitmapName, int positionX, int positionY){
        mYPos = positionY;
        mXPos = positionX;
        // TODO: now containes resources/ get just the resource name, LATER....HOW?
        mBitmapName = bitmapName.substring( (bitmapName.lastIndexOf( "/") + 1), bitmapName.lastIndexOf(".") );
    }


    public void setmPos_Y (String pos_y){
        this.mPos_y = pos_y;
        try {
            this.mYPos = Integer.parseInt(pos_y);
        } catch (NumberFormatException n) {
            this.mYPos = 0;
            Log.d(TAG,  n.getMessage());
        }
    }

        public void setmPos_X (String pos_x){

            this.mPos_x = pos_x;

            try {

                this.mXPos = Integer.parseInt(pos_x);

            } catch (NumberFormatException n) {

                this.mXPos = 0;
                Log.d(TAG,  n.getMessage());

            }

        }

    public void setYPos( int YPos ) {
        this.mYPos = YPos;
    }




    public void setXPos( int XPos ) {
        this.mXPos = XPos;
    }



    public int getYPos() {
        return mYPos;
    }

    public int getXPos() {
        return mXPos;
    }

    public String getBitmapName() {
        Log.d("FRAME IMAGE: ",root+mBitmapName);
        return root+mBitmapName;
    }

    public void setRoot(String path){ root = path;}

    public String getRoot() {return root;}
    @Override
    public String toString() {
        return "FrameImage{" +
                ", mPos_x='" + mPos_x + '\'' +
                ", mPos_y='" + mPos_y + '\'' +
                ", mYPos=" + mYPos +
                ", mXPos=" + mXPos +
                ", mBitmapName='" + mBitmapName + '\'' +
                '}';
    }
}
