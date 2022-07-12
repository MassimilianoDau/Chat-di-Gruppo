package GroupChat;
import java.util.Scanner;
/**
 *
 * @author massi
 */
public class ClientThread extends Thread{              // processo client
    // ATTRIBUTI
    private Scanner input;
    
    // COSTRUTTORE
    public ClientThread(Scanner input){
        this.input = input;
    }
    
    // METODO RUN
    @Override
    public void run(){
        while(true){                                    // loop infinito per la lettura 
            try{
                String messaggio = null;
                
                while(messaggio == null){               // in attesa di un messaggio
                    messaggio = input.nextLine();
                }
                Grafica.stampaMessaggio(messaggio);     // il messaggio compare nella chat
            }catch(Exception e){
                System.exit(1);
            }
        }
    }
}