package com.autotest.Listener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.android.uitest.driver.UIFlags;
import com.autotest.entrypoint.Context;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class TakeScreen extends Context {

	public static String takeSreen(WebDriver driver) {

		@SuppressWarnings("unchecked")
		String id = ((AppiumDriver<MobileElement>) driver).getSessionId().toString();
		String fileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date()) + ".png";
		String picPath = config.get(UIFlags.IMAGE_PATH).concat(File.separator).concat(id).concat(File.separator);
		// String picPath =
		// "/Users/zhangfujun/Logs/appiumLogs/".concat(id).concat(File.separator);
		String file = picPath.concat(fileName);
		boolean sw = false;
		// if (((AppiumDriver) driver).isBrowser()) {
		// // driver.switchTo().window("NATIVE_APP");
		// ((AppiumDriver) driver).context("NATIVE_APP");
		// sw = true;
		// }
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// if (sw) {
		// switchToWebView(driver);
		// }
		try {
			FileUtils.copyFile(scrFile, new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public static void switchToWebView(WebDriver driver) {
		Set<String> context = ((AppiumDriver) driver).getContextHandles();
		for (String c : context) {
			if (c.startsWith("WEBVIEW")) {
				((AppiumDriver<MobileElement>) driver).context(c);
				// driver.switchTo().window("NATIVE_APP");
				break;
			}
		}
	}
}
