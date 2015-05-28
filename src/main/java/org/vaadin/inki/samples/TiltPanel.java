package org.vaadin.inki.samples;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Background;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Pane;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;

@SuppressWarnings("serial")
public class TiltPanel extends ChartPanel {

	private static final double MIN_TILT = 3;
	private static final double MAX_TILT = 15;
	private static final Number CHART_MAX_TILT = MAX_TILT + 2;
	private ListSeries tiltSeries = new ListSeries(MIN_TILT, MIN_TILT,
			MIN_TILT, MIN_TILT);

	private TiltPanel() {
		super();
		Chart chart = getTiltChart();
		chart.setSizeFull();
		setContent(chart);
	}

	public TiltPanel(MonitorView monitorView) {
		this();
		monitorView.addMonitorEventListener(this);
	}

	@Override
	public void newDroneEvent(DroneDataSample data) {
		tiltSeries.updatePoint(0, MIN_TILT);
		tiltSeries.updatePoint(1, MIN_TILT);
		tiltSeries.updatePoint(2, MIN_TILT);
		tiltSeries.updatePoint(3, MIN_TILT);
		double pitch = data.getPitch() / 1000;
		double roll = data.getRoll() / 1000;
		if (pitch < 0) {
			pitch = Math.abs(pitch);
			if (pitch < MIN_TILT) {
				pitch = MIN_TILT;
			} else if (pitch > MAX_TILT) {
				pitch = MAX_TILT;
			}
			tiltSeries.updatePoint(0, pitch);
			tiltSeries.updatePoint(2, MIN_TILT);
		} else {
			if (pitch < MIN_TILT) {
				pitch = MIN_TILT;
			} else if (pitch > MAX_TILT) {
				pitch = MAX_TILT;
			}
			tiltSeries.updatePoint(0, MIN_TILT);
			tiltSeries.updatePoint(2, pitch);
		}
		if (roll > 0) {
			if (roll < MIN_TILT) {
				roll = MIN_TILT;
			} else if (roll > MAX_TILT) {
				roll = MAX_TILT;
			}
			tiltSeries.updatePoint(1, roll);
			tiltSeries.updatePoint(3, MIN_TILT);
		} else {
			roll = Math.abs(roll);
			if (roll < MIN_TILT) {
				roll = MIN_TILT;
			} else if (roll > MAX_TILT) {
				roll = MAX_TILT;
			}
			tiltSeries.updatePoint(1, MIN_TILT);
			tiltSeries.updatePoint(3, roll);
		}
	}

	@Override
	public String getPanelCaption() {
		return "Tilt";
	}

	private Chart getTiltChart() {
		Chart chart = new Chart();

		Configuration conf = chart.getConfiguration();
		conf.getChart().setPolar(true);
		conf.setTitle("");

		conf.getLegend().setEnabled(false);
		conf.getTooltip().setEnabled(false);

		Pane pane = new Pane(-45, 315);
		pane.setBackground(new Background[] {});
		conf.addPane(pane);

		XAxis axis = new XAxis();
		axis.setCategories(new String[] { "forward", "right", "backward",
				"left" });
		conf.addxAxis(axis);

		YAxis yaxis = new YAxis();
		yaxis.getLabels().setEnabled(false);
		yaxis.setMax(CHART_MAX_TILT);
		conf.addyAxis(yaxis);

		PlotOptionsColumn opts = new PlotOptionsColumn();
		opts.setColor(SolidColor.ORANGE);
		opts.setShadow(false);
		tiltSeries.setPlotOptions(opts);

		conf.setSeries(tiltSeries);
		chart.drawChart(conf);
		chart.setSizeFull();
		return chart;
	}

}
