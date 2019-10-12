import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
  public static void main(String[] args) {
    Thread t = (new Thread() {
      public void run() {
        RMIRegistry.main(new String[0]);
        RMIReplicaManager.main(new String[0]);
        RMIServer.main(new String[] {"2025"});
        RMIServer.main(new String[] {"2026"});
        RMIServer.main(new String[] {"2027"});
      }
    });
    t.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } // garante que todos os serviços estão disponíveis antes de executar o
      // código do cliente

    PlacesListManagerInterface p1 = null;
    ObjectRegistryInterface p2 = null;
    PlacesListManagerInterface p3 = null;
    PlacesListInterface p4 = null;

    try {
      System.out.println("Adicionar place no ReplicaManager");
      p1 = (PlacesListManagerInterface) Naming.lookup("rmi://localhost:2024/replicamanager");
      p1.addPlace(new Place("3500", "Viseu"));
      
      System.out.println("Invocar um replica manager do object registry");
      p2 = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
      String url = p2.resolve("3500");
      
      System.out.println("Invocar uma replica do replica manager");
      p3 = (PlacesListManagerInterface) Naming.lookup(url);
      String url2 = p3.getPlaceListAddress("3500");
      
      System.out.println("Invocar o object place da replica");
      p4 = (PlacesListInterface) Naming.lookup(url2);
      System.out.println(p4.getPlace("3500").getLocality());
    } catch (MalformedURLException | RemoteException | NotBoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
}
