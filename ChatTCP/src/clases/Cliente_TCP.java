package clases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Cliente_TCP implements Runnable {

    private Socket scCliente;
    private Server_TCP server;
    private PrintWriter out;
    private String nombre;

    public Cliente_TCP(Socket scCliente, Server_TCP server){
        this.scCliente = scCliente;
        this.server = server;

        try {
            out = new PrintWriter(scCliente.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getNombre() {
        return nombre;
    }
    public void mandarMensaje(String mensaje){
        out.println(mensaje);
    }
    public void enviarClientesConectados(List<String> listaClientes) {

        String lista = "Usuarios conectados:\n";
        for (String usuario : listaClientes) {
            lista = lista + usuario + "\n";
        }
        mandarMensaje(lista);
    }

    @Override
    public void run() {

        try(BufferedReader in = new BufferedReader(new InputStreamReader(scCliente.getInputStream()))){
            String nombreUser = in.readLine();
            server.emitirMensaje(nombreUser + " se unió al chat");

            String texto;
            while ((texto = in.readLine()) != null){
                server.emitirMensaje(texto);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                scCliente.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.eliminarClienteLista(this);
            server.emitirMensaje("Usuario desconectado");
        }
    }
}
