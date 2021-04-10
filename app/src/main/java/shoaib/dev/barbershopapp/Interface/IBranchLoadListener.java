package shoaib.dev.barbershopapp.Interface;

import java.util.List;

import shoaib.dev.barbershopapp.Model.Salon;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> salonList);
    void onBranchLoadFailed(String message);
}
