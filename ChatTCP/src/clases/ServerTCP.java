package clases;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerTCP {

    public static void main(String[] args) {
        ServerTCP server = new ServerTCP();
        server.lanzarServer();
    }

    private final int PUERTO_SERVER = 6001;
    private List<ClienteTCP> listaClientes = new ArrayList<>();

    public  void lanzarServer(){

        try {
            ServerSocket scServer = new ServerSocket(PUERTO_SERVER);
            while (true) {
                Socket scCliente = scServer.accept();
                System.out.println("Nuevo cliente conectado!");

                ClienteTCP c = new ClienteTCP(scCliente, this);
                listaClientes.add(c);
                Thread hiloCliente = new Thread (c);
                hiloCliente.start();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void emitirMensaje(String mensaje) {
        for (ClienteTCP c : listaClientes) {
            c.mandarMensaje(mensaje);
        }
    }
    public void eliminarClienteLista(ClienteTCP cl) {
        listaClientes.remove(cl);
    }

}
