package cc.aliko.payment_service.dto;

import lombok.Data;

@Data
public class DirectPaymentRequest {

    private String cc_holder_name;
    private String cc_no;
    private String expiry_month;
    private String expiry_year;
    private String cvv;
    private String currency_code;
    private String installments_number;
    private String invoice_id;
    private String invoice_description;
    private String total;
    private String items; // Dokümantasyonda string formatında istenmiş
    private String name;
    private String surname;
    
    private String cancel_url;
    private String return_url;
}
