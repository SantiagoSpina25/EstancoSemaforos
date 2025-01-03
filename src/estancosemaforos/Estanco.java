package estancosemaforos;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Santiago Spina
 */
public class Estanco {

    private final Semaphore semaforoTabaco = new Semaphore(0);
    private final Semaphore semaforoPapel = new Semaphore(0);
    private final Semaphore semaforoCerillas = new Semaphore(0);
    private final Semaphore semaforoEspera = new Semaphore(1);
    int numTabaco = 0, numPapel = 0, numCerillas = 0, tiempoProduccion, tiempoFumando = 1000;

    public Estanco(int tiempoProduccion) {
        this.tiempoProduccion = tiempoProduccion;
    }

    //Este metodo produce un ingrediente aleatorio entre tabaco, papel y cerillas.  
    public void producirIngrediente() {
        try {
            System.out.println("-----------------------------------\nNumero de ingredientes en el estanco actuales:\n Tabaco: " + numTabaco + "\n Papel: " + numPapel + "\n Cerillas: " + numCerillas + "\n-----------------------------------\n");

            int random = (int) (Math.random() * 3) + 1;

            switch (random) {
                case 1:
                    System.out.println("Estanquero produce tabaco");
                    numTabaco++;
                    semaforoTabaco.release();
                    break;
                case 2:
                    System.out.println("Estanquero produce papel");
                    numPapel++;
                    semaforoPapel.release();
                    break;
                case 3:
                    System.out.println("Estanquero produce cerillas");
                    numCerillas++;
                    semaforoCerillas.release();
                    break;
            }

            Thread.sleep(tiempoProduccion);

        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public void pedirIngrediente(String ingrediente, String nombre) {
        try {
            semaforoEspera.acquire();
            System.out.println(nombre + " pide " + ingrediente + "...");
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
        semaforoEspera.release();
    }

    public void fumar(String nombre) {
        try {
            System.out.println(nombre + " fumandose un puchito...");
            Thread.sleep(tiempoFumando);
        } catch (InterruptedException ex) {
            Logger.getLogger(Estanco.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(nombre + " se va a su casa");
    }

}
