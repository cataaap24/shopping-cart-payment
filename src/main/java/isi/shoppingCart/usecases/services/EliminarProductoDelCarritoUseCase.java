package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.ports.CartRepository;

public class EliminarProductoDelCarritoUseCase {
    private CartRepository cartRepository;

    public EliminarProductoDelCarritoUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public OperationResult execute(int productId) {
        Cart cart = cartRepository.getCart();
        cart.removeProduct(productId);
        cartRepository.save(cart);
        return OperationResult.ok("Producto eliminado del carrito.");
    }
}

