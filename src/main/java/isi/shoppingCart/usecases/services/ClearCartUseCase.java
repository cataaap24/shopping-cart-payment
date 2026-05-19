package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.*;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.CustomerRepository;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;

public class ClearCartUseCase {
    private CartRepository cartRepository;

    public ClearCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public OperationResult execute() {
        Cart cart = cartRepository.getCart();
        OperationResult result = null;

        if (cart == null) result = OperationResult.fail("Error interno, NO EXISTE carrito.");
        else if (cart.getItems().isEmpty()) result = OperationResult.fail("El carrito ya está vacío.");
        else {
            cartRepository.save(new Cart());
            result = OperationResult.ok("El Carrito ha sido vaciado.");
        }
        return result;
    }
}
