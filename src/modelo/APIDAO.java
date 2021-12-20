/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import static java.util.Collections.list;

/**
 *
 * @author Santiago
 */
public class APIDAO {

    public APIDAO() {
    }

    public ArrayList<Api> GetApi(int opcion, int n, int m) {
        // url de la api
        StringBuilder resultado = new StringBuilder();

        ArrayList<Api> liss = new ArrayList<>();
        if (opcion == 1) {
            String URL_API = "https://jsonplaceholder.typicode.com/posts/" + n;
            try {
                //inicio de la conexion 
                URL url = new URL(URL_API);
                URLConnection conexion = url.openConnection();
                //lectura del contenido
                //clase concreta de la api de java
                Reader r = new InputStreamReader(conexion.getInputStream());
                // 
                BufferedReader br = new BufferedReader(r);
                //va a contener cada una de las lineas del get 
                String linea;
                //mientras haya contenido para leer se va a mostrar en la pantalla.
                while ((linea = br.readLine()) != null) {
                    resultado.append(linea);
                }
                //dividimos con el split por comas
                String[] partes = resultado.toString().split(",");
                /*String [] aux = new String[4];
            for (int i =0; i<partes.length;i++){
               // System.out.println("aa " + i);
               //ahora lo separamos por los : y guardamos la segunda parte en el vector
                String []aux2= partes[i].split(":");
                //System.out.println("aber " + aux2[1]);
                aux[i]=aux2[1];
                //System.out.println("aux " + aux[i]);
               // System.out.println(partes[i]);
            }
            String userId = aux[0];
            String id = aux[1];
            String title = aux[2];
            String body = aux[3];*/
                Api a = new Api();
                a = this.separarGet(partes);
                liss.add(a);
            } catch (Exception e) {

            }

        } else if (opcion == 2) {
            int desde = n;
            int hasta = m;

            for (int i = desde; i <= hasta; i++) {
                StringBuilder resultado1 = new StringBuilder();
                // System.out.println("entro al for");
                String URL_API = "https://jsonplaceholder.typicode.com/posts/" + i;
                try {

                    //inicio de la conexion 
                    URL url = new URL(URL_API);
                    URLConnection conexion = url.openConnection();
                    //lectura del contenido
                    //clase concreta de la api de java
                    Reader r = new InputStreamReader(conexion.getInputStream());
                    // 
                    BufferedReader br = new BufferedReader(r);
                    //va a contener cada una de las lineas del get 
                    String linea;
                    //mientras haya contenido para leer se va a mostrar en la pantalla.
                    while ((linea = br.readLine()) != null) {
                        resultado1.append(linea);

                    }
                    String[] partes2 = {""};
                    partes2 = resultado1.toString().split(",");
                    Api a = new Api();
                    a = this.separarGet(partes2);

                    /*  String [] aux = new String[4];
            for (int j =0; j<partes2.length;j++){
               // System.out.println("aa " + i);
               //ahora lo separamos por los : y guardamos la segunda parte en el vector
                String []aux2= partes2[j].split(":");
                //System.out.println("aber " + aux2[1]);
               aux[j]=aux2[1];
               // System.out.println("aux " + aux[i]);
                System.out.println(partes2[j]);
            }
            String userId = aux[0];
               
            String id = aux[1];
            String title = aux[2];
            String body = aux[3];
             Api a = new Api(userId,id,title,body);*/
                    liss.add(a);
                } catch (Exception e) {

                }

            }

        }
        return liss;

    }

    public Api separarGet(String[] partes) {
        String[] aux = new String[4];
        for (int j = 0; j < partes.length; j++) {
            // System.out.println("aa " + i);
            //ahora lo separamos por los : y guardamos la segunda parte en el vector
            String[] aux2 = partes[j].split(":");
            //System.out.println("aber " + aux2[1]);
            //SE GUARDA EN EL VECTOR AUX EL VALOR OBTENIDO DE LA API, PORQUE AL HACER EL SPLIT QUEDA DIVIDO EN "VARIABLE": VALOR 
            aux[j] = aux2[1];
            // System.out.println("aux " + aux[i]);
            // System.out.println(partes[j]);
        }
        String userId = aux[0];

        String id = aux[1];
        String title = aux[2];
        String body = aux[3];
        Api a = new Api(userId, id, title, body);

        return a;

    }

}
