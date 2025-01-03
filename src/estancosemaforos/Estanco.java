package estancosemaforos;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Santiago Spina
 */
public class Estanco {

    //Se crea un semaforo para cada ingrediente
    private final Semaphore semaforoTabaco = new Semaphore(0);
    private final Semaphore semaforoPapel = new Semaphore(0);
    private final Semaphore semaforoCerillas = new Semaphore(0);
    
    //Mejora n2. se crea un semaforo para controlar si hay o no peticiones de ingredientes.
    private final Semaphore semaforoPeticiones = new Semaphore(0);
    
    //Se crea un contador para cada ingrediente, ademas del tiempo de produccion y el tiempo fumando que seran constantes
    int numTabaco = 0, numPapel = 0, numCerillas = 0;
    final int tiempoProduccion, tiempoFumando = 1000;
    private String ingredienteNecesitado;

    public Estanco(int tiempoProduccion) {
        this.tiempoProduccion = tiempoProduccion;
    }

    //Este metodo produce un ingrediente aleatorio entre tabaco, papel y cerillas, mostrando el inventario actual.  
    public void producirIngrediente() {
        try {
            semaforoPeticiones.acquire();

            //Mejora n1. El estanquero priorizara el material mas necesitado
           System.out.println("El estanquero produce " + ingredienteNecesitado);
            switch (ingredienteNecesitado) {
                case "tabaco":
                    numTabaco++;
                    semaforoTabaco.release();
                    break;
                case "papel":
                    numPapel++;
                    semaforoPapel.release();
                    break;
                case "cerillas":
                    numCerillas++;
                    semaforoCerillas.release();
                    break;
            }
            
            Thread.sleep(tiempoProduccion);
            
            System.out.println("-----------------------------------\nNumero de ingredientes en el estanco actuales:\n Tabaco: " + numTabaco + "\n Papel: " + numPapel + "\n Cerillas: " + numCerillas + "\n-----------------------------------\n");

        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    
    //Este metodo lo utiliza el fumador, pidiendo el ingrediente que precisa en ese momento y tomandolo cuando esté disponible
    public void pedirIngrediente(String ingrediente, String nombre) {
        try {
            ingredienteNecesitado = ingrediente;
            System.out.println(nombre + " pide " + ingrediente + "...");
            semaforoPeticiones.release();
            
            switch (ingrediente) {
                case "tabaco":
                    semaforoTabaco.acquire();
                    if (numTabaco > 0) {
                        numTabaco--;
                    }
                    break;
                case "papel":
                    semaforoPapel.acquire();
                    if (numPapel > 0) {
                        numPapel--;
                    }
                    break;
                case "cerillas":
                    semaforoCerillas.acquire();
                    if (numCerillas > 0) {
                        numCerillas--;
                    }
                    break;
            }
            System.out.println(nombre + " toma " + ingrediente + "...");
        } catch (InterruptedException e) {
            System.out.println(e);
        }
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

}
