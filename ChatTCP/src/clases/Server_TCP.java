package clases;

import java.io.IOException;
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
    private List<Cliente_TCP> listaClientes = new ArrayList<>();

    public void lanzarServer(){

        try {
            ServerSocket scServer = new ServerSocket(PUERTO_SERVER);
            while (true) {
                Socket scCliente = scServer.accept();
                System.out.println("Nuevo cliente conectado!");

                Cliente_TCP c = new Cliente_TCP(scCliente, this);
                listaClientes.add(c);
                Thread hiloCliente = new Thread (c);
                hiloCliente.start();

                emitirListaConectados();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void emitirMensaje(String mensaje) {
        for (Cliente_TCP c : listaClientes) {
            c.mandarMensaje(mensaje);
        }
    }
    public void emitirListaConectados() {
        List<String> lista = new ArrayList<>();
        for (Cliente_TCP c : listaClientes) {
            lista.add(c.getNombre());
        }

        for (Cliente_TCP c : listaClientes) {
            c.enviarClientesConectados(lista);
        }
    }

    public void eliminarClienteLista(Cliente_TCP cl) {
        listaClientes.remove(cl);
        emitirListaConectados();
    }

}
