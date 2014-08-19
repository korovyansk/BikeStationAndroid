package ucsoftworks.com.bikestation.data;

public interface PersistentStorage {
    public boolean isRegistered();
    public String getUUID();
    public void setUUID(String uuid);
}
