package org.vaadin.inki;

import javax.servlet.annotation.WebFilter;

import net.sf.ehcache.constructs.web.filter.GzipFilter;

/**
 * As we are not using CDN for this simple app, it is a good idea to use GZIP
 * compression to keep loading time and bandwidth usage to minimum. This is
 * optional though.
 *
 */
//@WebFilter({ "*.js", "*.css", "/UIDL/*" })
public class CompressionFilter extends GzipFilter {

	public CompressionFilter() {

	}
}
