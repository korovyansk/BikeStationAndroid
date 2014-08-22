package ucsoftworks.com.bikestation.data;

public class MockPersistentStorage implements PersistentStorage {

    @Override
    public boolean isRegistered() {
        return false;
    }

    @Override
    public String getUUID() {
        return "mock_uuid";
    }

    @Override
    public void setUUID(String uuid) {
    }

    @Override
    public String getBikeModel() {
        return "mock_bike_model";
    }

    @Override
    public void setBikeModel(String bikeModel) {
    }
}
