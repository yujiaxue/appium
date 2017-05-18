package com.android.uitest.driver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.autotest.Listener.ActionListener;
import com.autotest.Listener.EventListener;
import com.autotest.Listener.ExceptionListener;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.EventFiringWebDriverFactory;
import io.appium.java_client.events.api.Listener;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class AppiumDriverManger {

	private static String udid;
	private static String port;

	public static String getUdid() {
		return udid;
	}

	public static void setUdid(String udid) {
		AppiumDriverManger.udid = udid;
	}

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		AppiumDriverManger.port = port;
	}

	@SuppressWarnings("rawtypes")
	private static ThreadLocal<AppiumDriver> driverHolder = new ThreadLocal<AppiumDriver>() {
		@Override
		protected AppiumDriver<MobileElement> initialValue() {
			AppiumDriver<MobileElement> driver = null;

			File app = new File("/Users/zhangfujun/testApp/hxmkl.apk");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
			capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
			capabilities.setCapability(MobileCapabilityType.UDID, udid);
			System.out.println("udid is ..." + udid);
			try {
				driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			List<Listener> listeners = new ArrayList<>();
			listeners.add(new ActionListener());
			listeners.add(new EventListener());
			listeners.add(new ExceptionListener());
			driver = EventFiringWebDriverFactory.getEventFiringWebDriver(driver, listeners);
			return driver;
		}
	};

	@SuppressWarnings("unchecked")
	public static AppiumDriver<MobileElement> getDriver() {
		return driverHolder.get();
	}
}
