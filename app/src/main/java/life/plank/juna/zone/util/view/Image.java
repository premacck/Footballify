package life.plank.juna.zone.util.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Image {

    public Bitmap compress(File file, String imgPath) throws IOException {
        Bitmap bitmapImage;

        //Decode image size
        BitmapFactory.Options bitmapScaleOptions = new BitmapFactory.Options();
        bitmapScaleOptions.inJustDecodeBounds = true;

        FileInputStream fis;

        fis = new FileInputStream(file);
        BitmapFactory.decodeStream(fis, null, bitmapScaleOptions);
        fis.close();

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (bitmapScaleOptions.outHeight > IMAGE_MAX_SIZE || bitmapScaleOptions.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(bitmapScaleOptions.outHeight, bitmapScaleOptions.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = scale;

        fis = new FileInputStream(file);
        bitmapImage = BitmapFactory.decodeStream(fis, null, bitmapOptions);
        fis.close();

        File imgFile = new File(imgPath);

        FileOutputStream out = new FileOutputStream(imgFile);
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();

        return bitmapImage;
    }
}
