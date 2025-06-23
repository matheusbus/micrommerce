package com.esw.paymentservice.model;

public enum PaymentStatus {
    PENDING((short) 1),
    COMPLETED((short) 2),
    FAILED((short) 3);

    private Short value;

    private PaymentStatus(Short value) {
        this.value = value;
    }

    public Short getValue() {
        return this.value;
    }

    public static PaymentStatus valueOf(Short value) {
        for(PaymentStatus ps : PaymentStatus.values()){
            if(ps.getValue().equals(value)){
                return ps;
            }
        }
        throw new IllegalArgumentException("Invalid Payment Status. Error at: [PaymentStatus.valueOf(value)]. Must be one of [PENDING, COMPLETED, FAILED]");
    }
}
