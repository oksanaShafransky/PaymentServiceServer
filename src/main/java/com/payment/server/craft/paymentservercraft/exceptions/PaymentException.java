package com.payment.server.craft.paymentservercraft.exceptions;

public class PaymentException extends Exception {
    /*
     * Default constructor
     */
    public PaymentException()
    {
        super();
    }

    /*
     * Constructs a new exception with the specified detail message.
     */
    public PaymentException(String message)
    {
        super(message);
    }

    /*
     * Constructs a new exception with the specified detail message and cause.
     */
    public PaymentException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
