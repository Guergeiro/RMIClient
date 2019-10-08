import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RMIClient {
  public static void main(String[] args) {
    PlacesListInterface l1 = null;

    try {
      System.out.println("Localizar servidor de Objetos...");
      l1 = (PlacesListInterface) Naming.lookup("rmi://localhost:2022/placelist");

      Place p = new Place("3510", "Viseu");
      System.out.println("Invocar addPlace() ...");
      l1.addPlace(p);

      System.out.println("Obter o endereço do servidor no Registry() ...");
      ObjectRegistryInterface l2;
      PlacesListInterface l3;
      try {
        l2 = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
        String addr = l2.resolve("3510");

        System.out.println("Invocar getPlace() no servidor de objetos...");
        l3 = (PlacesListInterface) Naming.lookup(addr);
        System.out.println(l3.getPlace("3510").getLocality());
      } catch (MalformedURLException | NotBoundException e) {
        System.out.println(e.getCause());
      }

    } catch (Exception e) {
      System.out.println("Problemas de Comunicação\n" + e.getMessage());
    }
  }
}
