package com.xytsz.xytaj.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by admin on 2017/7/25.
 *
 * 上报图片显示
 */
public class ReportPhotoShowAdapter extends PagerAdapter{

    private List<String> imageUrllist;

    public ReportPhotoShowAdapter(List<String> imageUrllist) {

        this.imageUrllist = imageUrllist;
    }

    @Override
    public int getCount() {
        return imageUrllist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView  = new ImageView(container.getContext());
        imageView.setBackgroundColor(Color.BLACK);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        String path = imageUrllist.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //Bitmap imageBitmap = rotateBitmap(bitmap, 90f);
        imageView.setImageBitmap(bitmap);
        //Glide.with(container.getContext()).load(imageUrllist.get(position)).transform(new RotateTransformation(container.getContext(),90f)).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    private Bitmap rotateBitmap(Bitmap bm,float orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {

            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;
        } catch (OutOfMemoryError ex) {
        }

        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
         container.removeView((View) object);
    }
}
