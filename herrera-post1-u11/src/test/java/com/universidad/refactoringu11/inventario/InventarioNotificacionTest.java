package com.universidad.refactoringu11.inventario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventarioNotificacionTest {

    private InventarioService service;
    private int veces;
    private int ultimaCantidad;
    private String ultimoProducto;

    @BeforeEach
    void setUp() {
        service = new InventarioService();
        service.agregarProducto(new Producto(1L, "Clean Code", TipoProducto.LIBRO, 45.0, 20, 5));
        veces = 0;
        service.setNotificador((producto, cantidadReorden) -> {
            veces++;
            ultimoProducto = producto.getNombre();
            ultimaCantidad = cantidadReorden;
        });
    }

    @Test
    void bajoStock_notificaUnaVezConProductoYCantidadDeReorden() {
        service.actualizarInventario(1L, 17, "SALIDA");
        assertEquals(1, veces);
        assertEquals("Clean Code", ultimoProducto);
        assertEquals(12, ultimaCantidad);
    }

    @Test
    void stockSuficiente_noNotifica() {
        service.actualizarInventario(1L, 8, "SALIDA");
        assertEquals(0, veces);
    }
}
