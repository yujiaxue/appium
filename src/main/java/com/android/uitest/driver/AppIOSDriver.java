package com.android.uitest.driver;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.autotest.entrypoint.IDriver;
import com.driver.manage.FileUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

/**
 * authorize-ios
 * Make sure UI Automation is enabled on your device. Settings -> Developer -> Enable UI Automation
 * 
 * @author zhangfujun
 * > ideviceinstaller -u 2f8b74d0d1e291d035bda425340f10dc8df2b134 -i /Users/zhangfujun/testApp/96a511fc926846bf864b771039d59b64.ipa


 */
public class AppIOSDriver implements IDriver {

	public AppiumDriver<WebElement> driver = null;

	@Override
	public AppiumDriver<WebElement> genDriver(Map<String, String> config) {
		File app = new File(config.get(UIFlags.IosIpa));
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

//		desiredCapabilities.setCapability("unicodekeyboard", true);
//		desiredCapabilities.setCapability("resetkeyboard", true);
		
		desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
		desiredCapabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.6.4");
		desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, config.get(UIFlags.DEVICE_NAME));
		desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, config.get(UIFlags.IOS_PLATFORM_VERSION));
		desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
		desiredCapabilities.setCapability(MobileCapabilityType.UDID, "e322388af97fa223fe1ccc742c3b96040b96228f");//"2f8b74d0d1e291d035bda425340f10dc8df2b134"
		desiredCapabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
		
		
		desiredCapabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		//desiredCapabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
		// capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
		// AutomationName.IOS_XCUI_TEST);

		
		try { // "http://127.0.0.1:4723/wd/hub"
			//driver = new AppiumDriver<>(new URL(config.get("url")), desiredCapabilities);
			driver = new IOSDriver<>(new URL(config.get("url")), desiredCapabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return driver;
	}

	/**
	 * identifier MobileBy.AccessibilityId Label MobileBy.name Hint
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		HashMap<String, String> config = FileUtils.load().loadProperties();
		config.put("url", "http://127.0.0.1:4723/wd/hub");
		AppiumDriver<WebElement> driver = getInstance().genDriver(config);
		// System.out.println(driver.getPageSource());
		// WebElement ele = driver.findElement(for_text("Buttons,
		// AAPLButtonViewController"));
		// WebElement ele =
		// driver.findElement(MobileBy.AccessibilityId("Buttons,
		// AAPLButtonViewController"));
		// List<WebElement> ele =
		// driver.findElements(MobileBy.className("XCUIElementTypeCell"));

		System.out.println(driver.getContext());
		try {
			if (driver.findElement(MobileBy.AccessibilityId("Allow")) != null) {
				driver.findElement(MobileBy.AccessibilityId("Allow")).click();
			}
		} catch (Exception e) {
			System.out.println("没找到弹出框");
		}

		Thread.sleep(5000);
		try {
			WebElement ele = driver.findElement(MobileBy.xpath("//XCUIElementTypeButton//XCUIElementTypeButton[5]"));// MobileBy.AccessibilityId("textField2"));
			ele.click();
		} catch (Exception e) {
			System.out.println("ooooo");
			TouchAction tc = new TouchAction(driver);
			tc.tap(332, 639).perform();
		}

		driver.findElement(MobileBy.AccessibilityId("登录 /")).click();
		WebElement e = driver.findElement(MobileBy.xpath("//*[1]//*[1]//*[1]//*[1]//*[1]//*[1]//*[1]//*[2]//*[1]"));
		e.clear();
		e.sendKeys("15000173575");

		// ele.sendKeys("2014");
		//
		// WebElement ele1 = driver.findElement(MobileBy.name("submitBtn"));
		// ele1.click();
		// Thread.sleep(5000);
		// ele.clear();
		// ele.sendKeys("2013");
		// ele1.click();

	}

	public synchronized static AppIOSDriver getInstance() {
		return new AppIOSDriver();
	}

	public static By for_text(String text) {
		String up = text.toUpperCase();
		String down = text.toLowerCase();
		return By.xpath("//UIAStaticText[@visible=\"true\" and (contains(translate(@name,\"" + up + "\",\"" + down
				+ "\"), \"" + down + "\") or contains(translate(@hint,\"" + up + "\",\"" + down + "\"), \"" + down
				+ "\") or contains(translate(@label,\"" + up + "\",\"" + down + "\"), \"" + down
				+ "\") or contains(translate(@value,\"" + up + "\",\"" + down + "\"), \"" + down + "\"))]");
	}
}

//WebDriverAgentRunner-Runner.app, NSUnderlyingError=0x7fb40ceca240 {Error Domain=NSPOSIXErrorDomain Code=2 "No such file or directory"}}
//
//
//[MJSONWP] Bad parameters: BadParametersError: Parameters were incorrect. We wanted 
//{"required":["desiredCapabilities"],"optional":["requiredCapabilities","capabilities","sessionId","id"]} 
//and you sent ["capabilities"]
//
//		desired capabilities = Capabilities 
//		[{unicodekeyboard=true, app=/Users/zhangfujun/Library/Developer/Xcode/DerivedData/RedStarMain
//		-efozwcglfckzoveakseotcbiycuz/Build/Products/Debug-iphoneos/RedStarMain.app, 
//		appiumVersion=1.6.4, platformVersion=10.2, automationName=XCUITest, resetkeyboard=true,
//		platformName=iOS, udid=2f8b74d0d1e291d035bda425340f10dc8df2b134, deviceName=iPhone}], 
//		required capabilities = Capabilities [{}]
//				Build info: version: '3.3.1', revision: '5234b325d5', time: '2017-03-10 09:10:29 +0000'