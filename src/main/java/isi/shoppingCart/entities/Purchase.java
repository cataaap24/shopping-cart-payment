package isi.shoppingCart.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Purchase {
    private int id;
    private Customer customer;
    private List<PurchaseItem> items;
    private Payment payment;

    public Purchase(int id, Customer customer, Payment payment) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<PurchaseItem>();
        this.payment = payment;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<PurchaseItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(PurchaseItem item) {
        items.add(item);
    }

    public double getTotal() {
        double total = 0.0;
        int i;

        for (i = 0; i < items.size(); i++) {
            total = total + items.get(i).getSubtotal();
        }

        return total;
    }
}
