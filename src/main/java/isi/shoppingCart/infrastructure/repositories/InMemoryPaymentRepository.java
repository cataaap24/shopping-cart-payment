package isi.shoppingCart.infrastructure.repositories;

import isi.shoppingCart.entities.Payment;
import isi.shoppingCart.usecases.ports.PaymentRepository;
import java.util.ArrayList;
import java.util.List;

public class InMemoryPaymentRepository implements PaymentRepository {
    private List<Payment> payments;
    private int nextId;

    public InMemoryPaymentRepository() {
        payments = new ArrayList<Payment>();
    }

    public void save(Payment payment) {
        payments.add(payment);
    }

    public List<Payment> findAll() {
        return payments;
    }

    public int getNextId() {
        int id = nextId;
        nextId = nextId + 1;
        return id;
    }
}
