package isi.shoppingCart.usecases.dto;

public class PaymentResult {
    private boolean approved;
    private String message;
    private String reference;

    public PaymentResult(boolean approved, String reference, String message) {
        this.approved = approved;
        this.message = message;
        this.reference = reference;
    }

    public static PaymentResult approved(String reference, String message) {
        return new PaymentResult(true, reference, message);
    }

    public static PaymentResult rejected(String reference, String message) {
        return new PaymentResult(false, reference, message);
    }

    public boolean isApproved() {
        return approved;
    }

    public String getMessage() {
        return message;
    }

    public String getReference() {
        return reference;
    }

}
