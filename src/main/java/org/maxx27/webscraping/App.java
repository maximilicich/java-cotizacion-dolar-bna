package org.maxx27.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Map;
import java.util.HashMap;


/**
 * 
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String url = "https://www.bna.com.ar/Personas";
        
        System.out.println( "Buscador de Cotizaciones Dolar Compra/Venta en " + url );
        
        Response response = null;
        
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
            
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            
            Map<String, String> cotizaciones = scrapCotizacionDolar(doc);
            
            System.out.println("COTIZACIONES:");
            System.out.println("COMPRA:" + cotizaciones.get("compra"));
            System.out.println("VENTA:" + cotizaciones.get("venta"));
            
            
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
        }
        
        System.out.println( "status: " + response.statusCode());
    }
    
    

    private static Map<String, String> scrapCotizacionDolar(Document dom) {
        
        /*
                <div class="tab-pane fade" id="divisas">
                <table class="table cotizacion">
                    <thead>
                        <tr>
                        <th class="fechaCot">14/2/2020</th>
                        <th>Compra</th>
                        <th>Venta</th>
                        </tr>
                    </thead>
                    <tbody>
                  
                            <tr>
                            <td class="tit">Dolar U.S.A</td>
                            <td>61.2590</td>
                            <td>61.4590</td>
                            </tr>
                            <tr>
                            <td class="tit">Libra Esterlina</td>
                            <td>79.8572</td>
                            <td>80.3023</td>        
                ...
                ...
        */
        
        Element sectionDivisas = dom.getElementById("divisas");
        
        Elements celdas = sectionDivisas.select("td");
        
        String cotizacionCompra = null;
        String cotizacionVenta = null;
        
        for (Element unaCelda : celdas) {
            // la cotizacion dolar divisa compra es el td que esta imediatamente LUEGO DE "Dolar U.S.A"
            // y la cotizacion venta es el td que esta inmediatamente LUEGO de la cotizacion compra
            if ("Dolar U.S.A".equalsIgnoreCase(unaCelda.text())) {
                cotizacionCompra = celdas.next().first().text();
                cotizacionVenta = celdas.next().next().first().text();
                break;
            }
        }
        
        Map<String, String> cotizaciones = new HashMap<String, String>(2);
        
        cotizaciones.put("compra", cotizacionCompra);
        cotizaciones.put("venta", cotizacionVenta);
        
        return cotizaciones;
    }
}
