package org.vaadin.inki.samples;

import org.vaadin.inki.samples.MonitorView.MonitorEventListener;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class ChartPanel extends CssLayout implements MonitorEventListener {

	private static final long serialVersionUID = 1L;
	private VerticalLayout root;

	public ChartPanel() {
		addStyleName(ValoTheme.LAYOUT_CARD);
		setSizeFull();
		this.root = new VerticalLayout();
		this.root.setSizeFull();
		this.root.setSpacing(true);
		this.root.setMargin(true);
		addComponent(this.root);
		Label cap = new Label(getPanelCaption());
		cap.addStyleName(ValoTheme.LABEL_LARGE);
		this.root.addComponent(cap);
	}

	protected void setContent(Component component) {
		this.root.addComponent(component, 1);
		this.root.setExpandRatio(component, 1);
	}

	@Override
	public abstract void newDroneEvent(DroneDataSample data);
	public abstract String getPanelCaption();
}
