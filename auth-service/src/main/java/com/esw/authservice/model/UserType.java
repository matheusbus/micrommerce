package com.esw.authservice.model;

public enum UserType {
    BUYER((short) 1),
    SELLER((short) 2);

    private Short value;

    private UserType(Short value) {
        this.value = value;
    }

    public Short getValue() {
        return this.value;
    }

    public static UserType valueOf(Short value) {
        for(UserType ut : UserType.values()){
            if(ut.getValue().equals(value)){
                return ut;
            }
        }
        throw new IllegalArgumentException("Invalid UserType Value. Error at: [UserType.valueOf(short value)]. Must be one of [BUYER, SELLER]");
    }
}
