package com.toddburgessmedia.customwidget;

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

    Paint background;
    Paint square;

    int top = 20;
    int bottom = 30;

    float x;
    float y;
    boolean isDrawing = false;

    int[] colours = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.YELLOW, Color.GRAY};

    Random random;

    ArrayList<Point> points;


    long startTime;
    long current;

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
        background = new Paint(Paint.ANTI_ALIAS_FLAG);
        background.setColor(Color.RED);
        background.setTextSize(122);

        square = new Paint(Paint.ANTI_ALIAS_FLAG);
        square.setColor(Color.BLUE);
        square.setStrokeWidth(50);
        square.setStrokeCap(Paint.Cap.ROUND);

        startTime = System.currentTimeMillis();
        this.postInvalidate();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        x = point.x / 2;
        y = point.y / 2;

        points = new ArrayList<>();
        //points.add(new Point((int) x, (int) y));

        random = new Random();

    }

    public void clearLines () {
        points.clear();
//        points.add(new Point((int) x, (int) y));
        invalidate();
    }

    public void animateColours() {
            postInvalidateDelayed(30);
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
                points.add(new Point((int)x, (int)y));
                isDrawing = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDrawing) {
                    x = event.getX();
                    y = event.getY();
                    points.add(new Point((int)x, (int)y));
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrawing) {
                    x = event.getX();
                    y = event.getY();
                    if (lineType == LINE) {
                        points.add(new Point((int) x, (int) y));
                    } else {
                        points.add(new Point(-1, -1));
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

        canvas.drawCircle(x,y,100,background);

        if (points.size() < 1) {
            return;
        }

        Point oldp = points.get(0);
        for (Point p : points) {

            if ((p.x != -1) && (oldp.x != -1)) {
                if (!isDrawing) {
                    square.setARGB(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
                }
                if (lineType == LINE) {
                    canvas.drawLine(oldp.x, oldp.y, p.x, p.y, square);
                } else {
                    canvas.drawCircle(p.x, p.y, 30, square);
                }
            }
            oldp = p;
        }
    }

}
