package estancosemaforos;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Santiago Spina
 */
public class Estanco {

    //Mejora n2. se crea un semaforo para controlar si hay o no peticiones de ingredientes.
    private final Semaphore semaforoPeticiones = new Semaphore(0);

    //Semaforo para que el fumador espere a que el ingrediente este producido para poder recogerlo
    private final Semaphore semaforoIngredienteListo = new Semaphore(0);

    //Semaforo para que cada fumador espere a poder pedir un ingrediente y que no se pisoteen entre si
    private final Semaphore semaforoOrdenPedido = new Semaphore(1);

    private final Semaphore semaforoFumadores = new Semaphore(1);

    //Se crea un contador para cada ingrediente, ademas del tiempo de produccion y el tiempo fumando que seran constantes
    int numTabaco = 0, numPapel = 0, numCerillas = 0;
    final int tiempoProduccion, tiempoFumando = 1000;
    private String ingredienteNecesitado;
    private int contadorFumadores ;

    public Estanco(int tiempoProduccion) {
        this.tiempoProduccion = tiempoProduccion;
    }

    //Este metodo produce un ingrediente aleatorio entre tabaco, papel y cerillas, mostrando el inventario actual.  
    public void producirIngrediente() {
        try {
            
            semaforoPeticiones.acquire();

            //Mejora n1. El estanquero priorizara el material mas necesitado
            System.out.println("El estanquero produce " + ingredienteNecesitado);
            Thread.sleep(tiempoProduccion);

            switch (ingredienteNecesitado) {
                case "tabaco":
                    numTabaco++;
                    break;
                case "papel":
                    numPapel++;
                    break;
                case "cerillas":
                    numCerillas++;
                    break;
            }

            System.out.println(ingredienteNecesitado + " listo");
            //System.out.println("-----------------------------------\nNumero de ingredientes en el estanco actuales:\n Tabaco: " + numTabaco + "\n Papel: " + numPapel + "\n Cerillas: " + numCerillas + "\n-----------------------------------\n");

            semaforoIngredienteListo.release();

        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    //Este metodo lo utiliza el fumador, pidiendo el ingrediente que precisa en ese momento y tomandolo cuando esté disponible
    public void pedirIngrediente(String ingrediente, String nombre) {
        try {
            semaforoOrdenPedido.acquire();
            ingredienteNecesitado = ingrediente;
            System.out.println("-----------------------------------\n" + nombre + " pide " + ingrediente + "...\n-----------------------------------");
            semaforoPeticiones.release();
            semaforoIngredienteListo.acquire();

            switch (ingrediente) {
                case "tabaco":
                    if (numTabaco > 0) {
                        numTabaco--;
                    }
                    break;
                case "papel":
                    if (numPapel > 0) {
                        numPapel--;
                    }
                    break;
                case "cerillas":
                    if (numCerillas > 0) {
                        numCerillas--;
                    }
                    break;
            }
            System.out.println("-----------------------------------\n" + nombre + " toma " + ingrediente + "...\n-----------------------------------");
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        semaforoOrdenPedido.release();
    }

    //Este metodo lo utilizan los fumadores para consumir sus ingredientes y fumar el cigarro
    public void fumar(String nombre) {
        try {
            System.out.println(nombre + " fumandose un puchito...");
            Thread.sleep(tiempoFumando);
        } catch (InterruptedException ex) {
            Logger.getLogger(Estanco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean hayFumadores() {
        return contadorFumadores > 0;
    }

    // Método para que un fumador notifique que ha terminado
    public void notificarFumadorTerminado() {
        try {
            semaforoFumadores.acquire();
            contadorFumadores--;
            if (contadorFumadores == 0) {
                System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++\n++++++++++++++++++++++++++++++++++++++++++++++\nYa no hay fumadores. El estanco cierra por hoy\n++++++++++++++++++++++++++++++++++++++++++++++\n++++++++++++++++++++++++++++++++++++++++++++++");
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            semaforoFumadores.release();
        }
    }

    public int getContadorFumadores() {
        return contadorFumadores;
    }

    public void setContadorFumadores(int contadorFumadores) {
        this.contadorFumadores = contadorFumadores;
    }
    
    

}
