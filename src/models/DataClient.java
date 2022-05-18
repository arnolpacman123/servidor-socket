package models;

import java.io.Serializable;

public class DataClient implements Serializable {
    String id;
    float temperature;
    float humidity;

    public DataClient(String id, float temperature, float humidity) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
