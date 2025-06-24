package com.esw.paymentservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSingleton {

    private static LoggerSingleton instance;
    private final Logger logger;

    private LoggerSingleton() {
        this.logger = LoggerFactory.getLogger("PaymentServiceLogger");
    }

    public static synchronized LoggerSingleton getInstance() {
        if (instance == null) {
            instance = new LoggerSingleton();
        }
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }
}
