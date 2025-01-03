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
        
        Fumador fumador = new Fumador(estanco,"Fulano", 1,0,0);
        //Fumador fumador2 = new Fumador(estanco,"Mengano",0,1,0);
        //Fumador fumador3 = new Fumador(estanco, "Juan", 0,0,2);
        
        estanquero.start();
        fumador.start();
        //fumador2.start();
        //fumador3.start();
    }
    
}
