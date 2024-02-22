package clases;

import java.io.*;
import java.net.Socket;

public class ManejadorClientes implements Runnable {

    private Socket scCliente;
    private Server_TCP server;
    private PrintWriter out;
    private String nombre;

    public ManejadorClientes(Socket scCliente, Server_TCP server){
        this.scCliente = scCliente;
        this.server = server;

//        try {
//            out = new PrintWriter(scCliente.getOutputStream(), true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void mandarMensaje(String mensaje){

        OutputStream outaux = null;
        try {
            outaux = scCliente.getOutputStream();
            DataOutputStream flujo_salida= new DataOutputStream(outaux);
            System.out.println("Estoy mandando mensaje desde manejador " + mensaje);
            flujo_salida.writeUTF(mensaje);
            //flujo_salida.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //out.println(mensaje);
    }


    @Override
    public void run() {
        System.out.println("Entro en el hilo manejador");
        InputStream inaux = null;
        try {
            inaux = scCliente.getInputStream();
            DataInputStream flujo_entrada = new	 DataInputStream(inaux);

//            String nombreUser = flujo_entrada.readUTF();
//            System.out.println(nombreUser);
//
//            server.emitirMensaje(nombreUser + " se unió al chat");

            String texto = flujo_entrada.readUTF();
            server.emitirMensaje(texto);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        finally{
//            try {
//                scCliente.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        server.eliminarClienteLista(this);
//        server.emitirMensaje("Usuario desconectado");

//        try(BufferedReader in = new BufferedReader(new InputStreamReader(scCliente.getInputStream()))){
//            String nombreUser = in.readLine();
//            server.emitirMensaje(nombreUser + " se unió al chat");
//
//            String texto;
//            while ((texto = in.readLine()) != null){
//                server.emitirMensaje(texto);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally{
//            try {
//                scCliente.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            server.eliminarClienteLista(this);
//            server.emitirMensaje("Usuario desconectado");
//        }
    }
}
