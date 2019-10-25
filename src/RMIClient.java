import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIClient {
  public static void main(String[] args) {
    PlacesListManagerInterface plm;
    ObjectRegistryInterface ori;


    try {
      System.out.println("SE DER ERROS DE CONCORRÊNCIA, É NECESSÁRIO TENTAR OUTRA VEZ!!!");
      System.out.println("Localizar ReplicaManager...");
      plm = (PlacesListManagerInterface) Naming.lookup("rmi://localhost:2024/replicamanager");
      Thread.sleep(1000);
      Place p1 = new Place("3510", "Viseu");
      System.out.println("Invocar addPlace() no ReplicaManager para 3510...");
      plm.addPlace(p1);

      Place p2 = new Place("1000", "Lisboa");
      System.out.println("Invocar addPlace() no ReplicaManager para 1000...");
      plm.addPlace(p2);

      Place p3 = new Place("4000", "Lisboa");
      System.out.println("Invocar addPlace() no ReplicaManager para 4000...");
      plm.addPlace(p3);

      System.out
          .println("Obter o endereço do ReplicaManager no Registry() para o ObjectID 1000 ...");
      ori = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
      String addrRM = ori.resolve("1000");

      System.out.println("Destruir uma das réplicas ...");
      LocateRegistry.getRegistry(2029).unbind("placelist");
      Thread.sleep(2000);


      System.out.println(
          "Pedir ao ReplicaManager para devolver um PlaceManager para o ObjectID 1000 ...");
      plm = (PlacesListManagerInterface) Naming.lookup(addrRM);
      boolean pl1done = false;
      boolean pl2done = false;
      boolean pl3done = false;
      String plAddress = null;
      for (int i = 0; i < 10; i++) {
        plAddress = plm.getPlaceListAddress("1000");
        invokeGetPlacePlaceManager(plAddress);
        System.out.println("\tPara o Endereço " + plAddress);
        if (plAddress.equals("rmi://localhost:2028/placelist"))
          pl1done = true;
        else if (plAddress.equals("rmi://localhost:2029/placelist"))
          pl2done = true;
        else if (plAddress.equals("rmi://localhost:2030/placelist"))
          pl3done = true;
      }

      if (!(pl1done == pl2done == pl3done == true))
        System.out.println("Não está a devolver um PlaceManager aleatório...");

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Erro\n" + e.getMessage());
    }
  }

  private static void invokeGetPlacePlaceManager(String plAddress)
      throws NotBoundException, MalformedURLException, RemoteException {
    PlacesListInterface pli;
    System.out.println("Invocar getPlace() no PlaceManager");
    pli = (PlacesListInterface) Naming.lookup(plAddress);
    String locality = pli.getPlace("1000").getLocality();

    System.out.println("\tDevolveu " + locality);
  }

}
