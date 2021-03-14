package shoaib.dev.barbershopapp.Adapter;

import java.util.List;

import shoaib.dev.barbershopapp.Model.Banner;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class HomeSliderAdapter extends SliderAdapter {

    List<Banner> bannerList;

    public HomeSliderAdapter(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(bannerList.get(position).getImage());
    }
}
