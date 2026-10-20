
package com.fashionstore.model;

import java.math.BigDecimal;

public class Product {

    private int productId;
    private int categoryId;
    private String productName;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPercent;
    private String imageUrl;
    private boolean isActive;
    

    public Product() {
    }

    public Product(int productId, int categoryId,
                   String productName, String description,
                   BigDecimal discountPercent, String imageUrl,
                   boolean isActive) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.discountPercent = discountPercent;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
