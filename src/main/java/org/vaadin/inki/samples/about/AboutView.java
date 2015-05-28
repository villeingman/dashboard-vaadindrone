package org.vaadin.inki.samples.about;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.vaadin.inki.samples.DroneDataListener;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AboutView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "Social";
	private Twitter twitter;
	private TwitterFactory twitterFactory;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public AboutView() {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("")
				.setOAuthConsumerSecret(
						"")
				.setOAuthAccessToken(
						"")
				.setOAuthAccessTokenSecret(
						"");
		twitterFactory = new TwitterFactory(cb.build());
		twitter = twitterFactory.getInstance();

		CustomLayout aboutContent = new CustomLayout("aboutview");
		aboutContent.setStyleName("about-content");

		VerticalLayout card = new VerticalLayout();
		card.setSpacing(true);
		final TextField field = new TextField("Your Twitter");
		field.addStyleName(ValoTheme.TEXTFIELD_HUGE);
		field.setWidth("300px");
		field.setInputPrompt("@your-twitter-handle");
		card.addComponent(field);
		Button btn = new Button("Blink");
		btn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btn.addStyleName(ValoTheme.BUTTON_HUGE);
		btn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				DroneDataListener.getInstance().sendBlinkCommand();
				String twitterHandle = field.getValue();
				doTwitterMagic(twitterHandle);
			}

			private void doTwitterMagic(String twitterHandle) {
				if (twitterHandle != null) {
					try {
						if (twitterHandle.startsWith("@")) {
							twitterHandle = twitterHandle.substring(1);
						}
						String[] handles = new String[] { twitterHandle };
						ResponseList<User> foundUsers = null;
						try {
							foundUsers = twitter.lookupUsers(handles);
						} catch (Exception e) {
							Notification.show("Sorry, you don't exist",
									Notification.Type.WARNING_MESSAGE);
							return;
						}
						if (foundUsers != null && foundUsers.size() == 1) {
							if (twitterHandle.indexOf("@") == -1) {
								twitterHandle = "@" + twitterHandle;
							}
							Date date = new Date();
							twitter.updateStatus(twitterHandle
									+ " just remotely blinked me at #iotconf_sweden in mid-air via #Bluemix. #Vaadin "
									+ timeFormat.format(date));
							Notification
									.show("Thanks! Check your Twitter and RT");
						}
					} catch (Exception e) {
						Notification.show("Sorry, didn't go through", Type.WARNING_MESSAGE);
						e.printStackTrace();
					}
				}
			}
		});
		card.addComponent(btn);
		card.setComponentAlignment(field, Alignment.BOTTOM_LEFT);
		card.setComponentAlignment(btn, Alignment.BOTTOM_LEFT);

		// you can add Vaadin components in predefined slots in the custom
		// layout
		aboutContent.addComponent(card, "info");

		setSizeFull();
		setStyleName("about-view");
		addComponent(aboutContent);
		setComponentAlignment(aboutContent, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
