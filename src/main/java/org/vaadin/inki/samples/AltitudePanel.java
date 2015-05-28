package org.vaadin.inki.samples;

import java.text.DecimalFormat;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Axis;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AltitudePanel extends ChartPanel {

	private static final Number MAX_ALTITUDE = 2;
	private static final Number MIN_ALTITUDE = -0.2;
	private Label altitudeValue = new Label();
	private DataSeries altitudeSeries = new DataSeries();
	private Chart altitudeChart = new Chart();

	private AltitudePanel() {
		super();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();

		Chart chart = getAltitudeChart();
		layout.addComponent(chart);
		altitudeValue.addStyleName(ValoTheme.LABEL_HUGE);
		altitudeValue.setValue("-- m");
		layout.addComponent(altitudeValue);
		altitudeValue.setWidth("100px");
		layout.setExpandRatio(chart, 1);
		setContent(layout);
	}

	private Chart getAltitudeChart() {

		final Configuration configuration = altitudeChart.getConfiguration();
		configuration.getChart().setType(ChartType.SPLINE);
		configuration.getTitle().setText("");

		Axis xAxis = configuration.getxAxis();
		xAxis.setType(AxisType.DATETIME);
		xAxis.setTickPixelInterval(200);

		YAxis yAxis = configuration.getyAxis();
		yAxis.setTitle("");
		yAxis.setMax(MAX_ALTITUDE);
		yAxis.setMin(MIN_ALTITUDE);

		configuration.getTooltip().setEnabled(false);
		configuration.getLegend().setEnabled(false);

		PlotOptionsSpline opts = new PlotOptionsSpline();
		opts.setMarker(new Marker(false));
		opts.setColor(SolidColor.YELLOWGREEN);
		opts.setShadow(false);
		opts.setLineWidth(4);
//		opts.setThreshold(500);
//		opts.setNegativeColor(SolidColor.YELLOWGREEN);
		altitudeSeries.setPlotOptions(opts);
		configuration.setSeries(altitudeSeries);

		altitudeChart.drawChart(configuration);
		altitudeChart.setSizeFull();
		return altitudeChart;
	}

	public AltitudePanel(MonitorView monitorView) {
		this();
		monitorView.addMonitorEventListener(this);
	}

	@Override
	public void newDroneEvent(DroneDataSample data) {
		boolean shift = false;
		if (altitudeSeries.size() > 100) {
			shift = true;
		}
			double altitudeMeters = (double) data.getAltitude() / 1000;
			altitudeSeries.add(new DataSeriesItem(data.getTimestamp(),
					altitudeMeters), true, shift);
			DecimalFormat df = new DecimalFormat("0.0");
			altitudeValue.setValue(df.format(altitudeMeters) + " m");
	}

	@Override
	public String getPanelCaption() {
		return "Altitude";
	}

}
