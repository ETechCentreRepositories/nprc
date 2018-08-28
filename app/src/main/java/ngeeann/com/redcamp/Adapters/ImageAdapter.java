package ngeeann.com.redcamp.Adapters;


import android.content.Context;

import android.support.v4.view.PagerAdapter;

import android.support.v4.view.ViewPager;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;

import ngeeann.com.redcamp.R;


/**

 * Created by SrceCde on 18-09-2015.

 */

public class ImageAdapter extends PagerAdapter {

    Context context;


    private int[] GalImages = new int[] {


//            R.drawable.first,    //Here first,second,third... are the name of the jpeg files placed in drawable folder
//
//            R.drawable.second,
//
//            R.drawable.third,
//
//            R.drawable.day2_selected_1

    };

    ImageAdapter(Context context){

        this.context=context;

    }

    @Override

    public int getCount() {

        return GalImages.length;

    }


    @Override

    public boolean isViewFromObject(View view, Object object) {

        return view == ((ImageView) object);

    }


    @Override

    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);


        imageView.setImageResource(GalImages[position]);

        ((ViewPager) container).addView(imageView, 0);

        return imageView;

    }


    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((ImageView) object);

    }

}