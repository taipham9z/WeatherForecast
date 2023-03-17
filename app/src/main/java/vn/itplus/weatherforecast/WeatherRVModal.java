package vn.itplus.weatherforecast;

public class WeatherRVModal {
    private String time;
    private String temperature;
    private String icon;
    private String windSpeed;
    private String windSpeedCurrent;
    private String cloud;
    private String humidity;

    public String getWindSpeedCurrent() {
        return windSpeedCurrent;
    }

    public void setWindSpeedCurrent(String windSpeedCurrent) {
        this.windSpeedCurrent = windSpeedCurrent;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public WeatherRVModal() {
    }

    public WeatherRVModal(String time, String temperature, String icon, String windSpeed, String windSpeedCurrent, String cloud, String humidity) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.windSpeedCurrent = windSpeedCurrent;
        this.cloud = cloud;
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
