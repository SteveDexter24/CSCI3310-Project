package model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.ProductRepository;


public class ProductViewModel extends AndroidViewModel {

    public static ProductRepository productRepository;
    public final LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application){
        super(application);
        productRepository = new ProductRepository(application);
        allProducts = productRepository.getAllProducts();
    }

    public static LiveData<List<Product>> getAllProducts() {return productRepository.getAllProducts();}
    public static void insertProduct(Product product) {productRepository.insertProduct(product);}
}
