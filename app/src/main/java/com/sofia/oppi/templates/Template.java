package com.sofia.oppi.templates;

import com.badlogic.androidgames.framework.Input;
import com.sofia.oppi.animationengine.ActionImage;
import com.sofia.oppi.animationengine.ActionScene;
import com.sofia.oppi.animationengine.Graphics;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanflorez on 25/05/15.
 */
public interface Template {

    public void draw(Graphics graphics);

    public void update(List<Input.TouchEvent> touchEvents);
}
