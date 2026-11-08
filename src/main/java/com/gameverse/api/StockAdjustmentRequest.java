package com.gameverse.api;

/**
 * Cuerpo JSON del ajuste relativo de stock.
 */
public class StockAdjustmentRequest {

    private Integer cambio;

    public StockAdjustmentRequest() {
    }

    public Integer getCambio() {
        return cambio;
    }

    public void setCambio(Integer cambio) {
        this.cambio = cambio;
    }
}
