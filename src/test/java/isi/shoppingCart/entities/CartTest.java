package isi.shoppingCart.entities;

import org.junit.jupiter.api.Test;
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

}