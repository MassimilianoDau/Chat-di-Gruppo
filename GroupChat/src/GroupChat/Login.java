package GroupChat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author massi
 */
public class Login {
    private static String nome;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        creaLogin();
    }
    
    public static void creaLogin(){
        // =========================================    VALORI    ====================================
        int larghezzaFrame              =   450;
        int altezzaFrame                =   300;
        int frameX                      =   550;
        int frameY                      =   250;

        int larghezzaLabelUtente        =   larghezzaFrame;
        int altezzaLabelUtente          =   20;
        int labelUtenteX                =   (larghezzaFrame/2) - (larghezzaLabelUtente/2) + 50 ;
        int labelUtenteY                =   30;

        int larghezzaTextUtente         =   150;
        int altezzaTextUtente           =   30;
        int textUtenteX                 =   (larghezzaFrame/2) - (larghezzaTextUtente/2) - 10;
        int textUtenteY                 =   60;

        int larghezzaBtConnetti         =   100;
        int altezzaBtConnetti           =   30;
        int btConnettiX                 =   (larghezzaFrame/2) - (larghezzaBtConnetti/2) - 10;
        int btConnettiY                 =   150;

        // =========================================   FINESTRA    ===================================
        JFrame finestra= new JFrame();
        finestra.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        finestra.setPreferredSize(new Dimension(larghezzaFrame,altezzaFrame));
        finestra.setLocation(frameX,frameY);

        // =========================================   PANNELLO    ===================================
        JPanel pannello = new JPanel();
        pannello.setLayout(null);
        finestra.add(pannello);

        // =========================================   NOME UTENTE  ==================================
        JLabel labelNomeUtente = new JLabel("Benvenuto, per accedere alla chat, inserisci un nome utente:");
        labelNomeUtente.setSize(larghezzaLabelUtente,altezzaLabelUtente);
        labelNomeUtente.setLocation(labelUtenteX,labelUtenteY);
        pannello.add(labelNomeUtente);

        JTextField nomeUtente = new JTextField();
        nomeUtente.setSize(larghezzaTextUtente,altezzaTextUtente);
        nomeUtente.setLocation(textUtenteX,textUtenteY);
        pannello.add(nomeUtente);
        
        // =========================================    BOTTONE CONNETTI    ===========================
        JButton connetti = new JButton("Connetti");
        connetti.setSize(larghezzaBtConnetti,altezzaBtConnetti);
        connetti.setLocation(btConnettiX,btConnettiY);
        pannello.add(connetti);

        ActionListener connessione=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nomeUtente.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Devi inserire un nome per connetterti!");
                }
                else if(nomeUtente.getText().startsWith("\\")){
                    JOptionPane.showMessageDialog(null,"Carattere iniziale non valido, reinserire un nome utente.");
                }
                else{
                    nome = nomeUtente.getText();
                    finestra.setVisible(false);
                    
                    Grafica grafica=new Grafica(nome);
                    grafica.creaGraficaClient(); 
                }
            }
        };
        connetti.addActionListener(connessione);
        // =========================================    FINESTRA PACK   ==============================
        finestra.pack();
        finestra.setResizable(false);
        finestra.setVisible(true);
        
    }
}
