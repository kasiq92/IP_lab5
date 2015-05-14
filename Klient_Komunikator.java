/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klient_komunikator;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Katarzyna Skrzatek
 */
public class Klient_Komunikator {

    /**
     * @param args the command line arguments
     */
         public static void wypisz(String string){
        System.out.println(string);
    }
     
      public static String czytajLinie() throws IOException{
        BufferedReader x = new BufferedReader(new InputStreamReader(System.in)); //bufor
        return x.readLine();
    }  
        public static void sprLog() throws IOException{
        String login;   
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        
        
        Socket socket = new Socket("157.158.62.93", 10110); // tworzenie socketa
        BufferedReader odSerwera = new BufferedReader(new InputStreamReader(socket.getInputStream())); //wysyla
        
        DataOutputStream doSerwera = new DataOutputStream(socket.getOutputStream()); //pobiera
        
        
        String komenda=null; //zmienna przechowujaca komede napisna przez uzytkownika
        String [] tab=null;  //tablica z tekstem napisanym przez uzytkownika
        String login=null; // zmienna przechowujaca login
        String zalogowani=null; // zmienna przechowujaca liste zalogowanych
        Boolean koniec=false;
        Boolean czyZalogowany=false;
        
        while(czyZalogowany==false){
            
            
            wypisz("Podaj swój login:");
            
            komenda=czytajLinie();
        
            if(komenda!=null){
                tab=komenda.split(" ");
            }
                    
            login = tab[0];
            doSerwera.writeBytes("LISTCLIENTS\n");
            zalogowani = odSerwera.readLine();
            if(zalogowani.contains(login)){
                wypisz("Login jest już zajęty.");
                czyZalogowany=false;
            }
                
            else {
                wypisz("Zalogowano pomyślnie jako " + login);
                doSerwera.writeBytes("LOGIN"+ " " + login + "\n");
                doSerwera.writeBytes("AUTORECEIVE TRUE\n"); 
                doSerwera.writeBytes("LISTCLIENTS\n");
                zalogowani = odSerwera.readLine();
                wypisz("Lista innych użytkowników:");
                wypisz(zalogowani);
                czyZalogowany=true;
            }
        }        
                
        while(koniec==false){
        
        komenda=czytajLinie();
        
        if(komenda!=null){
            tab=komenda.split(" "); // podzielenie napisu i wrzucenie go do tablicy
        }
        
        if(tab[0].equals("LOGIN")){
                      
            login = tab[1];
            doSerwera.writeBytes("LISTCLIENTS\n");
            zalogowani = odSerwera.readLine();
            if(zalogowani.contains(login))
                wypisz("Ten login jest już zajęty. Spróbuj ponownie.");
            else {
                wypisz("Zalogowano jako " + login);
                doSerwera.writeBytes("LOGIN"+ " " + login + "\n");
                doSerwera.writeBytes("AUTORECEIVE TRUE\n");
            }
  
        }
        else if(tab[0].equals("LISTCLIENTS")){
            
                doSerwera.writeBytes("LISTCLIENTS\n");
                zalogowani = odSerwera.readLine();
                wypisz(zalogowani);
            }
        
        else if(tab[0].equals("SEND")){
            doSerwera.writeBytes("LISTCLIENTS\n");
            zalogowani = odSerwera.readLine();
            if(zalogowani.contains(tab[1])){
                doSerwera.writeBytes(komenda+"\n");
                wypisz("Wysłano");
            }
            else
                wypisz("Nie ma takiego użytkownika!");
        }
        
        else if(tab[0].equals("SENDALL")){
            doSerwera.writeBytes(komenda+"\n");
        }
        
        else if (tab[0].equals("EXIT")){
            doSerwera.writeBytes(komenda+"\n");
            koniec=true;
        }
        else 
            wypisz("Podaj poprawną komendę.");
            
        }
    }
    
}
