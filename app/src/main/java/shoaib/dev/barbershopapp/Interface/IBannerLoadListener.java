package shoaib.dev.barbershopapp.Interface;

import java.util.List;

import shoaib.dev.barbershopapp.Model.Banner;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
