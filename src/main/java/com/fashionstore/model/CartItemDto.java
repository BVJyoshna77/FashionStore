package com.fashionstore.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartItemDto {

    private int productId;
    private String productName;
    private String imageUrl;
    private String sizeLabel;
    private int quantity;
    private BigDecimal originalPrice;
    private BigDecimal discountPercent;
    private BigDecimal unitPrice;

    public CartItemDto() {
    }

    public CartItemDto(int productId, String productName, String imageUrl,
                       String sizeLabel, int quantity, BigDecimal originalPrice,
                       BigDecimal discountPercent) {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.sizeLabel = (sizeLabel != null && !sizeLabel.trim().isEmpty()) ? sizeLabel : "Free Size";
        this.quantity = Math.max(1, quantity);
        this.originalPrice = originalPrice != null ? originalPrice : BigDecimal.ZERO;
        this.discountPercent = discountPercent != null ? discountPercent : BigDecimal.ZERO;

        // Calculate discounted price
        if (this.discountPercent.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountMultiplier = BigDecimal.valueOf(100).subtract(this.discountPercent)
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            this.unitPrice = this.originalPrice.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
        } else {
            this.unitPrice = this.originalPrice.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public String getItemKey() {
        return productId + "_" + (sizeLabel != null ? sizeLabel : "");
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getOriginalSubtotal() {
        return originalPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSavings() {
        return getOriginalSubtotal().subtract(getSubtotal());
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSizeLabel() {
        return sizeLabel;
    }

    public void setSizeLabel(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
