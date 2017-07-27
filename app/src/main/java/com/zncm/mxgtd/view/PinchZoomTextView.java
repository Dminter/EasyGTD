package com.zncm.mxgtd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;

/**
 * TextView that increases/decreases font size as it is pinched/zoomed.
 * <p>
 * Created by adam.mcneilly on 11/27/16.
 */
public class PinchZoomTextView extends TextView {
    /**
     * Consider each "step" between the two pointers as 200 px. In other words, the TV size will grow
     * every 200 pixels.
     */
    private static final float STEP = 200;

    /**
     * The ratio of the text size compared to its original.
     */
    private float ratio = 12.0f;

    /**
     * The distance between the two pointers when they are first placed on the screen.
     */
    private int baseDistance;

    /**
     * The ratio of the text size when the gesture is started.
     */
    private float baseRatio;

    /**
     * Boolean flag for whether or not zoom feature is enabled. Defaults to true.
     */
    private boolean zoomEnabled = true;


    private float fontSize;
    private float fontSizeBase = 12f;

    /**
     * Default constructor.
     */
    public PinchZoomTextView(Context context) {
        this(context, null);
    }

    /**
     * Default constructor.
     */
    public PinchZoomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Handles the touch event by the user and determines whether font size should grow,
     * and by how much.
     * <p>
     * If the action is simply `POINTER_DOWN` it means the user is just setting their fingers down,
     * so collect base values.
     * <p>
     * Otherwise, the user is pinching, so get the distance between the pointers, find the ratio
     * we need, and set the text size. Note: based on an initial size of 13, and can't exceed a ratio
     * of 1024.
     * <p>
     * Inspiration taken from: http://stackoverflow.com/a/20303367/3131147
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Must have two gestures.
        if (zoomEnabled && event.getPointerCount() == 2) {
            int action = event.getAction();
            int distance = getDistance(event);
            int pureAction = action & MotionEvent.ACTION_MASK;
            if (pureAction == MotionEvent.ACTION_POINTER_DOWN) {
                baseDistance = distance;
                baseRatio = ratio;
            } else {
                float delta = (distance - baseDistance) / STEP;
                float multi = (float) Math.pow(2, delta);
                ratio = Math.min(1024.0f, Math.max(0.1f, baseRatio * multi));
                fontSize = ratio + fontSizeBase;
                setTextSize(fontSize);
                MySp.setFontSize(fontSize);
            }

        }

        return true;
    }

    /**
     * Returns the distance between two pointers on the screen.
     */
    private int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }

    /**
     * Sets the enabled state of the zoom feature.
     */
    public void setZoomEnabled(boolean enabled) {
        this.zoomEnabled = enabled;
    }

    /**
     * Returns the enabled state of the zoom feature.
     */
    public boolean isZoomEnabled() {
        return zoomEnabled;
    }



}