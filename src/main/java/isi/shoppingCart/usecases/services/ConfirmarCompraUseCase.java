package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.*;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.dto.PaymentRequest;
import isi.shoppingCart.usecases.dto.PaymentResult;
import isi.shoppingCart.usecases.ports.*;

import java.util.List;

public class ConfirmarCompraUseCase {
    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private PurchaseRepository purchaseRepository;
    private ProductRepository productRepository;
    private PaymentRepository paymentRepository;
    private PaymentGateway paymentGateway;

    public ConfirmarCompraUseCase(CartRepository cartRepository,
                                  CustomerRepository customerRepository,
                                  PurchaseRepository purchaseRepository, ProductRepository productRepository,
                                  PaymentGateway paymentGateway, PaymentRepository paymentRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
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

        //Procesar pago
        PaymentRequest paymentRequest = new PaymentRequest(customer.getId(), cart.getTotal());
        PaymentResult result = paymentGateway.processPayment(paymentRequest);

        if (!result.isApproved()) {
            return OperationResult.fail("Pago rechazado por la pasarela de pago simulada. Detalle: " + result.getMessage());
        }

        Payment payment = new Payment(paymentRepository.getNextId(), cart.getTotal(), result.isApproved(), result.getReference());
        paymentRepository.save(payment);

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

        return OperationResult.ok(
                "Compra " + purchase.getId() + " confirmada para " + customer.getName() + ". Total: $ " + purchase.getTotal()
               + ". Referencia de pago: " + payment.getReference());
    }
}
