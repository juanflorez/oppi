package com.sofia.oppi.animationengine;

/**
 * Created by juanflorez on 25/05/15.
 */
public class ActionImage extends PojoActionImage {

    private boolean activated = false;
    public void activate() {activated = true;}
    public boolean isActivated(){return activated;}
    public String getStartImage(){
        return root+mStart_image;
    }

    /**
     * Creates a copy of this object
      * @param image
     */
    public ActionImage(ActionImage image){
        this.mName = image.mName;
        this.mStart_image = image.mStart_image;
        this.root = image.getRoot();
        this.mCorrect = image.mCorrect;
    }
}
