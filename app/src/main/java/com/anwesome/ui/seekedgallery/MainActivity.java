package com.anwesome.ui.seekedgallery;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anwesome.ui.seekedgallerylibrary.SeekedGallery;

public class MainActivity extends AppCompatActivity {
    private int resources[] = {R.drawable.car1,R.drawable.car2,R.drawable.car3,R.drawable.car4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekedGallery seekedGallery = new SeekedGallery(this);
        for(int i=0;i<4;i++) {
            for (int resource : resources) {
                seekedGallery.addBitmap(BitmapFactory.decodeResource(getResources(), resource));
            }
        }
        seekedGallery.setInterval(2000);
        seekedGallery.addViewToParent();
    }
}
