/**
 * Author: Mihir
 * Date: 2023-10-06
 * Description: custom exception class for the Mall Billing System.
 * Version: 1.0
 */
package Mallbillingsystem3;

/**
 * Custom exception class for handling negative input errors.
 * This exception is thrown when negative input is encountered.
 */
public class NegativeInput extends Exception {
    /**
     * Constructs a new NegativeInput exception with the specified error message.
     * 
     */
    NegativeInput(String s) {
        super(s);
    }
}

