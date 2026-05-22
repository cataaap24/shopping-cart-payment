package isi.shoppingCart.usecases.ports;

import isi.shoppingCart.entities.Payment;

public interface PaymentRepository {
    void save(Payment payment);
    int getNextId();
}
