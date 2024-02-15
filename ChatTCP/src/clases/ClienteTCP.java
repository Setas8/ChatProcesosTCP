package clases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTCP implements Runnable {

    private Socket scCliente;
    private ServerTCP server;
    private PrintWriter out;

    public ClienteTCP(Socket scCliente, ServerTCP server){
        this.scCliente = scCliente;
        this.server = server;

        try {
            out = new PrintWriter(scCliente.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void mandarMensaje(String mensaje){
        out.println(mensaje);
    }
    @Override
    public void run() {

        try(BufferedReader in = new BufferedReader(new InputStreamReader(scCliente.getInputStream()))){
            String nombreUser = in.readLine();
            server.emitirMensaje(nombreUser + " se unió al chat");

            String texto;
            while ((texto = in.readLine()) != null){

                server.emitirMensaje(nombreUser + "$-> "+ texto);
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
