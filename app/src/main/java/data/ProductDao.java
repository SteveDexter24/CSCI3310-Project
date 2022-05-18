package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import model.Product;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM Product_table")
    LiveData<List<Product>> getAllProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(Product product);

    @Query("SELECT * FROM Product_table WHERE productId = :productId")
    LiveData<Product> getProduct(String productId);
}
