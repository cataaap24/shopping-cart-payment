package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.usecases.ports.CartRepository;

public class ReducirCantidadDelCarritoUseCase {
    private CartRepository cartRepository;

    public ReducirCantidadDelCarritoUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(int productId) {
        Cart cart = cartRepository.getCart();
        if (cart == null) return;
        cart.decreaseProduct(productId);
        cartRepository.save(cart);
    }
}
