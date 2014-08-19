package ucsoftworks.com.bikestation.events;

public class StartRentEvent {

    private final String username;

    public StartRentEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
