package com.change_vision.astah.extension.plugin.dbreverse;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.change_vision.jude.api.inf.ui.IMessageDialogHandler;
import com.change_vision.jude.api.inf.ui.IMessageDialogHandlerFactory;

public class Activator implements BundleActivator {

	private static IMessageDialogHandler messageHandler;
	private ServiceReference reference;

	public void start(BundleContext context) {
		ServiceTracker tracker = new ServiceTracker(context, IMessageDialogHandlerFactory.class.getName(), null);
		tracker.open();
		initializeMessageDialogHandler(context);
	}

	public void stop(BundleContext context) {
	}

	public static IMessageDialogHandler getMessageHandler() {
		return messageHandler;
	}

	private void initializeMessageDialogHandler(BundleContext context) {
		reference = context.getServiceReference(IMessageDialogHandlerFactory.class.getName());
		IMessageDialogHandlerFactory factory = (IMessageDialogHandlerFactory)context.getService(reference);
		if (factory != null) {
			messageHandler = factory.createMessageDialogHandler(new Messages(), "\\.astah\\professional\\dbreverse.log");
		}
		context.ungetService(reference);
	}
}