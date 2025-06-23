package com.esw.paymentservice.model;

public enum PaymentMethod {
    PIX((short) 1),
    TICKET((short) 2),
    CREDIT_CARD((short) 3),
    DEBIT_CARD((short) 4);

    private Short value;

    private PaymentMethod(Short value) {
        this.value = value;
    }

    public Short getValue() {
        return this.value;
    }

    public static PaymentMethod valueOf(Short value) {
        for(PaymentMethod pm : PaymentMethod.values()){
            if(pm.getValue().equals(value)){
                return pm;
            }
        }
        throw new IllegalArgumentException("Invalid Payment Method. Error at: [PaymentMethod.valueOf(value)]. Must be one of [PIX, TICKET, CREDIT_CARD, DEBIT_CARD]");
    }
}