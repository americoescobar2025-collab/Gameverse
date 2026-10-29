package com.gameverse.inventory;

public class InventoryPersistenceException extends RuntimeException {

    public InventoryPersistenceException(String message) {
        super(message);
    }

    public InventoryPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
