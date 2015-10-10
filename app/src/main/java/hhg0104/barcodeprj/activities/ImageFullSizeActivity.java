package hhg0104.barcodeprj.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import hhg0104.barcodeprj.R;
import hhg0104.barcodeprj.utils.DrawableManager;

/**
 * Created by HGHAN on 2015-10-10.
 */
public class ImageFullSizeActivity extends Activity{

    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.picture_full_size);

        thisActivity = this;

        Intent intent = getIntent();

        ImageView fullSizeImage = (ImageView) findViewById(R.id.full_size_image);

        String imagePath = intent.getStringExtra("imagePath");
        if(imagePath == null || imagePath.isEmpty()){
            return;
        }

        Drawable image = DrawableManager.getInstance().fetchDrawable(imagePath);
        fullSizeImage.setImageDrawable(image);

        ImageView cancelImage = (ImageView) findViewById(R.id.close);

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
