package model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Product_table")
public class Product {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "productId")
    private String productId;

    @ColumnInfo(name = "productName")
    private String productName;

    @ColumnInfo(name = "productPrice")
    private String productPrice;

    @ColumnInfo(name = "productState")
    private String productState;

    @ColumnInfo(name = "productCategory")
    private String productCategory;

    @ColumnInfo(name = "productDescription")
    private String productDescription;

    @ColumnInfo(name = "productDeliveryMethod")
    private String productDeliveryMethod;

    @ColumnInfo(name = "productSeller")
    private String productSeller;

    @ColumnInfo(name = "productBuyer")
    private String productBuyer;

    @ColumnInfo(name = "imageUrl")
    private List<String> imageUrls;

    @ColumnInfo(name = "likes")
    private List<String> likes;

    @ColumnInfo(name = "conditions")
    private String condition;

    @ColumnInfo(name = "timeAdded")
    private String timeAdded;

    @ColumnInfo(name = "sellerUserName")
    private String sellerUserName;
    @ColumnInfo(name = "sellerImageUrl")
    private String sellerImageUrl;

    public Product() {

    }

    public Product(String productId, String productName, String productPrice, String productState, String productCategory,
                   String productDescription, String productDeliveryMethod, String productSeller, String productBuyer,
                   List<String> imageUrls, List<String> likes, String condition, String sellerUserName, String sellerImageUrl, String timeAdded) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productState = productState;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productDeliveryMethod = productDeliveryMethod;
        this.productSeller = productSeller;
        this.productBuyer = productBuyer;
        this.imageUrls = imageUrls;
        this.likes = likes;
        this.condition = condition;
        this.timeAdded = timeAdded;
        this.sellerUserName = sellerUserName;
        this.sellerImageUrl = sellerImageUrl;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductDeliveryMethod() {
        return productDeliveryMethod;
    }

    public void setProductDeliveryMethod(String productDeliveryMethod) {
        this.productDeliveryMethod = productDeliveryMethod;
    }

    public String getProductSeller() {
        return productSeller;
    }

    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }

    public String getProductBuyer() {
        return productBuyer;
    }

    public void setProductBuyer(String productBuyer) {
        this.productBuyer = productBuyer;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrl) {
        this.imageUrls = imageUrl;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getSellerUserName() {
        return sellerUserName;
    }

    public void setSellerUserName(String sellerUserName) {
        this.sellerUserName = sellerUserName;
    }

    public String getSellerImageUrl() {
        return sellerImageUrl;
    }

    public void setSellerImageUrl(String sellerImageUrl) {
        this.sellerImageUrl = sellerImageUrl;
    }
}
