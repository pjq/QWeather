package com.qisda.qweather.handle;

import com.qisda.qweather.*;
import com.qisda.qweather.data.CurrentConditionsData;
import com.qisda.qweather.data.ForecastConditionsData;
import com.qisda.qweather.data.ForecastInformationData;
import com.qisda.qweather.data.WeatherData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandleParseXML extends DefaultHandler
{
	/*
	 * These variables name are named according to the XML.
	 */
	private boolean		in_forecast_information	= false;
	private boolean		in_current_conditions	= false;
	private boolean		in_forecast_conditions	= false;

	/*
	 * To store whether parse the XML ok or not.
	 */
	private boolean		bParseOK				= false;

	private WeatherData	weatherData				= null;

	public WeatherData getWeatherData()
	{
		return this.weatherData;
	}

	@Override
	public void startDocument() throws SAXException
	{
		this.weatherData = new WeatherData();
		this.weatherData.setCurrentConditionsData(new CurrentConditionsData());
		this.weatherData
				.setForecastInformationData(new ForecastInformationData());
	}

	@Override
	public void endDocument() throws SAXException
	{

	}

	@Override
	public void startElement(String namespaceUIR, String localName,
			String qName, Attributes atts) throws SAXException
	{
		if (localName.equals("forecast_information"))
		{
			this.in_forecast_information = true;

		} else if (localName.equals("current_conditions"))
		{
			this.in_current_conditions = true;
		} else if (localName.equals("forecast_conditions"))
		{
			this.in_forecast_conditions = true;
			this.weatherData.getForecastConditionsData().add(
					new ForecastConditionsData());
		} else
		{
			String data = atts.getValue("data");
			if (localName.equals("city"))
			{
				this.weatherData.getForecastInformationData().setCity(data);

			} else if (localName.equals("postal_code"))
			{
				this.weatherData.getForecastInformationData().setPostalCode(
						data);

			} else if (localName.equals("latitude_e6"))
			{
				this.weatherData.getForecastInformationData().setLatitude(data);

			} else if (localName.equals("longitude_e6"))
			{
				this.weatherData.getForecastInformationData()
						.setLongitude(data);

			} else if (localName.equals("forecast_date"))
			{
				this.weatherData.getForecastInformationData().setForecastDate(
						data);

			} else if (localName.equals("current_date_time"))
			{
				this.weatherData.getForecastInformationData()
						.setCurrentDateTime(data);

			} else if (localName.equals("unit_system"))
			{
				this.weatherData.getForecastInformationData().setUnitSystem(
						data);

			} else if (localName.equals("condition"))
			{
				if (this.in_current_conditions)
				{
					this.weatherData.getCurrentConditionsData().setCondition(
							data);
				} else if (this.in_forecast_conditions)
				{
					this.weatherData.getLastForecastConditionsData()
							.setCondition(data);
				}

			} else if (localName.equals("temp_f"))
			{
				this.weatherData.getCurrentConditionsData().setTempF(data);

			} else if (localName.equals("temp_c"))
			{
				this.weatherData.getCurrentConditionsData().setTempC(data);

			} else if (localName.equals("humidity"))
			{
				this.weatherData.getCurrentConditionsData().setHumidity(data);

			} else if (localName.equals("icon"))
			{
				if (this.in_current_conditions)
				{
					this.weatherData.getCurrentConditionsData().setIcon(data);
				} else if (this.in_forecast_conditions)
				{
					this.weatherData.getLastForecastConditionsData().setIcon(
							data);
				}

			} else if (localName.equals("wind_condition"))
			{
				this.weatherData.getCurrentConditionsData().setWindCondition(
						data);

			} else if (localName.equals("day_of_week"))
			{
				this.weatherData.getLastForecastConditionsData().setDayOfWeek(
						data);

			} else if (localName.equals("low"))
			{
				this.weatherData.getLastForecastConditionsData().setLowTemp(
						TempConvert.FToC(Integer.parseInt(data)));

			} else if (localName.equals("high"))
			{
				this.weatherData.getLastForecastConditionsData().setHighTemp(
						TempConvert.FToC(Integer.parseInt(data)));

			}

		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException
	{
		if (localName.equals("forecast_information"))
		{
			this.in_forecast_information = false;
		} else if (localName.equals("current_conditions"))
		{
			this.in_current_conditions = false;
		} else if (localName.equals("forecast_conditions"))
		{
			this.in_forecast_conditions = false;
			this.bParseOK = true;
		}
	}

	@Override
	public void characters(char ch[], int start, int length)
	{

	}

	public boolean bParseOK()
	{
		return this.bParseOK;
	}

}
