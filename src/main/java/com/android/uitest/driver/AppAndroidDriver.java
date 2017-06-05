package com.android.uitest.driver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.autotest.Listener.ActionListener;
import com.autotest.Listener.EventListener;
import com.autotest.Listener.ExceptionListener;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.StartsActivity;
import io.appium.java_client.events.EventFiringWebDriverFactory;
import io.appium.java_client.events.api.Listener;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class AppAndroidDriver {

	public AppiumDriver<WebElement> driver = null;
	private String udid = null;
	private String url = null;

	public AppiumDriver<WebElement> genDriver(Map<String, String> config) {

		File app = new File(config.get(UIFlags.AndroidApk));
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		capabilities.setCapability(MobileCapabilityType.VERSION, 5.1);
		capabilities.setCapability(MobileCapabilityType.UDID, config.get(UIFlags.UDID));
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM);
		capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);

		// capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
		// ".business.main.MainActivity");
		// capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
		// "com.redstar.mainapp");
		// com.redstar.mainapp.business.LaunchActivity -"com.redstar.mainapp"
		capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, config.get(UIFlags.WAIT_ACTIVITY)); // ".business.LaunchActivity"
		capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_PACKAGE, config.get(UIFlags.WAIT_PACKAGE));

		capabilities.setCapability(MobileCapabilityType.SUPPORTS_JAVASCRIPT, true);
		capabilities.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, true);
		capabilities.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, true);

		capabilities.setCapability(AndroidMobileCapabilityType.RECREATE_CHROME_DRIVER_SESSIONS, true);
		capabilities.setCapability(AndroidMobileCapabilityType.NATIVE_WEB_SCREENSHOT, true);

		// capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);

		try {
			driver = new AndroidDriver<>(new URL(config.get(UIFlags.APPIUM_SERVER_URL)), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//System.out.println(((StartsActivity) driver).currentActivity());

		driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.get(UIFlags.ELEMENT_WAIT).trim()),
				TimeUnit.SECONDS);
		udid = config.get("udid");
		url = config.get(UIFlags.APPIUM_SERVER_URL);
		List<Listener> listeners = new ArrayList<>();
		listeners.add(new ActionListener());
		listeners.add(new EventListener());
		listeners.add(new ExceptionListener());
		driver = EventFiringWebDriverFactory.getEventFiringWebDriver(driver, listeners);
		System.out.println("driver is successed generate...");
		return driver;
	}

	public boolean equals(Object o) {
		AppAndroidDriver d = (AppAndroidDriver) o;
		if (this.udid.equals(d.udid) | this.url.equals(d.url))
			return true;
		else
			return false;
	}

}
