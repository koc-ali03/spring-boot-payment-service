package cc.aliko.payment_service.service;

import cc.aliko.payment_service.dto.DirectPaymentRequest;

public interface PaymentService {
    String directPayment(DirectPaymentRequest request, String userToken);
}
