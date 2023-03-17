package vn.itplus.weatherforecast;

public class ThoiTiet10day {
    String day;
    String status;
    String image;
    String maxtemp;
    String mintemp;

    public ThoiTiet10day(String day, String status, String image, String maxtemp, String mintemp) {
        this.day = day;
        this.status = status;
        this.image = image;
        this.maxtemp = maxtemp;
        this.mintemp = mintemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(String maxtemp) {
        this.maxtemp = maxtemp;
    }

    public String getMintemp() {
        return mintemp;
    }

    public void setMintemp(String mintemp) {
        this.mintemp = mintemp;
    }
}
