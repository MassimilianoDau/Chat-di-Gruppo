/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GroupChat;

import java.io.*;
import java.net.Socket;

/**
 *
 * @author massi
 */
public class ServerThread extends Thread{        //figlio della classe server
    // =================================================    ATTRIBUTI   =============================================
    private Socket client           =   null;       // client
    private BufferedReader input    =   null;       // input
    private PrintWriter output      =   null;       // output
    private Server server;                          // server
    
    private String nome;                            // nome del client
    private String indirizzo;                       // indirizzo del client
    private int porta;                              // porta del client
    private String socketInfo;                      // info del socket
    
    // =================================================    COSTRUTTORE     ==========================================
    public ServerThread(Server server, Socket client){
        this.client=client;                                         // gli passo il socket temporaneo creato nel server
        this.server=server;                                         // gli passo il server
        try{
            input   =   new BufferedReader(new InputStreamReader(client.getInputStream()));     // apro il flusso di input, quello che arriva
            output  =   new PrintWriter(this.client.getOutputStream(),true);                    // apro il flusso di output, quello che invia
        }catch(IOException ex){
            ex.getMessage();
        }
    }
    
    // =================================================    METODO RUN   =============================================
    @Override
    public void run(){
        while(true){                                        // ciclo infinito che sta in attesa dei messaggi in arrivo
            try{
                String mex = null;                          
                while((mex=input.readLine())==null){}       // finchè non è stato mandato nessun messaggio, sta in attesa
                socketInfo ="["+this.getIndirizzo()+" : "+this.getPorta()+"] ";     // mi prendo ip + porta in modo dinamico
                server.broadcast(socketInfo+mex);                                   // invio al server l'intero messaggio
            }catch(IOException ex) {
            }
        }
    }
    
    // =================================================    METODO BROADCAST   =============================================
    // metodo utilizzato dal server per mandare un messaggio a un singolo client
    public void sendMessageToClient(String messaggio){
        try{
            String mex=messaggio.substring(socketInfo.length());             // toglie dal messaggio le informazioni del client
            
            if(mex.startsWith("\\con:")){                       // se il messaggio inizia cosi, allora
                String nuovoUser= mex.substring(5);                  // mi prendo il nome di chi si è connesso, saltando i caratteri del comando
                output.println(nuovoUser+" si è connesso.");         // manda il messaggio in chat dicendo che persona X si è connesso alla chat
            }
            else{
                output.println(mex);                        // se il messaggio è normale, quindi senza comando, lo spedisce normalmente
            }
        }catch(Exception e) {
            System.out.println("Errore nella spedizione del messaggio");
        }
    }
    
    // =================================================    GET E SET    =============================================
    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome=nome;
    }
    
    public String getIndirizzo(){
        return indirizzo;
    }
    public void setIndirizzo(String indirizzo){
        this.indirizzo=indirizzo;
    }
    
    public int getPorta(){
        return porta;
    }
    public void setPorta(int porta){
        this.porta=porta;
    }
}
