package com.toddburgessmedia.customwidget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Todd Burgess (todd@toddburgessmedia.com on 06/12/16.
 */

public class MyCustomView extends View {

    public final static int LINE = 0;
    public final static int CIRCLE = 1;

    private int lineType = LINE;

    Context context;

    Paint pointer;
    Paint shape;

    float x;
    float y;
    int aRGB;
    boolean isDrawing = false;

//    int[] colours = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.YELLOW, Color.GRAY};

    Random random;

    ArrayList<SillyPoint> points;

    public MyCustomView (Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MyCustomView (Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        this.context = context;
        init();
    }

    private void init () {
        pointer = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointer.setColor(Color.RED);
        pointer.setTextSize(122);

        shape = new Paint(Paint.ANTI_ALIAS_FLAG);
        shape.setColor(Color.BLUE);
        shape.setStrokeWidth(50);
        shape.setStrokeCap(Paint.Cap.ROUND);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        x = point.x / 2;
        y = point.y / 2;

        points = new ArrayList<>();

        random = new Random();

    }

    public void clearLines () {
        points.clear();
        invalidate();
    }

    public void animateColours() {

        ValueAnimator animator = ValueAnimator.ofInt(0, points.size()-1);
        animator.setDuration(1200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                           @Override
                           public void onAnimationUpdate(ValueAnimator valueAnimator) {
                               aRGB = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
                               int i = (int) valueAnimator.getAnimatedValue();
                               if (i < points.size()) {
                                   points.get(i).aRGB = aRGB;
                               }
                               invalidate();

                           }
                       }
        );
        animator.start();
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                aRGB = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
                points.add(new SillyPoint((int)x, (int)y, aRGB));
                isDrawing = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDrawing) {
                    x = event.getX();
                    y = event.getY();
                    aRGB = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
                    points.add(new SillyPoint((int)x, (int)y, aRGB));
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrawing) {
                    x = event.getX();
                    y = event.getY();
                    if (lineType == LINE) {
                        aRGB = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        points.add(new SillyPoint((int)x, (int)y, aRGB));
                    } else {
                        points.add(new SillyPoint(-1, -1,0));
                    }
                    isDrawing = false;
                    invalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(x,y,100, pointer);

        if (points.size() < 1) {
            return;
        }

        SillyPoint oldp = points.get(0);
        for (SillyPoint p : points) {
            if ((p.x != -1) && (oldp.x != -1)) {
                shape.setColor(p.aRGB);
                if (lineType == LINE) {
                    canvas.drawLine(oldp.x, oldp.y, p.x, p.y, shape);
                } else {
                    canvas.drawCircle(p.x, p.y, 30, shape);
                }
            }
            oldp = p;
        }
    }

    class SillyPoint {

        int aRGB;
        int x;
        int y;

        SillyPoint(int x, int y, int aRGB) {

            this.x = x;
            this.y = y;
            this.aRGB = aRGB;

        }
    }
}
