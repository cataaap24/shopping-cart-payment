package isi.shoppingCart.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    @Test
    public void testCartWhenEmpty() {
        Cart cart = new Cart();
        assertFalse(cart.esAptoParaPago());
    }

    @Test
    public void testCartWithProducts() {
        Cart cart = new Cart();
        cart.addProduct(new Product(1,
                "Producto Prueba",
                100.0,
                10));
        assertTrue(cart.esAptoParaPago());
    }

    @Test
    public void insufficientInventory() {
        Cart cart = new Cart();
        Product product = new Product(1, "Mouse", 80.0, 2);
        cart.addProduct(product);
        cart.addProduct(product);
        cart.addProduct(product);
        List<CartItem> items = cart.getItems();
        boolean insufficientInventory = items.getFirst().getQuantity() > product.getAvailableQuantity();
        assertTrue(insufficientInventory);
    }
}