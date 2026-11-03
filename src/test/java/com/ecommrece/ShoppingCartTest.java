package com.ecommrece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

    private ShoppingCart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        product = new Product("prod_1", "Ноутбук", 10000.0, 10);
        cart.registerProduct(product);
    }

    @Test
    void testAddItemSuccess() {
        cart.addItem("prod_1", 2);
        assertEquals(2, cart.getItems().get("prod_1"));
    }

    @Test
    void testAddItemUpdatesQuantity() {
        cart.addItem("prod_1", 2);
        cart.addItem("prod_1", 3);
        assertEquals(5, cart.getItems().get("prod_1"));
    }

    @Test
    void testAddItemExceedsStock() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("prod_1", 15);
        });
        assertTrue(ex.getMessage().contains("Недостаточно товара"));
    }

    @Test
    void testAddItemInvalidQuantityZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("prod_1", 0);
        });
        assertEquals("Количество товара должно быть больше 0", ex.getMessage());
    }

    @Test
    void testAddItemInvalidQuantityNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("prod_1", -5);
        });
        assertEquals("Количество товара должно быть больше 0", ex.getMessage());
    }

    @Test
    void testAddItemNotInCatalog() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("unknown_id", 1);
        });
        assertEquals("Товар не найден в каталоге", ex.getMessage());
    }

    @Test
    void testRemoveItemPartial() {
        cart.addItem("prod_1", 5);
        cart.removeItem("prod_1", 2);
        assertEquals(3, cart.getItems().get("prod_1"));
    }

    @Test
    void testRemoveItemFull() {
        cart.addItem("prod_1", 5);
        cart.removeItem("prod_1");
        assertFalse(cart.getItems().containsKey("prod_1"));
    }

    @Test
    void testRemoveItemExactAmount() {
        cart.addItem("prod_1", 5);
        cart.removeItem("prod_1", 5);
        assertFalse(cart.getItems().containsKey("prod_1"));
    }

    @Test
    void testRemoveItemNotInCart() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.removeItem("prod_1");
        });
        assertEquals("Товар не найден в корзине", ex.getMessage());
    }

    @Test
    void testGetTotalCostEmpty() {
        assertEquals(0.0, cart.getTotalCost());
    }

    @Test
    void testGetTotalCostWithItems() {
        cart.addItem("prod_1", 2);
        assertEquals(20000.0, cart.getTotalCost());
    }

    @Test
    void testApplyValidPromoSale10() {
        cart.addItem("prod_1", 2);
        cart.applyPromoCode("SALE10");
        assertEquals(18000.0, cart.getTotalCost());
    }

    @Test
    void testApplyValidPromoSale20() {
        cart.addItem("prod_1", 2);
        cart.applyPromoCode("SALE20");
        assertEquals(16000.0, cart.getTotalCost());
    }

    @Test
    void testApplyValidPromoHalf() {
        cart.addItem("prod_1", 2);
        cart.applyPromoCode("HALF");
        assertEquals(10000.0, cart.getTotalCost());
    }

    @Test
    void testApplyPromoCaseInsensitive() {
        cart.addItem("prod_1", 2);
        cart.applyPromoCode("sale10");
        assertEquals(18000.0, cart.getTotalCost());
    }

    @Test
    void testApplyPromoWithSpaces() {
        cart.addItem("prod_1", 2);
        cart.applyPromoCode("  SALE10  ");
        assertEquals(18000.0, cart.getTotalCost());
    }

    @Test
    void testApplyInvalidPromo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.applyPromoCode("INVALID");
        });
        assertTrue(ex.getMessage().contains("Недействительный промокод"));
    }

    @Test
    void testApplyEmptyPromo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.applyPromoCode("");
        });
        assertEquals("Промокод должен быть непустой строкой", ex.getMessage());
    }

    @Test
    void testApplyNullPromo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.applyPromoCode(null);
        });
        assertEquals("Промокод должен быть непустой строкой", ex.getMessage());
    }

    @Test
    void testApplyMultiplePromos() {
        cart.applyPromoCode("SALE10");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cart.applyPromoCode("SALE20");
        });
        assertTrue(ex.getMessage().contains("Промокод уже применён"));
    }

    @Test
    void testResetPromoCode() {
        cart.addItem("prod_1", 2);
        cart.applyPromoCode("SALE10");
        assertEquals(18000.0, cart.getTotalCost());
        cart.resetPromoCode();
        assertEquals(20000.0, cart.getTotalCost());
        assertEquals(0.0, cart.getDiscountPercent());
    }

    @Test
    void testGetTotalCostWithoutPromo() {
        cart.addItem("prod_1", 2);
        assertEquals(20000.0, cart.getTotalCost());
    }
}