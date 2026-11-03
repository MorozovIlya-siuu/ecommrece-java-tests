package com.ecommrece;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void testProductCreationSuccess() {
        Product product = new Product("prod_1", "Ноутбук", 50000.0, 10);
        assertEquals("prod_1", product.getId());
        assertEquals("Ноутбук", product.getName());
        assertEquals(50000.0, product.getPrice());
        assertEquals(10, product.getStock());
    }

    @Test
    void testProductCreationInvalidPrice() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new Product("prod_2", "Телефон", -100.0, 5);
        });
        assertEquals("Цена и количество на складе не могут быть отрицательными", ex.getMessage());
    }

    @Test
    void testProductCreationInvalidStock() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new Product("prod_3", "Планшет", 20000.0, -1);
        });
        assertEquals("Цена и количество на складе не могут быть отрицательными", ex.getMessage());
    }
}