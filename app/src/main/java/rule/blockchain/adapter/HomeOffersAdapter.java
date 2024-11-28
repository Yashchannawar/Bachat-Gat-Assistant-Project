package rule.blockchain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import rule.blockchain.R;
public class HomeOffersAdapter extends PagerAdapter {

    List<Integer> list;
    BottomNavigationView bottomNavigationView;
    Context context;

    public HomeOffersAdapter(List<Integer> imageList, Context context, BottomNavigationView bottomNavigationView) {
        this.list = imageList;
        this.bottomNavigationView = bottomNavigationView;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_layout, container, false);
        ImageView image = view.findViewById(R.id.imageView);
//        image.setImageResource(list.get(position));

         image.setImageResource(context.getResources().getIdentifier(String.valueOf(list.get(position)), "raw", context.getPackageName()));


        container.addView(view);


        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
