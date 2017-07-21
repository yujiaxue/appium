package com.autotest.entrypoint;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.InvalidCoordinatesException;

import com.autotest.Listener.TakeScreen;
import com.autotest.assertion.Assertion;
import com.persist.ExecuteDetail;

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

public class EntryPoint implements IEntryPoint {
	AppiumDriver<?> driver = null;
	String sessionId = null;
	String caseId = null;
	TouchAction touchAction = null;
	String device = null;
	int casePass = 0;
	int caseFail = 0;

	// appium bug 临时解决
	int x = 0;
	int y = 0;
	int pre_x = 0;
	int pre_y = 0;

	public int getCasePass() {
		return casePass;
	}

	public void setCasePass(int casePass) {
		this.casePass = casePass;
	}

	public int getCaseFail() {
		return caseFail;
	}

	public void setCaseFail(int caseFail) {
		this.caseFail = caseFail;
	}

	// int executeId =0;

	// public int getExecuteId() {
	// return executeId;
	// }
	//
	// public void setExecuteId(int executeId) {
	// this.executeId = executeId;
	// }

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
		System.out.println("设置caseId ..." + this.caseId);
	}

	public EntryPoint(AppiumDriver<WebElement> driver, String device) {
		this.driver = driver;
		this.device = device;
		touchAction = new TouchAction(this.driver);
		this.sessionId = this.driver.getSessionId().toString();
		Assertion.setAttr(this.driver, this.sessionId, device);
	}

	public String getSessionId() {
		return driver.getSessionId().toString();
	}

	/**
	 * 获取界面元素支持 id,class,xpath,accessbility id
	 * 
	 * @param locator
	 * @return
	 */
	public WebElement getElement(String locator, String log) {
		putCaseId();
		By type = TypeCheck.getLocatorType(locator);
		WebElement ele = null;
		try {
			int i = 0;
			ele = driver.findElement(type);
			while (ele == null && i < 3) {
				ele = driver.findElement(type);
				i++;
			}
		} catch (NoSuchElementException e) {
			Assertion.noElementFail(locator, e.getMessage(), String.valueOf(getCaseId()));
		} catch (Throwable e) {
			System.out.println(e.getCause() + "   " + e.getClass().getSimpleName());
			e.printStackTrace();
			// 用例失败标记，
			// 脚本中断退出，未知异常
			ExecuteDetail.save(sessionId, String.format("CaseFail:没有找到对应元素 { %s ,%s}", locator, e.getMessage()), "",
					caseId, device);
			Assertion.failCase();
		}
		return ele;
	}

	/**
	 * 获取界面元素支持 id,class,xpath,accessbility id
	 * 
	 * @param locator
	 * @return
	 */
	public WebElement getElement(String locator) {
		return getElement(locator, String.format("查找元素{%s}", locator));
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
			ExecuteDetail.save(sessionId, String.format("getElements by { %s } ", locator), "", caseId, device);
		} catch (NoSuchElementException e) {
			Assertion.noElementFail(locator, e.getMessage(), String.valueOf(getCaseId()));
		} catch (Exception e) {
			e.printStackTrace();
			ExecuteDetail.save(sessionId, String.format("getElements by { %s } fail { %s }", locator, e.getMessage()),
					"", caseId, device);
			Assertion.failCase();
		}
		return eles;
	}

	/**
	 * 获取元素个数
	 */
	public int getElementsCount(String locator) {
		List<WebElement> elements = getElements(locator);
		return elements.size();
	}

	/**
	 * 验证元素
	 * 
	 * @param locator
	 * @return
	 */
	public WebElement checkElement(String locator, String log) {
		By type = TypeCheck.getLocatorType(locator);
		WebElement ele = null;
		try {
			ele = driver.findElement(type);
		} catch (NoSuchElementException e) {
			System.out.println(locator + "该元素没有找到，不抛出异常");
			// Assertion.noElementFail(locator, e.getMessage().toString(),
			// getCaseId());
		} catch (Throwable e) {
			e.printStackTrace();
			// Assertion.failCase();
		}
		return ele;
	}

	/**
	 * 验证元素
	 * 
	 * @param locator
	 * @return
	 */
	public WebElement checkElement(String locator) {
		return checkElement(locator, String.format("检查元素{%s}是否能被找到，找到即返回,否则返回null", locator));
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
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("Click Element { %s }, { %s } on device { %s }", locator, log, device), fileName,
					caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("Click Element { %s }, { %s } on device { %s },{ %s }", locator,
					log, device, e.getMessage()), fileName, caseId, device);
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

	/**
	 * 点击元素，传入Element
	 */
	public void click(WebElement element, String log) {
		try {
			element.click();
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("点击元素 { %s }, { %s } on device { %s },{ %s }", element, log, device, e.getMessage()),
					fileName, caseId, device);
			Assertion.failCase();
		}
	}

	public void click(WebElement element) {
		click(element, "点击元素" + element);
	}

	public void enterText(String locator, String text, String log) {
		WebElement ele = getElement(locator);
		try {
			ele.sendKeys(text);
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("enter { %s } to Element { %s }, { %s }", text, locator, log),
					fileName, caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("enter { %s } to Element { %s }, { %s }", text, locator, e.getMessage()), fileName,
					caseId, device);
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
	public String getText(String locator, String log) {
		WebElement ele = getElement(locator);
		String text = null;
		try {
			text = ele.getText();
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("获取元素文本从 { %s }", locator), fileName, caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("获取元素文本从 { %s }", locator), fileName, caseId, device);
			Assertion.failCase();
		}
		return text;
	}

	public String getText(String locator) {
		return getText(locator, String.format("获取元素文本 {%s}", locator));
	}

	/**
	 * 获取视图content-desc的内容
	 * 
	 * @param locator
	 * @return
	 */
	public String getName(String locator) {
		return getName(locator, "");
	}

	/**
	 * 获取视图content-desc的内容
	 * 
	 * @param locator
	 * @return
	 */
	public String getName(String locator, String log) {
		WebElement ele = getElement(locator);
		String text = null;
		try {
			text = ele.getAttribute("name");
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("Success:获取content-desc属性值从 { %s },%s", locator, log), fileName,
					caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("Fail:获取content-desc属性值从 { %s },%s", locator, log), fileName,
					caseId, device);
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
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("switch To webview: { %s }", webview), fileName, caseId,
					device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("switch To webview: { %s }", webview), fileName, caseId,
					device);
			Assertion.failCase();
		}
	}

	/**
	 * 是否在webview中，返回真，否则返回假
	 * 
	 * @return
	 */
	public boolean isBrowser() {
		String fileName = TakeScreen.takeSreen(driver);
		ExecuteDetail.save(sessionId, "check current window is webview", fileName, caseId, device);
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
	 * 能够完成下啦刷新 depreated 暂不用
	 * 
	 * @param text
	 * @return
	 */
	@Deprecated
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
		// this.touchAction.longPress(540,200).moveTo(540,1050).release();
		this.touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		ExecuteDetail.save(sessionId,
				String.format("After scrollUp from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, endX, endY),
				fileName, caseId, device);
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
		ExecuteDetail.save(sessionId,
				String.format("After scrollUp from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, endX, endY),
				fileName, caseId, device);
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
		String fileName = TakeScreen.takeSreen(driver);
		ExecuteDetail.save(sessionId,
				String.format("After scrollDown from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, endX, endY),
				fileName, caseId, device);
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
		// touchAction.longPress(startX,
		// getScreenSize().height-200).moveTo(startX, endY).release();
		touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		ExecuteDetail.save(sessionId,
				String.format("After scrollDown from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, endX, endY),
				fileName, caseId, device);
	}

	/**
	 * swipeLeft
	 * 
	 * @param ele
	 *            根据元素的中心坐标swipeLeft
	 */
	public void swipeLeft(WebElement ele) {
		Point p = ((MobileElement) ele).getCenter();
		int startX = p.x;
		int startY = p.y;
		int endY = startY;
		TouchAction ta = new TouchAction(driver);
		// ta.longPress(startX, startY).moveTo(-stepX, 0).release();
		// ta.longPress(1000,1600).moveTo(100, 1600).release();
		ta.longPress(startX, startY).moveTo(100, startY).release();
		ta.perform();
		// ta.longPress(1000,1600).moveTo(100, 1600).release();
		// ta.perform();

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
		String fileName = TakeScreen.takeSreen(driver);
		ExecuteDetail.save(sessionId,
				String.format("After swipeLeft from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, 100, endY),
				fileName, caseId, device);
	}

	/**
	 * swipeLeft
	 * 
	 * @param startY
	 *            根据y坐标
	 */
	public void swipeLeft(int startY) {
		int startX = getScreenSize().width - 100;
		touchAction.longPress(startX, startY).moveTo(100, startY).release();
		touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		ExecuteDetail.save(sessionId,
				String.format("After swipeLeft from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", startX,
						startY, 100, startY),
				fileName, caseId, device);
	}

	/**
	 * swipeRight
	 * 
	 * @param startY
	 *            根据y坐标
	 */
	public void swipeRight(int startY) {
		int startX = getScreenSize().width - 100;
		touchAction.longPress(100, startY).moveTo(startX, startY).release();
		touchAction.perform();
		String fileName = TakeScreen.takeSreen(driver);
		ExecuteDetail.save(sessionId,
				String.format("After swipeRight from { startX=%d,startY=%d } scroll to { endX=%d,endY=%d }", 100,
						startY, startX, startY),
				fileName, caseId, device);
	}

	/**
	 * 暂不用
	 * 
	 * @param text
	 * @return
	 */
	@Deprecated
	public WebElement findElementByScroll(String text) {
		WebElement e = getElement("com.redstar.mainapp:id/tv_title");
		System.out.println(e.getText());
		return e;
	}

	/**
	 * 返回屏幕尺寸
	 * 
	 * @return Dimension
	 */
	public Dimension getScreenSize() {
		return ((AndroidDriver<?>) driver).manage().window().getSize();
		// System.out.println(d.height + " " + d.width);
	}

	// public Dimension getScrollXY(){
	// //getScreenSize().height
	// }
	/**
	 * 暂不用
	 */
	@Deprecated
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
	 * tap 坐标
	 * 
	 * @param x
	 * @param y
	 */
	public void tap(int x, int y) {
		tap(x, y, "");
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param log
	 */
	public void tap(int x, int y, String log) {
		if (driver.isBrowser()) {
			driver.context("NATIVE_APP");
		}
		String fileName = TakeScreen.takeSreen(driver);
		try {
			touchAction.tap(x, y).perform();
			ExecuteDetail.save(sessionId, String.format("Success:tap by xy { x=%d,y=%d };%s", x, y, log), fileName,
					caseId, device);
		} catch (Exception e) {
			ExecuteDetail.save(sessionId, String.format("Fail:tap by xy { x=%d,y=%d };%s", x, y, log), fileName, caseId,
					device);
			Assertion.failCase();
		}
		if (!driver.isBrowser()) {
			switchToWebView();
		}

	}

	/**
	 * 
	 * @param locator
	 */
	public void press(String locator, String log) {
		WebElement ele = getElement(locator);
		String fileName = TakeScreen.takeSreen(driver);
		try {
			touchAction.press(ele).release().perform();
			ExecuteDetail.save(sessionId, String.format("Success:press element { %s } ;%s", ele, log), fileName, caseId,
					device);
		} catch (Exception e) {
			ExecuteDetail.save(sessionId,
					String.format("Success:press element { %s } ;%s;%s", ele, log, e.getMessage()), fileName, caseId,
					device);
		}
	}

	public void press(int x, int y) {
		touchAction.press(x, y);
	}

	/**
	 * 根据元素的坐标点击操作，webview中可以使用此方法
	 * 
	 * @param locator
	 */
	public void tap(String locator) {
		WebElement ele = getElement(locator);
		tap(ele, "");
	}

	/**
	 * 
	 * @param locator
	 * @param log
	 */
	public void tap(String locator, String log) {
		WebElement ele = getElement(locator);
		tap(ele, log);
	}

	/**
	 * 根据元素的坐标点击操作，webview中可以使用此方法
	 * 
	 * @param ele
	 * @param log
	 */
	public void tap(WebElement ele, String log) {

		System.out.println("==================" + ele + "==================");
		Point p = ((MobileElement) ele).getCenter();
		x = p.x;
		y = p.y;

		if (driver.isBrowser()) {
			driver.context("NATIVE_APP");
		}
		try {
			touchAction.tap(p.x, p.y).perform(); // .release()
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("Success:tap click element { %s } by location { x=%d,y=%d };%s", ele, p.x, p.y, log),
					fileName, caseId, device);
		} catch (InvalidCoordinatesException e) {
			try {
				// appium bug 临时解决
				System.out.println(e.getMessage());
				int tempx = p.x - pre_x;
				int tempy = p.y - pre_y;
				touchAction.tap(tempx, tempy).perform(); // .release()

				String fileName = TakeScreen.takeSreen(driver);
				ExecuteDetail.save(sessionId,
						String.format("Success:Retry tap click element { %s } by location { x=%d,y=%d };%s", ele, tempx,
								tempy, log),
						fileName, caseId, device);
			} catch (Exception ex) {
				System.out.println(e.getClass());
				System.out.println("------------------------------------------------");
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("tap exception start====================================");
			System.out.println(e.getClass());
			System.out.println("------------------------------------------------");
			System.out.println(e.getCause());
			System.out.println("------------------------------------------------");
			System.out.println(e.getMessage());
			System.out.println("tap exception end====================================");
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("Fail:tap click element { %s } by location { x=%d,y=%d };%s", ele, p.x, p.y, log),
					fileName, caseId, device);
			Assertion.failCase();
		}

		if (!driver.isBrowser()) {
			switchToWebView();
		}
		pre_x = x;
		pre_y = y;
		x = 0;
		y = 0;
	}

	/**
	 * 执行js
	 */
	public void executeJs(String js) {
		((JavascriptExecutor) driver).executeScript(js);
	}

	/**
	 * 按下keycode
	 */
	public void pressKeyCode(int code) {
		try {
			((AndroidDriver<?>) driver).pressKeyCode(code);
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("Success:After press key_code %d { Enter }", code), fileName,
					caseId, device);
		} catch (Throwable t) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("Fail:After press key_code %d { Enter }, %s", t.getMessage()),
					fileName, caseId, device);
		}
	}

	/**
	 * 无submit的search
	 */
	public void searchSubmit() {
		pressKeyCode(66);
	}

	private void putCaseId() {
		System.setProperty(driver.getSessionId().toString(), String.valueOf(getCaseId()));
	}

	/**
	 * 切换到NATIVE_APP
	 */
	public void switchNative() {
		try {
			driver.context("NATIVE_APP");
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, "switch context NATIVE_APP", fileName, caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("switch context NATIVE_APP { %s }", e.getMessage()), fileName,
					caseId, device);
		}
	}

	/**
	 * 获取h5的源码
	 * 
	 * @return
	 */
	public String getPageSource() {
		String pagesource = null;
		try {
			pagesource = driver.getPageSource();
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("getPageSource { %s }", "success"), fileName, caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("getPageSource { %s }", "fail"), fileName, caseId, device);
		}
		return pagesource;
	}

	/**
	 * 获取当前的所有context
	 */
	public Set<String> getContext() {
		Set<String> allContext = null;
		try {
			allContext = driver.getContextHandles();
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("get All Context { %s }", allContext.toString()), fileName,
					caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("get All Context { %s },{ %s }", allContext.toString(), e.getMessage()), fileName,
					caseId, device);
		}
		return allContext;
	}

	/**
	 * 获取当前的context
	 * 
	 * @return
	 */
	public String getCurrentContext() {
		String context = null;
		try {
			context = driver.getContext();
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("current Context { %s }", context), fileName, caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("current Context { %s }, { %s }", context, e.getMessage()),
					fileName, caseId, device);
		}
		return context;
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
		String packageTo = "com.redstar.mainapp";
		String activity = ".business.LaunchActivity";
		try {
			Activity home = new Activity(packageTo, activity);
			((AndroidDriver<?>) driver).startActivity(home);
			putCaseId();
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("启动activity { %s , %s }", packageTo, activity), fileName,
					caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("启动activity { %s , %s };exception { %s }", packageTo, activity, e.getMessage()),
					fileName, caseId, device);
		}

	}

	/**
	 * 硬件返回上一级页面
	 */
	public void back_Menu() {
		try {
			pressKeyCode(4);
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("Success:返回上一级页面"), fileName, caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("Fail:返回上一级页面"), fileName, caseId, device);
		}

	}

	/**
	 * 获取当前driver的capability
	 * 
	 * @param key
	 * @return
	 */
	public Object getCapability(String key) {
		Object cap = null;
		try {
			cap = driver.getCapabilities().getCapability(key);
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("获取capability key= { %s },value={ %s }", key, cap), fileName,
					caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("获取capability key= { %s },value={ %s };exception { %s }", key, cap, e.getMessage()),
					fileName, caseId, device);
		}
		return cap;
	}

	/**
	 * 获取设备id
	 * 
	 * @return
	 */
	public String getDeviceId() {
		return getCapability("udid").toString();
	}

	/**
	 * 线程等待 second 秒
	 * 
	 * @param second
	 */
	public void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("线程等待 { %s 秒 }", second), fileName, caseId, device);
		} catch (InterruptedException e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("线程等待 { %s 秒 }，exception {%s}", second, e.getMessage()),
					fileName, caseId, device);
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

	/**
	 * 获取状态栏
	 * 
	 * @return
	 */
	public Map<String, String> getbars() {
		return ((HasDeviceDetails) driver).getSystemBars();
	}

	/**
	 * 获取当前设备的显示密度
	 * 
	 * @return
	 */
	public long getDisplayDensity() {

		long density = 0;
		try {
			density = ((HasDeviceDetails) driver).getDisplayDensity();
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("getDisplayDensity { %s }", density), fileName, caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("getDisplayDensity { %s }", density), fileName, caseId, device);
		}
		return density;
	}

	/**
	 * 获取当前activity
	 * 
	 * @return
	 */
	public String currentActity() {
		String currentActivity = null;
		try {
			currentActivity = ((StartsActivity) driver).currentActivity();
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("currentActivity { %s }", currentActivity), fileName, caseId,
					device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("currentActivity { %s }", currentActivity), fileName, caseId,
					device);
		}
		return currentActivity;
	}

	/**
	 * 获取元素属性
	 * 
	 * @param locator
	 * @param attribute
	 * @param log
	 * @return
	 */
	public String getAttribute(String locator, String attribute, String log) {
		WebElement ele = getElement(locator);
		String attr = null;
		try {
			attr = ele.getAttribute(attribute);
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId, String.format("Success: getAttribute %s of %s,%s", attribute, locator, log),
					fileName, caseId, device);
		} catch (Exception e) {
			String fileName = TakeScreen.takeSreen(driver);
			ExecuteDetail.save(sessionId,
					String.format("Fail: getAttribute %s of %s,%s;%s", attribute, locator, log, e.getMessage()),
					fileName, caseId, device);
		}
		return attr;
	}

	/**
	 * 获取元素属性
	 * 
	 * @param locator
	 * @param attribute
	 * @return
	 */
	public String getAttribute(String locator, String attribute) {
		return getAttribute(locator, attribute, "");
	}

	/**
	 * 获取input的value
	 * 
	 * @param locator
	 * @param log
	 * @return
	 */
	public String getInputText(String locator, String log) {
		return getAttribute(locator, "value", log);
	}

	public String getInputText(String locator) {
		return getAttribute(locator, "value", "");
	}

}
