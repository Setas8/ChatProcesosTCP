package clases;

import java.io.*;
import java.net.Socket;

public class ManejadorClientes implements Runnable {
    private Socket scCliente;
    private Server_TCP server;
    private String nombre;

    public ManejadorClientes(Socket scCliente, Server_TCP server){
        this.scCliente = scCliente;
        this.server = server;
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
    }
    @Override
    public void run() {
        while (true) {
            System.out.println("Entro en el hilo manejador");
            InputStream inaux = null;
            try {
                inaux = scCliente.getInputStream();
                DataInputStream flujo_entrada = new DataInputStream(inaux);

                String texto = flujo_entrada.readUTF();
                server.emitirMensaje(texto);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
