package isi.shoppingCart.infrastructure.repositories;

import isi.shoppingCart.usecases.dto.PaymentRequest;
import isi.shoppingCart.usecases.dto.PaymentResult;
import isi.shoppingCart.usecases.ports.PaymentGateway;

public class InMemoryFakePaymentGateway implements PaymentGateway {

    // Pagos superiores a 1000.0 serán rechazados.
    private static final double MAX_APPROVED_AMOUNT = 1000.0;

    @Override
    public PaymentResult processPayment(PaymentRequest request) {

        if (request.getTotalAmount() > MAX_APPROVED_AMOUNT) {

            String rejectionReference = "ERR-" + request.getClientId() + "-" + System.currentTimeMillis();
            String message = "Transacción SIM-FAIL: Rechazada porque el monto excede el límite de simulación ($" + MAX_APPROVED_AMOUNT + ").";

            return PaymentResult.rejected(rejectionReference, message);
        }

        String fakeTransactionId = "SIM-TX-" + request.getClientId() + "-" + System.currentTimeMillis();
        String approvalMessage = "Pago aprobado exitosamente. Monto: " + request.getTotalAmount();

        return PaymentResult.approved(fakeTransactionId, approvalMessage);
    }
}
