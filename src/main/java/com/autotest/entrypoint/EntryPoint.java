package com.autotest.entrypoint;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.autotest.Listener.TakeScreen;
import com.autotest.assertion.Assertion;
import com.persist.api.Operation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.HasDeviceDetails;
import io.appium.java_client.android.StartsActivity;

public class EntryPoint {
	AppiumDriver<?> driver = null;
	String sessionId = null;
	int caseId = 0;
	TouchAction touchAction = null;
	// int executeId =0;

	// public int getExecuteId() {
	// return executeId;
	// }
	//
	// public void setExecuteId(int executeId) {
	// this.executeId = executeId;
	// }

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public EntryPoint(AppiumDriver<WebElement> driver) {
		this.driver = driver;
		touchAction = new TouchAction(this.driver);
		this.sessionId = this.driver.getSessionId().toString();
		Assertion.setAttr(this.driver, this.sessionId);
	}

	public String getSessionId() {
		return driver.getSessionId().toString();
	}

	/**
	 * 断言相等 actual== expect为真，否则用例失败
	 * 
	 * @param actual
	 * @param expect
	 */
	public void assertEquals(String actual, String expect) {
		Assertion.assertEquals(expect, actual);
	}

	/**
	 * 断言包含 actual包含expect为真
	 * 
	 * @param actual
	 * @param expect
	 */
	public void assertContains(String actual, String expect) {
		Assertion.assertContains(actual, expect);
	}

