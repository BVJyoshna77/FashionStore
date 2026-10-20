
package com.fashionstore.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CartItem {

    private int cartItemId;
    private int cartId;
    private int productId;
    private String sizeLabel;
    private int quantity;
    private BigDecimal unitPrice;
    private Timestamp addedAt;

    public CartItem() {
    }

    public CartItem(int cartItemId, int cartId, int productId,
                    String sizeLabel, int quantity,
                    BigDecimal unitPrice, Timestamp addedAt) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.sizeLabel = sizeLabel;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.addedAt = addedAt;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }
}
