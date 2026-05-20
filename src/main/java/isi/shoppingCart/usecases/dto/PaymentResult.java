package isi.shoppingCart.usecases.dto;

public class PaymentResult {
    private boolean approved;
    private String message;

    public PaymentResult(boolean approved, String message) {
        this.approved = approved;
        this.message = message;
    }

    public static PaymentResult approved(String message) {
        return new PaymentResult(true, message);
    }

    public static PaymentResult rejected(String message) {
        return new PaymentResult(false, message);
    }

    public boolean isApproved() {
        return approved;
    }

    public String getMessage() {
        return message;
    }
}
