package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Customer;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.entities.PurchaseItem;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.CustomerRepository;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;

import java.util.List;

public class ConfirmarCompraUseCase {
    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private PurchaseRepository purchaseRepository;
    private ProductRepository productRepository;

    public ConfirmarCompraUseCase(CartRepository cartRepository,
                                  CustomerRepository customerRepository,
                                  PurchaseRepository purchaseRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
    }

    public OperationResult execute() {
        Cart cart = cartRepository.getCart();

        if (cart == null || !cart.esAptoParaPago()) {
            return OperationResult.fail("El carrito no es apto para iniciar el pago.");
        }

        Customer customer = customerRepository.getCustomer();

        if (customer == null) {
            return OperationResult.fail("No hay cliente registrado.");
        }

        List<CartItem> items = cart.getItems();
        int i;

        for (i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = item.getProduct();

            if (item.getQuantity() > product.getAvailableQuantity()) {
                return OperationResult.fail("No hay disponibilidad suficiente para: " + product.getName() + ".");
            }
        }

        Purchase purchase = new Purchase(purchaseRepository.getNextId(), customer);

        for (i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = item.getProduct();

            product.decreaseAvailableQuantity(item.getQuantity());
            purchase.addItem(new PurchaseItem(product, item.getQuantity(), product.getPrice()));
            productRepository.save(product);
        }

        purchaseRepository.save(purchase);
        cartRepository.save(new Cart());

        return OperationResult.ok("Compra " + purchase.getId() + " confirmada para " + customer.getName() + ". Total: $ " + purchase.getTotal());
    }
}
/*
``` java
package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Customer;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.entities.PurchaseItem;
import isi.shoppingCart.entities.Payment;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.dto.PaymentRequest;
import isi.shoppingCart.usecases.dto.PaymentResult;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.CustomerRepository;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;
import isi.shoppingCart.usecases.ports.PaymentGateway;
import isi.shoppingCart.usecases.ports.PaymentRepository;

import java.util.List;

public class ConfirmarCompraUseCase {
    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private PurchaseRepository purchaseRepository;
    private ProductRepository productRepository;
    private PaymentGateway paymentGateway;
    private PaymentRepository paymentRepository;

    public ConfirmarCompraUseCase(CartRepository cartRepository,
                                  CustomerRepository customerRepository,
                                  PurchaseRepository purchaseRepository,
                                  ProductRepository productRepository,
                                  PaymentGateway paymentGateway,
                                  PaymentRepository paymentRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.paymentGateway = paymentGateway;
        this.paymentRepository = paymentRepository;
    }

    public OperationResult execute() {
        Cart cart = cartRepository.getCart();

        // Escenario de Excepción 1: Carrito no apto para iniciar pago (Validación de Martín Pineda Jaramillo)
        if (cart == null || !cart.esAptoParaPago()) {
            return OperationResult.fail("El carrito no es apto para iniciar el pago.");
        }

        Customer customer = customerRepository.getCustomer();

        // Escenario de Excepción 2: Cliente inexistente o no registrado (Analizado por Juan David Navarro Bermudez)
        if (customer == null) {
            return OperationResult.fail("No hay cliente registrado.");
        }

        List<CartItem> items = cart.getItems();
        int i;

        // Escenario de Excepción 3: Inventario insuficiente
        for (i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = item.getProduct();

            if (item.getQuantity() > product.getAvailableQuantity()) {
                return OperationResult.fail("No hay disponibilidad suficiente para: " + product.getName() + ".");
            }
        }

        // Flujo Normal: Pasos 5 y 6 (Preparación para el pago)
        double totalAmount = cart.getTotalAmount();
        String clientId = customer.getId();
        PaymentRequest request = new PaymentRequest(clientId, totalAmount); // DTO sin simulatedCredentials

        // Flujo Normal: Pasos 7 y 8 (Invocación al puerto PaymentGateway - Implementación de Juan David Navarro Bermudez)
        PaymentResult result = paymentGateway.processPayment(request);

        // Escenario de Excepción 4: Pago rechazado (Integración y validación liderada por Kevin Arturo Panesso Vásquez)
        // Si el monto total supera el umbral de simulación (ej. > $1000.0) en InMemoryFakePaymentGateway.
        if (!result.isApproved()) {
            return OperationResult.fail("Pago rechazado por la pasarela de pago simulada. Detalle: " + result.getMessage());
        }

        // Flujo Normal: Paso 9 (Instanciar y persistir Payment - Microfuncionalidad de Catalina Pineda Posada)
        Payment payment = new Payment(result.getReference(), result.getMessage(), result.isApproved(), totalAmount);
        paymentRepository.save(payment);

        // Flujo Normal: Pasos 10 y 11 (Registro de compra, descuento de inventario y vaciado de carrito)
        Purchase purchase = new Purchase(purchaseRepository.getNextId(), customer, payment);

        for (i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = item.getProduct();

            product.decreaseAvailableQuantity(item.getQuantity());
            purchase.addItem(new PurchaseItem(product, item.getQuantity(), product.getPrice()));
            productRepository.save(product);
        }

        purchaseRepository.save(purchase);
        cartRepository.save(new Cart());

        return OperationResult.ok("Compra " + purchase.getId() + " confirmada para " + customer.getName() + ". Total: $ " + totalAmount);
    }
}

```
 */