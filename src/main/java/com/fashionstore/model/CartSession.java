package com.fashionstore.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, CartItemDto> items = new LinkedHashMap<>();

    public void addItem(CartItemDto newItem) {
        if (newItem == null) return;
        String key = newItem.getItemKey();
        if (items.containsKey(key)) {
            CartItemDto existing = items.get(key);
            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
        } else {
            items.put(key, newItem);
        }
    }

    public void updateQuantity(String itemKey, int quantity) {
        if (quantity <= 0) {
            removeItem(itemKey);
        } else if (items.containsKey(itemKey)) {
            items.get(itemKey).setQuantity(quantity);
        }
    }

    public void incrementQuantity(String itemKey) {
        if (items.containsKey(itemKey)) {
            CartItemDto item = items.get(itemKey);
            item.setQuantity(item.getQuantity() + 1);
        }
    }

    public void decrementQuantity(String itemKey) {
        if (items.containsKey(itemKey)) {
            CartItemDto item = items.get(itemKey);
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                removeItem(itemKey);
            }
        }
    }

    public void removeItem(String itemKey) {
        items.remove(itemKey);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Collection<CartItemDto> getItems() {
        return items.values();
    }

    public int getTotalItems() {
        int count = 0;
        for (CartItemDto item : items.values()) {
            count += item.getQuantity();
        }
        return count;
    }

    public BigDecimal getSubtotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDto item : items.values()) {
            total = total.add(item.getSubtotal());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getOriginalTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDto item : items.values()) {
            total = total.add(item.getOriginalSubtotal());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalSavings() {
        return getOriginalTotal().subtract(getSubtotal()).max(BigDecimal.ZERO);
    }

    public BigDecimal getShippingFee() {
        if (isEmpty()) return BigDecimal.ZERO;
        // Free shipping if total is >= 999
        if (getSubtotal().compareTo(BigDecimal.valueOf(999)) >= 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(99.00).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalAmount() {
        return getSubtotal().add(getShippingFee()).setScale(2, RoundingMode.HALF_UP);
    }
}
