package clases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClienteHilo implements Runnable{

    MarcoChat marco;
    private Socket sc;

    public ClienteHilo(MarcoChat marco, Socket sc){
        this.marco = marco;
        this.sc = sc;
    }

    @Override
    public void run() {
        //this.taUsers.append(nombreUser);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            String texto = "";
            while ((texto = in.readLine()) != null){
                marco.getTaTextoChat().append(texto + "\n");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
