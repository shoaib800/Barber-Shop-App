package com.shoaib.barbershopapp.Interface;

import java.util.List;

import com.shoaib.barbershopapp.Model.Salon;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> salonList);
    void onBranchLoadFailed(String message);
}
