package shoaib.dev.barbershopapp.Interface;

import java.util.List;

import shoaib.dev.barbershopapp.Model.Banner;

public interface ILookbookLoadListener {
    void onLookbookLoadSuccess(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