	/**
	 * 获取界面元素支持 id,class,xpath,accessbility id
	 * 
	 * @param locator
	 * @return
	 */
	public WebElement getElement(String locator) {
		putCaseId();
		By type = TypeCheck.getLocatorType(locator);
		WebElement ele = null;
		try {
			ele = driver.findElement(type);
		} catch (NoSuchElementException e) {
			Assertion.noElementFail(locator, e.getMessage(), String.valueOf(getCaseId()));
		} catch (Exception e) {
			e.printStackTrace();
			// 用例失败标记，
			// 脚本中断退出，未知异常
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					sessionId, String.format("unknown error { %s ,%s}", locator, e.getMessage()), "", getCaseId());
			Assertion.failCase();
		}
		return ele;
	}

	/**
	 * 获取元素列表
	 * 
	 * @param locator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebElement> getElements(String locator) {
		By type = TypeCheck.getLocatorType(locator);
		List<WebElement> eles = null;
		try {
			eles = (List<WebElement>) driver.findElements(type);
		} catch (NoSuchElementException e) {
			Assertion.noElementFail(locator, e.getMessage(), String.valueOf(getCaseId()));
		} catch (Exception e) {
			e.printStackTrace();
			// Assertion.failCase("fail");
		}
		return eles;
	}

	/**
	 * 验证元素
	 * 
	 * @param locator
	 * @return
	 */
	public WebElement checkElement(String locator) {
		By type = TypeCheck.getLocatorType(locator);
		WebElement ele = null;
		try {
			ele = driver.findElement(type);
		} catch (NoSuchElementException e) {
			System.out.println("Element isnot found :" + locator + " :" + e.getMessage().toString());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ele;
	}

	/**
	 * 点击元素
	 * 
	 * @param locator
	 *            定位器
	 * @param log
	 *            自定义日志
	 */
	public void click(String locator, String log) {
		WebElement me = getElement(locator);
		try {
			me.click();
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(), String.format("Click Element { %s }, { %s }", locator, log), fileName, getCaseId());
			Assertion.failCase();
		}
	}

	/**
	 * 点击元素
	 * 
	 * @param locator
	 *            定位器
	 */
	public void click(String locator) {
		click(locator, String.format("点击元素Element { %s }", locator));
	}

	public void enterText(String locator, String text, String log) {
		WebElement ele = getElement(locator);
		try {
			ele.sendKeys(text);
			String fileName = TakeScreen.takeSreen(driver);
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(), String.format("enter { %s } to Element { %s }, { %s }", text, locator, log),
					fileName, getCaseId());
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(),
					String.format("enter { %s } to Element { %s }, { %s }", text, locator, e.getMessage()), fileName,
					getCaseId());
			Assertion.failCase();
		}
	}

	/**
	 * 输入文本至locator
	 * 
	 * @param locator
	 *            定位器
	 * @param text
	 *            文本
	 */
	public void enterText(String locator, String text) {
		enterText(locator, text, String.format("输入文本至Element { %s }", locator));
	}

	/**
	 * 获取文本
	 * 
	 * @param locator
	 * @return
	 */
	public String getText(String locator) {
		WebElement ele = getElement(locator);
		String text = null;
		try {
			text = ele.getText();
			String fileName = TakeScreen.takeSreen(driver);
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(), String.format("getText from  Element { %s }", locator), fileName, getCaseId());
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(), String.format("getText from  Element { %s }", locator), fileName, getCaseId());
			Assertion.failCase();
		}
		return text;
	}

	/**
	 * 滚动至元素locator
	 * 
	 * @param locator
	 */
	public void scrollTo(String locator) {
		// WebElement element = getElement(locator);
		// HashMap<String, String> arguments = new HashMap<String, String>();
		// arguments.put("element", element.getId());
		// (JavascriptExecutor)driver.executeScript("mobile: scrollTo",
		// arguments);
		//
		// TouchAction
	}

	/**
	 * 切换webview
	 * /usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/mac
	 */
	public void switchToWebView() {
		Set<String> context = driver.getContextHandles();
		String webview = null;
		try {
			for (String c : context) {
				if (c.startsWith("WEBVIEW")) {
					driver.context(c);
					webview = c;
					break;
				}
			}
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(), String.format("switch To webview: { %s }", webview), "", getCaseId());
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(), String.format("switch To webview: { %s }", webview), fileName, getCaseId());
			Assertion.failCase();
		}
	}

	/**
	 * 是否在webview中，返回真，否则返回假
	 * 
	 * @return
	 */
	public boolean isBrowser() {
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(), "check current window is webview", "", getCaseId());
		return driver.isBrowser();
	}

	FindsByAndroidUIAutomator<AndroidElement> a = new FindsByAndroidUIAutomator<AndroidElement>() {

		@Override
		public AndroidElement findElement(String by, String using) {
			MobileBy.AndroidUIAutomator("");
			return (AndroidElement) driver.findElement(by, using);
		}

		@Override
		public List<AndroidElement> findElements(String by, String using) {
			return null;
		}
	};

	/**
	 * 能够完成下啦刷新 depreated
	 * 
	 * @param text
	 * @return
	 */
	public MobileElement scrollFresh(String text) {
		return (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector()"
				+ ".scrollable(true)).scrollIntoView(resourceId(\"android:id/vp_activity_main\")).scrollIntoView("
				+ "new UiSelector().text(\"" + text + "\"))"));
	}

	/**
	 * 向上滚动一屏,首屏刷新
	 */
	public void scrollUp() {
		int startX = getScreenSize().width / 2;
		int startY = 300;
		int endX = startX;
		int endY = getScreenSize().height;
		this.touchAction.longPress(startX, startY).moveTo(0, endY - 300).release();
		//this.touchAction.longPress(540,200).moveTo(540,1050).release();
		this.touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(),
				String.format("After scrollUp from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, endX, endY),
				fileName, getCaseId());
	}
	/**
	 * 向上滚动半屏,首屏刷新
	 */
	public void scrollUpHalf() {
		int startX = getScreenSize().width / 2;
		int startY = 300;
		int endX = startX;
		int endY = getScreenSize().height - 200;
		this.touchAction.longPress(startX, startY).moveTo(0, endY - 300).release();
		this.touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(),
				String.format("After scrollUp from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, endX, endY),
				fileName, getCaseId());
	}


	/**
	 * 向下滚动一屏
	 */
	public void scrollDown() {
		int startX = getScreenSize().width / 2;
		int startY = getScreenSize().height - 200;
		int endX = startX;
		int endY = 300;
		this.touchAction.longPress(startX, startY).moveTo(0, endY).release();
		this.touchAction.perform();
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(),
				String.format("After scrollDown from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, endX, endY),
				"", getCaseId());
	}

	/**
	 * 向下滚动半屏
	 */
	public void scrollDownHalf() {
		int startX = getScreenSize().width / 2;
		int startY = getScreenSize().height / 2;
		int endX = startX;
		int endY = 300;
		touchAction.longPress(startX, startY).moveTo(0, endY).release();
		//touchAction.longPress(startX, getScreenSize().height-200).moveTo(startX, endY).release();
		touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(),
				String.format("After scrollDown from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, endX, endY),
				fileName, getCaseId());
	}

	/**
	 * swipeLeft  
	 * @param ele 根据元素的中心坐标swipeLeft
	 */
	public void swipeLeft(WebElement ele) {
		Point p = ((MobileElement) ele).getCenter(); 
		int startX = p.x;
		int startY = p.y;
		int endY = startY;
		TouchAction ta = new TouchAction(driver);
		//ta.longPress(startX, startY).moveTo(-stepX, 0).release();
		//ta.longPress(1000,1600).moveTo(100, 1600).release();
		ta.longPress(startX,startY).moveTo(100, startY).release();
		ta.perform();
//		ta.longPress(1000,1600).moveTo(100, 1600).release();
//		ta.perform();
		
		// int startX = 10; //getScreenSize().width / 2;
		// int startY = getScreenSize().height - 200;
		// int endX = startX;
		// int endY = 300;
		// this.touchAction.longPress(startX, startY).moveTo(0, endY).release();
		// this.touchAction.perform();
		// this.touchAction.release();
		// Operation.insertData("insert into
		// executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
		// getSessionId(),
		// String.format("After swipeLeft from { startX=%d,startY=%d } scroll to
		// { endX=%d,endY=%d }", startX,
		// startY, endX, endY),
		// "", getCaseId());
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(),
				String.format("After swipeLeft from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, 100, endY),
				"", getCaseId());
	}
	
	/**
	 * swipeLeft  
	 * @param startY 根据y坐标
	 */
	public void swipeLeft(int startY) {
		int startX = getScreenSize().width-100;
		touchAction.longPress(startX,startY).moveTo(100, startY).release();
		touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(),
				String.format("After swipeLeft from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, 100, startY),
				fileName, getCaseId());
	}
	/**
	 * swipeRight
	 * @param startY 根据y坐标
	 */
	public void swipeRight(int startY) {
		int startX = getScreenSize().width-100;
		touchAction.longPress(100,startY).moveTo(startX, startY).release();
		touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(),
				String.format("After swipeRight from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", 100,
						startY, startX, startY),
				fileName, getCaseId());
	}


	public WebElement findElementByScroll(String text) {
		WebElement e = getElement("com.redstar.mainapp:id/tv_title");
		System.out.println(e.getText());
		return e;
	}

	/**
	 * 返回屏幕尺寸
	 * 
	 * @return
	 */
	public Dimension getScreenSize() {
		return ((AndroidDriver<?>) driver).manage().window().getSize();
		// System.out.println(d.height + " " + d.width);
	}

	// public Dimension getScrollXY(){
	// //getScreenSize().height
	// }
	public void scrollToEle(String text) {

		//
		// driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable("
		// +"new UiSelector().packageName(\"com.redstar.mainapp\")."
		// +"resourceId(\"com.redstar.mainapp:id/ptrLayout\").scrollable(true)).scrollForward()"));

		// driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable("
		// +"new UiSelector()."
		// +"resourceId(\"loadMoreRecyclerView\").scrollable(true)).scrollIntoView(new
		// UiSelector().text(\"好物清单\"))"));
		// com.redstar.mainapp:id/
		// loadMoreRecyclerView

		// .packageName(\"com.redstar.mainapp\")
		TouchAction ta = new TouchAction(driver);
		// ta.tap(540, 300).moveTo(0, 1050).release().perform();

		// ta.longPress(540, 300).moveTo(0, 1050).release().perform();
		ta.longPress(540, 1050).moveTo(0, 300).release();
		ta.perform();
		ta.longPress(540, 1050).moveTo(0, 300).release();
		ta.perform();

		// ta.press(540, 300).moveTo(0, 1050).release().perform();

	}

	/**
	 * 根据元素的坐标点击操作，webview中可以使用此方法
	 * 
	 * @param locator
	 */
	public void tap(String locator) {
		WebElement ele = getElement(locator);
		tap(ele);
	}

	/**
	 * 根据元素的坐标点击操作，webview中可以使用此方法
	 * 
	 * @param ele
	 */
	public void tap(WebElement ele) {
		Point p = ((MobileElement) ele).getCenter();
		System.out.println(p.x + " " + p.y + " " + ((MobileElement) ele).getSize().getHeight());
		if (driver.isBrowser()) {
			driver.context("NATIVE_APP");
		}
		touchAction.tap(p.x, p.y).perform().release();
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				getSessionId(), String.format("tap click element { %s } by location { x=%d,y=%d }", ele, p.x, p.y), "",
				getCaseId());
		if (!driver.isBrowser()) {
			switchToWebView();
		}
	}

	public void executeJs(String js) {
		((JavascriptExecutor) driver).executeScript(js);
	}

	public void test() {
		// System.out.println(((HasSessionDetails) driver).getPlatformName());
		// System.out.println(driver.getAutomationName());
		// HashMap<String, String> arguments = new HashMap<String, String>();
		// arguments.put("direction", "down");
		// driver.executeScript("mobile: scroll", arguments);
		// driver.executeScript("mobile: scroll", arguments);

		TouchAction touchAction = new TouchAction(driver);

		//
		// WebElement e = driver.findElementByAccessibilityId("好物清单");
		// //WebElement element = driver.findElement(By.id("my-id"));
		// Actions actions = new Actions(driver);
		// actions.moveToElement(e).release().perform();

		TouchAction swipe = new TouchAction(driver).press(540, 300).moveTo(540, 1050).release();
		swipe.perform();
	}

	/**
	 * 按下keycode
	 */
	public void pressKeyCode(int code) {
		((AndroidDriver<?>) driver).pressKeyCode(code);
	}

	/**
	 * 无submit的search
	 */
	public void searchSubmit() {
		try {
			((AndroidDriver<?>) driver).pressKeyCode(66);
			String fileName = TakeScreen.takeSreen(driver);
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(), String.format("After press key_code 66 { Enter }"), fileName, getCaseId());
		} catch (Throwable t) {
			String fileName = TakeScreen.takeSreen(driver);
			Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
					getSessionId(), String.format("After press key_code 66 { Enter }, %s", t.getMessage()), fileName,
					getCaseId());
		}
	}

	private void putCaseId() {
		System.setProperty(driver.getSessionId().toString(), String.valueOf(getCaseId()));
	}

	/**
	 * 切换到NATIVE_APP
	 */
	public void switchNative() {
		driver.context("NATIVE_APP");
	}

	/**
	 * 获取h5的源码
	 * 
	 * @return
	 */
	public String getPageSource() {
		return driver.getPageSource();
	}

	/**
	 * 获取当前的所有context
	 */
	public Set<String> getContext() {
		return driver.getContextHandles();
	}

	/**
	 * 获取当前的context
	 * 
	 * @return
	 */
	public String getCurrentContext() {
		return driver.getContext();
	}

	/**
	 * 退出driver
	 */
	public void quit() {
		driver.close();
		driver.quit();
		driver = null;
	}

	/**
	 * 返回app首屏Activity
	 */
	public void backHome() {

		// Activity home = new Activity("com.redstar.mainapp",
		// "com.redstar.mainapp.business.main.MainActivity");
		Activity home = new Activity("com.redstar.mainapp", ".business.LaunchActivity");
		((AndroidDriver<?>) driver).startActivity(home);
		putCaseId();
	}

	/**
	 * 获取当前driver的capability
	 * 
	 * @param key
	 * @return
	 */
	public Object getCapability(String key) {
		return driver.getCapabilities().getCapability(key);
	}

	/**
	 * 线程等待 second 秒
	 * 
	 * @param second
	 */
	public void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 线程等待 5 秒,默认
	 * 
	 * @param second
	 */
	public void sleep() {
		sleep(5);
	}

	public long getDisplayDensity() {
		return ((HasDeviceDetails) driver).getDisplayDensity();
		// ((AndroidDriver)driver).openNotifications();
	}

	public String currentActity() {
		return ((StartsActivity) driver).currentActivity(); // ((AndroidDriver<?>)
	}

	// public stopApp(){
	// ((AndroidDriver)driver).s
	// }
}
