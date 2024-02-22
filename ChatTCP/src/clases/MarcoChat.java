package clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class MarcoChat extends JFrame {
    public static void main(String[] args) {

        String nombreUser = "";

        nombreUser = JOptionPane.showInputDialog("Escribe tu nick");
        MarcoChat chat = new MarcoChat(nombreUser);
        chat.lanzarChat();

    }

    private JPanel mainPanel;
    private JButton btnEnviar;
    private JButton btnDesconect;
    private JTextField tfChat;
    private JLabel lblChat;
    private JLabel lblUsers;
    private JTextArea taUsers;
    private JTextArea taTextoChat;
    private String nombreUser;
    private Socket sc;

    private final int PUERTO_SERVIDOR = 6001;
    private final String HOST = "localhost";

    public void lanzarChat(){
        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setTitle("CHAT DE  " + nombreUser.toUpperCase());
        conectarServidor();
    }
    public MarcoChat(String nombreUsuario) {

        this.nombreUser = nombreUsuario;

        Toolkit pantalla = Toolkit.getDefaultToolkit();

        // Coger la dimension de la pantalla 1900X1000
        Dimension pantallaSize = pantalla.getScreenSize();

        // Extraer el alto y el ancho (ejes x, y)
        int anchoPantalla = pantallaSize.width;
        int alturaPantalla = pantallaSize.height;

        //Establecer las dimensiones del marco
        this.setSize(anchoPantalla / 3, alturaPantalla / 2);
        this.setLocation(anchoPantalla / 4, alturaPantalla / 4);

        // Imagen (no la pilla)
        Image icono = pantalla.getImage("seta.gif"); // gif pesa menos
        this.setIconImage(icono);

        //Impedir que se redimensione
        this.setResizable(false);

        this.darNombreChat(nombreUser.toUpperCase());

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Enviar mensajes al panel del chat
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String user = nombreUsuario + "$-> ";
                String mensaje = user + tfChat.getText();

                //Mandar el mensaje a los otros
                if (!mensaje.isEmpty()) {

                    OutputStream outaux = null;
                    try {
                        outaux = sc.getOutputStream();
                        DataOutputStream flujo_salida= new DataOutputStream(outaux);

                        flujo_salida.writeUTF(mensaje);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }

                    //Limpiar el área de texto
                    tfChat.setText("");
                }
            }
        });

        //Desconectar usuario
//        btnDesconect.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                String mensaje = nombreUser + " abandonó el chat";
//
//                OutputStream outaux = null;
//                try {
//
//                    outaux = sc.getOutputStream();
//                    DataOutputStream flujo_salida= new DataOutputStream(outaux);
//
//                    flujo_salida.writeUTF(mensaje);
//
//                } catch (IOException ex) {
//                    System.out.println(ex);
//                }
//
//            }
//        });

    }
    private void conectarServidor() {

        try {
            sc = new Socket(HOST, PUERTO_SERVIDOR);

            OutputStream outaux = sc.getOutputStream();
            DataOutputStream flujo_salida= new DataOutputStream(outaux);

            flujo_salida.writeUTF(nombreUser);

            ClienteHilo ch = new ClienteHilo();
            Thread hiloCli = new Thread(ch);
            hiloCli.start();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private class ClienteHilo implements Runnable {

        @Override
        public void run() {

            while(true) {

                InputStream inaux = null;
                try {
                    inaux = sc.getInputStream();
                    DataInputStream flujo_entrada = new DataInputStream(inaux);

                    String texto = flujo_entrada.readUTF();

                    if (texto.startsWith("Usuarios conectados:")) {
                        //actualizarListaConectados(texto);
                        taUsers.setText(texto);
                    } else
                        taTextoChat.append(texto + "\n");

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    }
    private String darNombreChat(String nombre) {

        this.lblChat.setText(nombre);

        return this.lblChat.getText();
    }

}
