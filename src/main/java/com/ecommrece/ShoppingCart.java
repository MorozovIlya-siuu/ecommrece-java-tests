package com.ecommrece;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<String, Integer> items;
    private Map<String, Product> catalog;
    private double discountPercent;

    public ShoppingCart() {
        this.items = new HashMap<>();
        this.catalog = new HashMap<>();
        this.discountPercent = 0.0;
    }

    public void registerProduct(Product product) {
        catalog.put(product.getId(), product);
    }

    public void addItem(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество товара должно быть больше 0");
        }
        if (!catalog.containsKey(productId)) {
            throw new IllegalArgumentException("Товар в каталоге не найден");
        }
        Product product = catalog.get(productId);
        int currentInCart = items.containsKey(productId) ? items.get(productId) : 0;
        if (currentInCart + quantity > product.getStock()) {
            throw new IllegalArgumentException("Недостаточно товара на складе. Доступно: " + product.getStock());
        }
        items.put(productId, currentInCart + quantity);
    }

    public void removeItem(String productId, Integer quantity) {
        if (!items.containsKey(productId)) {
            throw new IllegalArgumentException("Товар не найден в корзине");
        }
        if (quantity == null || quantity >= items.get(productId)) {
            items.remove(productId);
        } else {
            items.put(productId, items.get(productId) - quantity);
        }
    }

    public void removeItem(String productId) {
        removeItem(productId, null);
    }

    public double getTotalCost() {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            total += catalog.get(entry.getKey()).getPrice() * entry.getValue();
        }
        if (discountPercent > 0) {
            total = total * (100 - discountPercent) / 100;
        }
        return total;
    }

    public void applyPromoCode(String promoCode) {
        if (promoCode == null || promoCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Промокод должен быть непустой строкой");
        }
        String code = promoCode.toUpperCase().trim();
        Map<String, Double> promoCodes = new HashMap<>();
        promoCodes.put("SALE10", 10.0);
        promoCodes.put("SALE20", 20.0);
        promoCodes.put("HALF", 50.0);
        if (!promoCodes.containsKey(code)) {
            throw new IllegalArgumentException("Недействительный промокод: " + code);
        }
        if (discountPercent > 0) {
            throw new IllegalArgumentException("Промокод уже применён. Нельзя использовать несколько промокодов");
        }
        discountPercent = promoCodes.get(code);
    }

    public void resetPromoCode() {
        discountPercent = 0.0;
    }

    public double getDiscountPercent() { return discountPercent; }
    public Map<String, Integer> getItems() { return items; }
}