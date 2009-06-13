package com.qisda.qweather.data;

import java.util.ArrayList;

public class WeatherData
{
	/*
	 * These variables name are named according to the XML.
	 */
	private CurrentConditionsData				currentCondition	= null;
	private ForecastInformationData				forecastInformation	= null;

	/*
	 * The ArrayList is used to store the parsed 4 days weather information
	 */
	private ArrayList<ForecastConditionsData>	forecastConditions	= new ArrayList<ForecastConditionsData>(
																			4);

	/*
	 * Setter and Getter
	 */
	public CurrentConditionsData getCurrentConditionsData()
	{
		return this.currentCondition;
	}

	public void setCurrentConditionsData(CurrentConditionsData currentCondition)
	{
		this.currentCondition = currentCondition;
	}

	public ForecastInformationData getForecastInformationData()
	{
		return this.forecastInformation;
	}

	public void setForecastInformationData(
			ForecastInformationData forecastInformation)
	{
		this.forecastInformation = forecastInformation;
	}

	public ArrayList<ForecastConditionsData> getForecastConditionsData()
	{
		return this.forecastConditions;
	}

	public ForecastConditionsData getLastForecastConditionsData()
	{
		int size = this.forecastConditions.size();
		return this.forecastConditions.get(size - 1);
	}

}
