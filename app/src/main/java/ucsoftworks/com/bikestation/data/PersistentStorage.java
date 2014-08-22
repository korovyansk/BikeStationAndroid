package ucsoftworks.com.bikestation.data;

public interface PersistentStorage {
    public boolean isRegistered();
    public String getUUID();
    public void setUUID(String uuid);
    public String getBikeModel();
    public void setBikeModel(String bikeModel);
}
