package com.universidad.refactoringu11.inventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventarioService {

    private static final String ENTRADA = "ENTRADA";
    private static final String SALIDA = "SALIDA";

    private final List<Producto> productos = new ArrayList<>();
    private final List<String> movimientos = new ArrayList<>();
    private final CalculadoraReorden calculadoraReorden = new CalculadoraReorden();

    private NotificadorBajoStock notificador; // depende de la abstraccion, no de reportes

    @Autowired(required = false)
    public void setNotificador(NotificadorBajoStock notificador) {
        this.notificador = notificador;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public String actualizarInventario(Long productoId, int cantidad, String tipoMovimiento) {
        if (!esEntradaValida(productoId, cantidad, tipoMovimiento)) return "ERROR_DATOS_INVALIDOS";
        Producto producto = buscarProducto(productoId);
        if (producto == null) return "ERROR_PRODUCTO_NO_EXISTE";
        int nuevoStock = calcularNuevoStock(producto, cantidad, tipoMovimiento);
        if (SALIDA.equals(tipoMovimiento) && nuevoStock < 0) return "ERROR_STOCK_INSUFICIENTE";
        producto.setStock(nuevoStock);
        registrarMovimiento(tipoMovimiento, cantidad, producto, nuevoStock);
        return resolverReorden(producto, nuevoStock);
    }

    private boolean esEntradaValida(Long productoId, int cantidad, String tipoMovimiento) {
        return productoId != null && cantidad != 0
                && (tipoMovimiento.equals(ENTRADA) || tipoMovimiento.equals(SALIDA));
    }

    private Producto buscarProducto(Long productoId) {
        for (Producto p : productos) {
            if (p.getId().equals(productoId)) return p;
        }
        return null;
    }

    private int calcularNuevoStock(Producto producto, int cantidad, String tipoMovimiento) {
        return ENTRADA.equals(tipoMovimiento)
                ? producto.getStock() + cantidad
                : producto.getStock() - cantidad;
    }

    private void registrarMovimiento(String tipoMovimiento, int cantidad,
                                     Producto producto, int nuevoStock) {
        String registro = tipoMovimiento + " de " + cantidad + " unidades de "
                + producto.getNombre() + " -> stock final: " + nuevoStock;
        movimientos.add(registro);
        System.out.println(registro);
    }

    private String resolverReorden(Producto producto, int nuevoStock) {
        if (!calculadoraReorden.necesitaReorden(producto.getStockMinimo(), nuevoStock)) {
            return "OK_" + nuevoStock;
        }
        int cantidadReorden = calculadoraReorden.calcularCantidad(producto.getStockMinimo(), nuevoStock);
        alertarBajoStock(producto, cantidadReorden);
        return "OK_BAJO_STOCK_REORDEN_" + cantidadReorden;
    }

    private void alertarBajoStock(Producto producto, int cantidadReorden) {
        if (notificador != null) {
            notificador.notificar(producto, cantidadReorden);
        }
        System.out.println("ALERTA: " + producto.getNombre()
                + " bajo stock minimo. Reordenar " + cantidadReorden + " unidades.");
    }
}
