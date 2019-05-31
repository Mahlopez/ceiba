package dominio;

import dominio.repositorio.RepositorioProducto;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";

    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;

    }

    public void generarGarantia(String codigo, String nombre) {
    	
    	//busco el producto por codigo
    	Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
    	
    	//si existe el producto 
    	if(producto != null) {
    		    	
    	//buscar si tiene garantia 
    		if(cantidadVocales(producto.getCodigo()) == 3) {
    			//pendiente la exception
    			throw new GarantiaExtendidaException("Este producto no cuenta con garantía extendida");
    		}else {
    			if(tieneGarantia(codigo)) {
    				//un producto no puede tener mas de una garantia retornar una exception
    				throw new GarantiaExtendidaException("El producto ya cuenta con una garantia extendida");
    			}else {
    				double precio = producto.getPrecio();

    				Calendar nuevaHoy = new GregorianCalendar();
			    	Date hoy = new Date();
			    	nuevaHoy.setTime(hoy);
    				
    				if(precio > 500000) {
    					
    					double precioGarantia = precio * 0.2;
    					Calendar fechaGarantia = sumarDiasFecha(200, nuevaHoy);
    					
    					GarantiaExtendida garantia = new GarantiaExtendida(producto,nuevaHoy.getTime(),
    							fechaGarantia.getTime(),precioGarantia,nombre);
    					
    					repositorioGarantia.agregar(garantia);
    				}else {
    					double precioGarantia = precio * 0.1;
    					Calendar fechaGarantia = sumarDiasFecha(100, nuevaHoy);
    					
    					GarantiaExtendida garantia = new GarantiaExtendida(producto,nuevaHoy.getTime(),
    							fechaGarantia.getTime(),precioGarantia,nombre);
    					repositorioGarantia.agregar(garantia);
    				}
    			}
    		}
    	}else {
    		System.out.println("El producto no exixte");
    	}
    }
    
    public boolean tieneGarantia(String codigo) {
    	
    	if(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo) == null) {
   			return false;
    	}else {
    		return true;
    	}
    }
    
    public int cantidadVocales(String codigo){
    	
    	int cantidad = codigo.replaceAll("[^AEIOUaeiou]","").length();
    	return cantidad;
    }
    
    public Calendar sumarDiasFecha(int dias, Calendar nuevaFecha) {
    	
    	//descontar los dias lunes de la fecha
    	int diasSinlunes = 1;
    	while (diasSinlunes<=dias) 
        {
    		if(nuevaFecha.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
    			diasSinlunes++;
    		}
    		nuevaFecha.add(Calendar.DATE, 1);
        } 
    	
    	if(nuevaFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
    		nuevaFecha.add(Calendar.DATE, 1);
    	}
    	
    	return nuevaFecha;
    }
}
