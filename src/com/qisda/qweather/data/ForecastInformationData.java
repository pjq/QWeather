package com.qisda.qweather.data;

public class ForecastInformationData
{
	/*
	 * These variables name are named according to the XML.
	 */
	private String	city;
	private String	postal_code;
	private String	latitude_e6;
	private String	longitude_e6;
	private String	forecast_date;
	private String	current_date_time;
	private String	unit_system;

	/*
	 * Setter and Getter
	 */
	public String getCity()
	{
		return this.city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getPostalCode()
	{
		return this.postal_code;
	}

	public void setPostalCode(String postal_code)
	{
		this.postal_code = postal_code;
	}

	public String getLatitude()
	{
		return this.latitude_e6;
	}

	public void setLatitude(String latitude_e6)
	{
		this.latitude_e6 = latitude_e6;
	}

	public String getLongitude()
	{
		return this.longitude_e6;
	}

	public void setLongitude(String longitude_e6)
	{
		this.longitude_e6 = longitude_e6;
	}

	public String getForecastDate()
	{
		return this.forecast_date;
	}

	public void setForecastDate(String forecast_date)
	{
		this.forecast_date = forecast_date;
	}

	public String getCurrentDateTime()
	{
		return this.current_date_time;
	}

	public void setCurrentDateTime(String current_date_time)
	{
		this.current_date_time = current_date_time;
	}

	public String getUnitSystem()
	{
		return this.unit_system;
	}

	public void setUnitSystem(String unit_system)
	{
		this.unit_system = unit_system;
	}

}
