package com.universidad.refactoringu11.inventario;

/**
 * Abstraccion que vive en el paquete de dominio (inventario): quien quiera
 * enterarse de un bajo stock implementa esta interfaz, sin que inventario
 * conozca a quien la implementa.
 */
public interface NotificadorBajoStock {
    void notificar(Producto producto, int cantidadReorden);
}
