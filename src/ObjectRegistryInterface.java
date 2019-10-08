import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectRegistryInterface extends Remote {
  void addObject(String objectID, String serverAddres) throws RemoteException;

  String resolve(String objectID) throws RemoteException;
}
