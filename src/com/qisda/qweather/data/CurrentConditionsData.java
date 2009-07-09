
package com.qisda.qweather.data;

public class CurrentConditionsData {
    /*
     * These variables name are named according to the XML.
     */
    private String condition;

    private String temp_f;

    private String temp_c;

    private String humidity;

    private String icon;

    private String wind_condition;

    /*
     * Setter and Getter
     */
    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTempF() {
        return this.temp_f;
    }

    public void setTempF(String tempF) {
        this.temp_f = tempF;
    }

    public String getTempC() {
        return this.temp_c;
    }

    public void setTempC(String tempC) {
        this.temp_c = tempC;
    }

    public String getHumidity() {
        return this.humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindCondition() {
        return this.wind_condition;
    }

    public void setWindCondition(String wind_condition) {
        this.wind_condition = wind_condition;
    }

}
