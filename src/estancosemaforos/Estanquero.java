package estancosemaforos;

/**
 *
 * @author Santiago Spina
 */
public class Estanquero extends Thread {

    private Estanco estanco;

    public Estanquero(Estanco estanco) {
        this.estanco = estanco;
    }

    @Override
    public void run() {
        while (true) {
            estanco.producirIngrediente();
        }
    }

}
