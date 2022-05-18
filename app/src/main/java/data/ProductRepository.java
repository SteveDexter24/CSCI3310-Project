package data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.Product;
import util.EcoSellRoomDatabase;

public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;
    private LiveData<Product> product;

    public ProductRepository(Application application) {
        EcoSellRoomDatabase db = EcoSellRoomDatabase.getDatabase(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insertProduct(Product product){
        EcoSellRoomDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insertProduct(product);
        });
    }
    public LiveData<Product> getProduct(String productId){
        return product;
    };
}
