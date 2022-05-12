package model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.UserRepository;

//import data.ProductRepository;


public class ProductViewModel extends AndroidViewModel {

//    public static ProductRepository repository;
//    public final LiveData<List<Product>> allUProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
    }
}
