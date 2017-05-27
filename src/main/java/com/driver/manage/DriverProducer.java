package com.driver.manage;

import java.util.HashMap;
import java.util.Map.Entry;

import org.testng.Assert;

import com.android.uitest.driver.UIFlags;
import com.autotest.entrypoint.EntryPoint;
import com.autotest.factory.EntryFactory;
import com.autotest.service.AppiumServer;

public class DriverProducer {
	static Object[][] see = null;
	public static HashMap<String, String> config = null;
	static DriverProducer dp = null;

	public synchronized static DriverProducer genInstance(HashMap<String, String> cf) {
		if (dp == null) {
			config = cf;
			dp = new DriverProducer();
			return dp;
		} else {
			return dp;
		}
	}

	/**
	 * 创建AppiumDriver并返回，
	 * 
	 * @return
	 */
	public Object[][] createDriver() {
		if (see == null) {
			synchronized ("mysee") {
				System.out.println("mysee is  init ....");
				see = new Object[AppiumServer.allServertemp.size()][1];
				AppiumDriverGenerate();
				System.out.println("driver all generated finish...");
			}
		} else {
			System.out.println("see is have found  return .....");
		}
		return see;
	}

	/**
	 * 生成 appiumdriver
	 */
	private void AppiumDriverGenerate() {
		int i = 0;
		for (Entry<String, String> es : AppiumServer.serverAndDevice.entrySet()) {
			config.put(UIFlags.UDID, es.getValue());
			config.put(UIFlags.APPIUM_SERVER_URL, es.getKey());
			EntryPoint point = EntryFactory.genDriver(config);
			if (point == null) {
				Assert.fail();
			}
			Object[] a2 = new Object[] { point };
			see[i] = a2;
			i++;
		}
	}

	/**
	 * appium server manage
	 */
	public static void serviceManage() {
		if (AppiumServer.allServertemp.size() == 0) {
			AppiumServer.launch();
		} else {
			System.out.println("服务已经启动，直接返回");
		}
	}

}
