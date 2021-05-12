package com.shoaib.barbershopapp.Interface;

import java.util.List;

import com.shoaib.barbershopapp.Model.Banner;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
