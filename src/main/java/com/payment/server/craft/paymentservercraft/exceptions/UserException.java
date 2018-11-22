package com.payment.server.craft.paymentservercraft.exceptions;

public class UserException extends Exception {
    /*
     * Default constructor
     */
    public UserException()
    {
        super();
    }

    /*
     * Constructs a new exception with the specified detail message.
     */
    public UserException(String message)
    {
        super(message);
    }

    /*
     * Constructs a new exception with the specified detail message and cause.
     */
    public UserException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
