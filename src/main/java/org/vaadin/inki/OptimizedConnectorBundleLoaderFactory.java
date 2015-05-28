package org.vaadin.inki;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect.LoadStyle;

public class OptimizedConnectorBundleLoaderFactory extends
		ConnectorBundleLoaderFactory {
	private Set<String> eagerConnectors = new HashSet<String>();
	{
		eagerConnectors
				.add(com.vaadin.client.ui.ui.UIConnector.class.getName());
		eagerConnectors
				.add(com.vaadin.client.ui.gridlayout.GridLayoutConnector.class
						.getName());
		eagerConnectors.add(com.vaadin.client.ui.menubar.MenuBarConnector.class
				.getName());
		eagerConnectors
				.add(com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector.class
						.getName());
		eagerConnectors.add(com.vaadin.client.ui.button.ButtonConnector.class
				.getName());
		eagerConnectors
				.add(com.vaadin.addon.charts.client.ui.ChartConnector.class
						.getName());
		eagerConnectors.add(com.vaadin.client.ui.label.LabelConnector.class
				.getName());
		eagerConnectors
				.add(com.vaadin.client.ui.csslayout.CssLayoutConnector.class
						.getName());
		eagerConnectors
				.add(com.vaadin.client.extensions.ResponsiveConnector.class
						.getName());
		eagerConnectors
				.add(com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector.class
						.getName());
		eagerConnectors.add(com.vaadin.client.ui.image.ImageConnector.class
				.getName());
		eagerConnectors
				.add(com.vaadin.client.ui.customlayout.CustomLayoutConnector.class
						.getName());
		eagerConnectors
				.add(com.vaadin.client.ui.textfield.TextFieldConnector.class
						.getName());
	}

	@Override
	protected LoadStyle getLoadStyle(JClassType connectorType) {
		if (eagerConnectors.contains(connectorType.getQualifiedBinaryName())) {
			return LoadStyle.EAGER;
		} else {
			// Loads all other connectors immediately after the initial view has
			// been rendered
			return LoadStyle.DEFERRED;
		}
	}
}
