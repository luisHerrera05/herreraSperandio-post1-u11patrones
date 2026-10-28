package com.universidad.refactoringu11.reportes;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.Producto;
import com.universidad.refactoringu11.inventario.TipoProducto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReporteBajoStockServiceTest {

    @Test
    void sinAlertas_reporteLoIndica() {
        assertEquals("Reporte de bajo stock: sin alertas", new ReporteBajoStockService().generarReporte());
    }

    @Test
    void integradoConInventario_reporteIncluyeLaAlerta() {
        ReporteBajoStockService reportes = new ReporteBajoStockService();
        InventarioService inventario = new InventarioService();
        inventario.setNotificador(reportes);
        inventario.agregarProducto(new Producto(1L, "Clean Code", TipoProducto.LIBRO, 45.0, 20, 5));

        inventario.actualizarInventario(1L, 17, "SALIDA");

        String reporte = reportes.generarReporte();
        assertTrue(reporte.contains("Clean Code"));
        assertTrue(reporte.contains("reordenar 12 unidades"));
    }
}
