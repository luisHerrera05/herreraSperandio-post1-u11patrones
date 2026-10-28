package com.universidad.refactoringu11.reportes;

import com.universidad.refactoringu11.inventario.NotificadorBajoStock;
import com.universidad.refactoringu11.inventario.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteBajoStockService implements NotificadorBajoStock {

    private final List<String> alertas = new ArrayList<>();

    @Override
    public void notificar(Producto producto, int cantidadReorden) {
        alertas.add(producto.getNombre() + " (stock " + producto.getStock()
                + ", minimo " + producto.getStockMinimo() + ") -> reordenar "
                + cantidadReorden + " unidades");
    }

    public String generarReporte() {
        if (alertas.isEmpty()) {
            return "Reporte de bajo stock: sin alertas";
        }
        StringBuilder reporte = new StringBuilder("Reporte de bajo stock:");
        for (String alerta : alertas) {
            reporte.append("\n - ").append(alerta);
        }
        return reporte.toString();
    }
}
