package com.universidad.refactoringu11.inventario;

/**
 * Politica de reposicion: decide cuando hay que reordenar y cuantas unidades pedir.
 * Tiene su propia razon de cambio (la politica comercial), distinta de la del servicio.
 */
public class CalculadoraReorden {

    private static final int FACTOR_REPOSICION = 3;

    public boolean necesitaReorden(int stockMinimo, int stockActual) {
        return stockActual < stockMinimo;
    }

    public int calcularCantidad(int stockMinimo, int stockActual) {
        return (stockMinimo * FACTOR_REPOSICION) - stockActual;
    }
}
