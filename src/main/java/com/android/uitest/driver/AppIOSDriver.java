package com.android.uitest.driver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.autotest.entrypoint.IDriver;
import com.driver.manage.FileUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class AppIOSDriver implements IDriver {

	public AppiumDriver<WebElement> driver = null;

	@Override
	public AppiumDriver<WebElement> genDriver(Map<String, String> config) {
		File app = new File(config.get(UIFlags.IosIpa));
		DesiredCapabilities capabilities = new DesiredCapabilities();
		
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, config.get(UIFlags.DEVICE_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, config.get(UIFlags.IOS_PLATFORM_VERSION));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);

		try { // "http://127.0.0.1:4723/wd/hub"
			driver = new AppiumDriver<>(new URL(config.get("url")), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return driver;
	}

	public static void main(String[] args) {
		HashMap<String, String> config = FileUtils.load().loadProperties();
		config.put("url", "http://127.0.0.1:4723/wd/hub");
		AppiumDriver<WebElement> driver = getInstance().genDriver(config);
		System.out.println(driver.isAppInstalled(""));
	}

	public synchronized static AppIOSDriver getInstance() {
		return new AppIOSDriver();
	}
}
