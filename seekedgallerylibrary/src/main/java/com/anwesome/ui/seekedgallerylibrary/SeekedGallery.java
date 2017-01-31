package com.anwesome.ui.seekedgallerylibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.hardware.display.DisplayManager;
import android.view.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anweshmishra on 01/02/17.
 */
public class SeekedGallery {
    private float seekx = 0,seekh = 100;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Activity activity;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private SeekedView seekedView;
    private GalleryView galleryView;
    private int currentIndex = 0;
    private long prevTime = System.currentTimeMillis();
    private int interval=2000,duration=10000;
    private int n = 0,w=500,h=500;
    public SeekedGallery(Activity activity) {
        this.activity = activity;
        initDimensions();
    }
    public void addBitmap(Bitmap bitmap) {
        bitmaps.add(bitmap);
    }
    private void initDimensions() {
        DisplayManager displayManager = (DisplayManager)activity.getSystemService(Context.DISPLAY_SERVICE);
        Display display = displayManager.getDisplay(0);
        if(display!=null) {
            Point size = new Point();
            display.getRealSize(size);
            w = size.x;
            h = size.y;
            seekh = h/20;
            seekx = seekh/2;
        }
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public void addViewToParent() {
        galleryView = new GalleryView(activity);
        duration = bitmaps.size()*interval;
        seekedView = new SeekedView(activity);
        galleryView.setX(w/8);
        galleryView.setY(h/12);
        seekedView.setX(0);
        seekedView.setY(7*h/10);
        activity.addContentView(seekedView,new ViewGroup.LayoutParams(w,(int)seekh));
        activity.addContentView(galleryView,new ViewGroup.LayoutParams(2*w/3,h/2));
    }
    private class GalleryView extends View {
        public GalleryView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            int w = canvas.getWidth(),h = canvas.getHeight(),r = w/6;
            if(h<w) {
                r = h/6;
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#757575"));
            canvas.drawRoundRect(new RectF(w/20,h/20,19*w/20,19*h/20),r,r,paint);
            Path path = new Path();
            path.addRoundRect(new RectF(w/20,h/20,19*w/20,19*h/20),r,r, Path.Direction.CCW);
            canvas.clipPath(path);
            paint.setStyle(Paint.Style.FILL);
            if(bitmaps.size()>0) {
                Bitmap bitmap = bitmaps.get(currentIndex);
                bitmap = Bitmap.createScaledBitmap(bitmap, 9 * w / 10, 9 * h / 10, true);
                canvas.drawBitmap(bitmap, w / 20, h / 20, paint);
            }
        }
    }
    private class SeekedView extends View {
        private boolean animated = true;
        private  int x = 0,time = 1;
        private boolean isDown = false,seeking = false;
        public SeekedView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            paint.setColor(Color.parseColor("#37474F"));
            canvas.drawRoundRect(new RectF(0,canvas.getHeight()/4,canvas.getWidth(),3*canvas.getHeight()/4),canvas.getHeight()/4,canvas.getHeight()/4,paint);
            paint.setColor(Color.parseColor("#d32f2f"));
            canvas.drawRoundRect(new RectF(0,canvas.getHeight()/4,seekx,3*canvas.getHeight()/4),canvas.getHeight()/4,canvas.getHeight()/4,paint);
            canvas.drawCircle(seekx,canvas.getHeight()/2,canvas.getHeight()/2,paint);
            if(seekx<w-w/duration && animated) {
                try {
                    if(System.currentTimeMillis()-prevTime>=1000 && !isDown) {
                        seekx+=((w)*1000.0f)/duration;
                        updateGallery();
                        if(currentIndex == bitmaps.size()-1 && !isDown) {
                            seekx = canvas.getWidth()-canvas.getHeight()/2;
                            animated = false;
                        }
                        prevTime = System.currentTimeMillis();
                    }
                    invalidate();
                }
                catch (Exception ex) {

                }
            }
        }
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX(),y = event.getY();
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(!isDown && x>=seekx-seekh/2 && x<=seekx+seekh/2 && y>=0 && y<=seekh) {
                        isDown = true;
                        seekx = event.getX();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(isDown) {
                        seekx = event.getX();
                        updateGallery();
                        animated = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(isDown){
                        isDown = false;
                    }
                    break;
            }
            return true;
        }
        private void updateGallery() {
            currentIndex = (int)((seekx*bitmaps.size())/w);
            if(currentIndex>=bitmaps.size()) {
                currentIndex = bitmaps.size()-1;
            }
            galleryView.postInvalidate();
        }
    }
}
