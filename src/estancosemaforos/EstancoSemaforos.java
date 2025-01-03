package estancosemaforos;

/**
 *
 * @author Santiago Spina
 */
public class EstancoSemaforos {

    
    public static void main(String[] args) {
        
        int tiempoProduccion = 1000;
        Estanco estanco = new Estanco(tiempoProduccion);
        Estanquero estanquero = new Estanquero(estanco);
        
        Fumador fumador = new Fumador(estanco,"Wachin", 1,0,0);
        Fumador fumador2 = new Fumador(estanco,"pibardo",0,0,1);
        
        estanquero.start();
        fumador.start();
        fumador2.start();
    }
    
}
