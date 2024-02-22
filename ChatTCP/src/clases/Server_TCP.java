package clases;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server_TCP {

    public static void main(String[] args) {
        Server_TCP server = new Server_TCP();
        server.lanzarServer();
    }

    private final int PUERTO_SERVER = 6001;
    private List<ManejadorClientes> listaClientes = new ArrayList<>();


    public void lanzarServer(){

        try {
            ServerSocket scServer = new ServerSocket(PUERTO_SERVER);
            System.out.println("Servidor en marcha por el puerto " + PUERTO_SERVER);
            while (true) {
                Socket scCliente = scServer.accept();
                System.out.println("Nuevo cliente conectado!");

                InputStream inaux = scCliente.getInputStream();
                DataInputStream flujo_entrada = new	 DataInputStream(inaux);

                String nombre = flujo_entrada.readUTF();

                //System.out.println("Prueba para ver si sale el nombre" + nombre);

                ManejadorClientes c = new ManejadorClientes(scCliente, this);
                c.setNombre(nombre);
                listaClientes.add(c);
                Thread hiloCliente = new Thread (c);
                hiloCliente.start();

                emitirListaConectados();
                //flujo_entrada.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void emitirMensaje(String mensaje) {
        for (ManejadorClientes c : listaClientes) {
            c.mandarMensaje(mensaje);
        }
    }
    public void emitirListaConectados() {

        List<String> lista = new ArrayList<>();

        for (ManejadorClientes c : listaClientes) {
            lista.add(c.getNombre());
        }

        String mensaje = "Usuarios conectados:\n " + String.join("\n", lista);
        for (ManejadorClientes c : listaClientes) {
            c.mandarMensaje(mensaje);
        }
    }

    public void eliminarClienteLista(ManejadorClientes cl) {
        listaClientes.remove(cl);
        emitirListaConectados();
    }

}
