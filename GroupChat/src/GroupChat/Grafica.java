package GroupChat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.*;
/**
 *
 * @author massi
 */
public class Grafica {
    // ======================================   ATTRIBUTI   ==========================================
    private String nome="";                                 // nome client (nome utente)
    private String indirizzoIP="127.0.0.1";                 // ip server
    private int porta=6789;                                 // porta server
    
    private static JTextArea areaMessaggi=new JTextArea();  // box messaggi
    private static JTextArea areaUtenti=new JTextArea();    // box utenti connessi
    
    private static JTextArea testoMessaggio;                // box del messaggio che si scrive
    private static Socket client;                           // socket utilizzato per connettersi al server
    
    private static Scanner input;                           // input  -> è l'output del server           
    private static PrintWriter output;                      // output -> quello che il server riceverà
    private static ClientThread lettore;
    
    // =====================================    COSTRUTTORE ==========================================
    public Grafica(String nome){                            
        this.nome=nome;                                     // il nome del client sarà passato dalla classe Login
    }
    // =====================================    METODO STAMPA MESSAGGIO ==============================   
    public static void stampaMessaggio(String mess){
        areaMessaggi.append(mess +"\n");                    // appendo il messaggio all'areaMessaggi
    }
    public static void inviaMessaggio(String mess){
        output.println(mess);                               // invio l'output al thread
    }
    
    public void creaGraficaClient(){ 
        try {
            client  = new Socket(indirizzoIP, porta);                   // richiesta di collegamento con il server, connessione con il server
            input   = new Scanner(client.getInputStream());             // input  -> quello che ricevo
            output  = new PrintWriter(client.getOutputStream(),true);   // output -> quello che mando, il true sta per l'autoflush, flush -> scrittura nel file
            lettore = new ClientThread(input);                          // creo un thread a cui
            lettore.start();                                            // mando il thread in run
            
            String connessione="\\con:";                                // comando che utilizzo per dare un messaggio di connessione alla chat
            inviaMessaggio(connessione+nome);                           // invio di un messaggio iniziale che dichiarerà la connessione al server
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JFrame(),"Non è stato possibile connettersi al server..\nChiusura client.");
            System.exit(0);
        }
        
        
        // =================================    FINESTRA    ==================================================
        JFrame finestra = new JFrame();
        finestra.setTitle("Connesso come: "+nome);                              // in questo modo differenzio ogni client
        finestra.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        finestra.setPreferredSize(new Dimension(500,700));                      // setto le dimensioni del frame
        finestra.setLocation(500, 50);                                          // il frame apparirà a queste coordinate dello schermo
        
        // ================================= LAYOUT ==========================================
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#0e0e10"));       // sfondo del pannello
        SpringLayout layout = new SpringLayout();
        // ================================== TEXT AREA MESSAGGI ==========================================
        
        areaMessaggi.setEditable(false);
        areaMessaggi.setBackground(Color.decode("#18181b"));
        areaMessaggi.setForeground(Color.decode("#efeff1"));
        areaMessaggi.setMargin(new Insets(2,5,0,5));                    // margini che avrà l'area
        areaMessaggi.setLineWrap(true);                                 // arrivato al margine, il testo andrà a capo automaticamente
        JScrollPane scrollAreaMessaggi = new JScrollPane(areaMessaggi);
        panel.add(scrollAreaMessaggi);
        
        Border bordo = BorderFactory.createLineBorder(Color.decode("#bf94ff"), 4);  // colore, spessore bordo
        scrollAreaMessaggi.setBorder(bordo);
        
        // ================================== TESTO DA INVIARE ==========================================
        testoMessaggio=new JTextArea(4,0);                          // setto 3 righe, le colonne non contano in quanto la larghezza viene settata nel layout
        testoMessaggio.setBackground(Color.decode("#4c4c4f"));
        testoMessaggio.setForeground(Color.BLACK);
        
        testoMessaggio.setMargin(new Insets(2,5,0,5));              // setto i margini, 5 alla sinistra
        testoMessaggio.setBackground(Color.decode("#A9A9A9"));      // setto lo sfondo
        testoMessaggio.setLineWrap(true);                           // va a capo in automatico
        JScrollPane scrollTesto = new JScrollPane(testoMessaggio);
        panel.add(scrollTesto);
        
        KeyListener tastoInvio = new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){                                                // se il tasto premuto è il tasto invio
                    if(! ((testoMessaggio.getText().replaceAll("\n", "")).equals(""))) {
                        String testo                = testoMessaggio.getText().replaceAll("\n", "");    // tolgo dal testo il codice del tasto invio
                        String messaggioCompleto    = nome + ": "+ testo;                               // output in chat -> nome: testo

                        inviaMessaggio(messaggioCompleto);                                              // invio il messaggio
                    }
                    testoMessaggio.setText("");                                                         // resetto il box del testo
                }
            }
        };
        testoMessaggio.addKeyListener(tastoInvio);

        // =================================================    BOTTONE INVIA   ==========================================
        JButton bottoneInvia=new JButton("Invia");
        bottoneInvia.setBackground(Color.decode("#9147ff"));
        bottoneInvia.setForeground(Color.decode("#FFFFFF"));
        panel.add(bottoneInvia);
        
        ActionListener inviaMessaggio;
        inviaMessaggio= new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(! (testoMessaggio.getText().equals(""))) {
                    String testo                = testoMessaggio.getText();
                    String messaggioCompleto    = nome + ": "+ testo;           // output in chat -> nome: testo

                    inviaMessaggio(messaggioCompleto);                          // invio il messaggio
                    testoMessaggio.setText("");                                 // resetto il box del testo
                }
            }
        };
        bottoneInvia.addActionListener(inviaMessaggio);

        //=================================================== LISTA UTENTI CONNESSI ===================================================
        
        areaUtenti.setBackground(Color.LIGHT_GRAY);
        panel.add(areaUtenti);

        //===================================================      SET LAYOUT      ==============================================
        panel.setLayout(layout);
        //distanzio di tot margine la scroll area dalla parte ovest del pannello
        int margine= 20;
        
        layout.putConstraint(SpringLayout.NORTH, scrollAreaMessaggi, margine, SpringLayout.NORTH, panel);               
        layout.putConstraint(SpringLayout.SOUTH, scrollAreaMessaggi, -margine, SpringLayout.NORTH, scrollTesto);
        layout.putConstraint(SpringLayout.WEST, scrollAreaMessaggi, margine, SpringLayout.WEST, panel);                 
        layout.putConstraint(SpringLayout.EAST, scrollAreaMessaggi, -margine, SpringLayout.EAST, panel);

        layout.putConstraint(SpringLayout.SOUTH, scrollTesto, -margine, SpringLayout.SOUTH, panel);
        layout.putConstraint(SpringLayout.WEST, scrollTesto, margine, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, scrollTesto, -margine, SpringLayout.WEST, bottoneInvia);
        
        layout.putConstraint(SpringLayout.NORTH, bottoneInvia, margine, SpringLayout.SOUTH, scrollAreaMessaggi);
        layout.putConstraint(SpringLayout.EAST, bottoneInvia, -margine, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, bottoneInvia, -margine, SpringLayout.SOUTH, panel);

        //=======================================================================================================================
        finestra.add(panel);
        finestra.pack();
        finestra.setVisible(true);
        
    }
}
