package ucsoftworks.com.bikestation.model;

public class Station {

    public int id;
    public String name;

    public Station(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
