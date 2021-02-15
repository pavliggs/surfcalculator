package com.khovaylo.surf.exception;

/**
 * @author Pavel Khovaylo
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
