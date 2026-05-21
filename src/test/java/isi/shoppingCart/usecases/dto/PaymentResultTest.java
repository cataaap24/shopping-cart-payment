package isi.shoppingCart.usecases.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentResultTest {


    @Test
    void getReferenceWhenApproved() {
        PaymentResult paymentResult = PaymentResult.approved("123456", "Pago aprobado");
        assertEquals("123456", paymentResult.getReference());
    }

    @Test
    void getMessageWhenApproved() {
        PaymentResult paymentResult = PaymentResult.approved("123456", "Pago aprobado");
        assertEquals("Pago aprobado", paymentResult.getMessage());
    }

    @Test
    void approvedIsApproved() {
        PaymentResult paymentResult = PaymentResult.approved("123",
                "Pago aprobado");
        assertTrue(paymentResult.isApproved());
    }

    @Test
    void rejectedIsapproved() {
        PaymentResult paymentResult = PaymentResult.rejected("123",
                "Pago rechazado");
        assertFalse(paymentResult.isApproved());
    }
}