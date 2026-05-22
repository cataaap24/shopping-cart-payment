package isi.shoppingCart.usecases.dto;

public class PaymentRequest {

    private final int clientId;
    private final double totalAmount;

    public PaymentRequest(int clientId, double totalAmount) {
        this.clientId = clientId;
        this.totalAmount = totalAmount;
    }

    public int getClientId() {
        return clientId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

}
