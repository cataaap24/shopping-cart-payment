package isi.shoppingCart.usecases.dto;

public class PaymentRequest {

    private final String clientId;
    private final double totalAmount;

    public PaymentRequest(String clientId, double totalAmount) {
        this.clientId = clientId;
        this.totalAmount = totalAmount;
    }

    public String getClientId() {
        return clientId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

}