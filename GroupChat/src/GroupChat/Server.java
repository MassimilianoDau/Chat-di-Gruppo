package GroupChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author massi
 */
public class Server implements Runnable{
    // =========================================    ATTRIBUTI   ===============================================
    private ServerSocket server;                                // server
    Thread serverStart;                                         // thread che starterà il runnable
    private ArrayList <ServerThread> listaClientConnessi;       // lista dei client
    private ServerThread client;                                // singolo client
    private boolean continua;                                   // boolean per il run
    private int porta =   6789;                                 // porta del server
    
    String nomeFile="log.txt";                                  // sarà il nome del file
    File file = new File(nomeFile);                             // istanziazione di un file che salverà i log                       
    FileWriter fw= null;
    
    // =========================================    COSTRUTTORE   ===============================================
    public Server(){
        listaClientConnessi = new ArrayList();          // istanziazione arraylist di client
        serverStart = new Thread(this);                 // creo il thread del server, e gli passo la classe, che è un runnable
        serverStart.start();                            // starto il server
        System.out.println("Server in esecuzione..");
    }
    
    // =========================================    METODO RUN   ===============================================
    @Override
    public void run() {
        continua=true;
        try{
            server = new ServerSocket(porta);       //socket del server che rimarrà in attesa
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Impossibile instanziare il server");
            continua = false;
        }
        
        if(continua){
            try{
                while(true){
                    Socket clientNuovo = null;                          // creazione socket vuoto temporaneo
                    clientNuovo = server.accept();                      // il server aspetta un client per connettersi, e prenderà le informazioni dal client che si connette                     
                    client = new ServerThread(this,clientNuovo);        // aggiungiamo alla lista utenti il thread di lettura appena creato con il nuovo socket "tempo"
                    listaClientConnessi.add(client);                    // aggiungo il client alla lista dei client
                    client.setIndirizzo(clientNuovo.getInetAddress().toString());  // prendo l'ip del client
                    client.setPorta(clientNuovo.getPort());                        // prendo la porta del client
                    client.start();                                                // starto il client
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Impossibile instanziare il server");
            }
        }
    }
    
    // =========================================    METODO BROADCAST   ===============================================
    //metodo chiamato quando il server riceve un mess dal client
    public void broadcast(String mex) {
        salvaLog(mex);                                              // salvo il messaggio nel file di log
        for (ServerThread client : listaClientConnessi){
              client.sendMessageToClient(mex);                      // inviamo il messaggio ricevuto
        }
    }
    // =========================================    METODO CHE SALVA LA CHAT IN UN FILE TXT   ===============================================
    public void salvaLog(String messaggio){
        try{
            fw= new FileWriter(nomeFile,true);          // il true è l'append, cosi non sovrascrive ogni volta il file
            fw.write(messaggio);
            fw.append("\n");
            fw.close();
        }catch (IOException e){}
    }
    public void resetFile(){
        try{
            fw= new FileWriter(nomeFile);               // senza true, infatti deve sovrascrivere
            fw.write("");
            fw.close();
        }catch (IOException e){
            System.out.println("file di testo non trovato");
        }
    } 
    // =========================================    METODO CHE MOSTRA I POSSIBILI COMANDI DEL SERVER   ===============================================
    public void comandiServer(){
        System.out.println("Comandi server:\n"
                + "----------------------------------------\n"
                + "[chiudi]     -> spegni server\n"
                + "[?]          -> informazioni sui comandi\n"
                + "[delete_log] -> cancella i log del server\n" 
                + "----------------------------------------\n"
                + "Digita un comando:");
    }
    // =========================================    METODO SPEGNI SERVER   ===============================================
    public void spegni(){
        System.out.println("Server offline.");
        System.exit(0);
    }
    
    // =========================================    MAIN   ===============================================
    public static void main(String[] args) {
        Server gestore =new Server();               // creo oggetto server che tramite il costruttore avvia il metodo run (thread)
        gestore.comandiServer();                    // stampo in console i comandi disponibili
        
        Scanner tastiera = new Scanner(System.in);  // utilizzo per poter scrivere in console
        
        String comando=tastiera.nextLine();         // la console aspetterà una stringa
        
        boolean comm=false;                         // boolean per cui ciclerà il while
        while(!comm){
            switch (comando) {
                case "chiudi":                      
                case "Chiudi":                      // raggruppo tutti i casi di scrittura di "chiudi"
                case "CHIUDI":                      
                    comm=true;                      // se scrivo chiudi mette a true il boolean, e smetterà di ciclare
                    gestore.spegni();               // spegne il server
                    break;
                case "?":
                    gestore.comandiServer();        // richiedo i comandi disponibili
                    comando=tastiera.nextLine();
                    break;
                case "delete_log":
                    gestore.resetFile();            // resetto il file dei log
                    System.out.println("File dei log resettato.\nDigita un comando:");
                    comando=tastiera.nextLine();
                    break;
                default:                            // tutti gli altri casi
                    System.err.println("Errore, inserisci un comando valido.");
                    comm=false;
                    comando=tastiera.nextLine();    // richiedo un altra stringa
                    break;
            }
        } 
    }
}
