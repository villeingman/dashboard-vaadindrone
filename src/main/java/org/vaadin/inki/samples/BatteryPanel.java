package org.vaadin.inki.samples;

import org.vaadin.inki.samples.MonitorView.MonitorEventListener;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.addon.charts.model.Title;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class BatteryPanel extends ChartPanel implements MonitorEventListener {

	private Chart chart;
	private Label batteryLevel = new Label();
	private ListSeries batteryDepletedSeries = new ListSeries(99);
	private ListSeries batteryCapacitySeries = new ListSeries(1);

	private BatteryPanel() {
		super();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();

		chart = getChart();
		layout.addComponent(chart);
		batteryLevel = getBatteryLevel();
		layout.addComponent(batteryLevel);
		batteryLevel.setWidth("100px");
		layout.setExpandRatio(chart, 1);
		setContent(layout);
	}

	public BatteryPanel(MonitorView monitorView) {
		this();
		monitorView.addMonitorEventListener(this);
	}

	@Override
	public String getPanelCaption() {
		return "Battery";
	}

	private Label getBatteryLevel() {
		Label label = new Label();
		label.setValue("-- %");
		label.addStyleName(ValoTheme.LABEL_HUGE);
		return label;
	}

	private Chart getChart() {
		Chart chart = new Chart(ChartType.COLUMN);

		Configuration conf = chart.getConfiguration();
		conf.setTitle("");

		XAxis xAxis = new XAxis();
		xAxis.setCategories("");
		conf.addxAxis(xAxis);

		YAxis yAxis = new YAxis();
		yAxis.setMin(0);
		yAxis.setMax(100);
		yAxis.setTitle(new Title("%"));
		conf.addyAxis(yAxis);

		PlotOptionsColumn plotOptions = new PlotOptionsColumn();
		plotOptions.setStacking(Stacking.PERCENT);
		plotOptions.setShadow(false);
		conf.setPlotOptions(plotOptions);

		conf.getTooltip().setEnabled(false);
		conf.getLegend().setEnabled(false);

		PlotOptionsColumn depletedOpt = new PlotOptionsColumn();
		depletedOpt.setColor(SolidColor.LIGHTGRAY);
		batteryDepletedSeries.setPlotOptions(depletedOpt);
		PlotOptionsColumn batteryOpt = new PlotOptionsColumn();
		batteryOpt.setColor(SolidColor.LIGHTGREEN);
		batteryCapacitySeries.setPlotOptions(batteryOpt);

		conf.addSeries(batteryDepletedSeries);
		conf.addSeries(batteryCapacitySeries);

		chart.drawChart(conf);
		chart.setWidth("80%");
		chart.setHeight("100%");

		return chart;
	}

	@Override
	public void newDroneEvent(DroneDataSample data) {
		if (batteryDepletedSeries == null || batteryCapacitySeries == null) {
			return;
		}
		batteryDepletedSeries.updatePoint(0, 100 - data.getBatteryPercentage());
		batteryCapacitySeries.updatePoint(0, data.getBatteryPercentage());
		batteryLevel.setValue(data.getBatteryPercentage() + " %");
	}
}
