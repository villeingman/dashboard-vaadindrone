package org.vaadin.inki.samples;

import org.vaadin.inki.samples.MonitorView.MonitorEventListener;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Axis;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SpeedPanel extends ChartPanel implements MonitorEventListener {

	private Label speedValue = new Label();
	private DataSeries speedSeries = new DataSeries();

	private SpeedPanel() {
		super();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setSizeFull();

		Chart chart = getSpeedChart();
		layout.addComponent(chart);
		speedValue.addStyleName(ValoTheme.LABEL_HUGE);
		layout.addComponent(speedValue);
		speedValue.setWidth("100px");
		layout.setExpandRatio(chart, 1);
		setContent(layout);
	}

	public SpeedPanel(MonitorView monitorView) {
		this();
		// monitorView.addMonitorEventListener(this);
	}

	@Override
	public void newDroneEvent(DroneDataSample data) {
		// boolean shift = false;
		// if (speedSeries.size() > 20) {
		// shift = true;
		// }
		// speedSeries.add(
		// new DataSeriesItem(dataList.getDataTimestamp(), dataList.getSpeed()),
		// true, shift);
		// speedValue.setValue(String.valueOf(dataList.getSpeed()));
	}

	@Override
	public String getPanelCaption() {
		return "Speed";
	}

	private Chart getSpeedChart() {
		Chart speedChart = new Chart();

		final Configuration configuration = speedChart.getConfiguration();
		configuration.getChart().setType(ChartType.SPLINE);
		configuration.getTitle().setText("");

		Axis xAxis = configuration.getxAxis();
		xAxis.setType(AxisType.DATETIME);

		YAxis yAxis = configuration.getyAxis();
		yAxis.setTitle("");
		yAxis.setMax(20);
		yAxis.setMin(-2);

		configuration.getTooltip().setEnabled(false);
		configuration.getLegend().setEnabled(false);

		PlotOptionsSpline opts = new PlotOptionsSpline();
		opts.setMarker(new Marker(false));
		opts.setColor(SolidColor.CADETBLUE);
		opts.setShadow(false);
		opts.setLineWidth(4);
		speedSeries.setPlotOptions(opts);
		configuration.setSeries(speedSeries);

		speedChart.drawChart(configuration);
		speedChart.setSizeFull();
		return speedChart;
	}

}
