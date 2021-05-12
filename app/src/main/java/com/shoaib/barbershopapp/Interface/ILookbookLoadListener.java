package com.shoaib.barbershopapp.Interface;

import java.util.List;

import com.shoaib.barbershopapp.Model.Banner;

public interface ILookbookLoadListener {
    void onLookbookLoadSuccess(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
