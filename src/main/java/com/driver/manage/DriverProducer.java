package com.driver.manage;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.annotations.DataProvider;

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
	 * @param caseName
	 * @param owner
	 * @param pName
	 * @return
	 */
	//@DataProvider(name = "create", parallel = true)
	public  Object[][] createDriver() {
		if (see == null) {
			synchronized ("mysee") {
				System.out.println("mysee is  init ....");
				see = new Object[AppiumServer.allServertemp.size()][1];
				AppiumDriverGenerate();
				System.out.println("driver all generated finish...");
			}
		} else {
			System.out.println("see is have found  return .....");
			// for (int i = 0; i < see.length; i++) {
			// if (see[i][0] == null) {
			// AppiumDriverManage(caseName, owner, pName);
			// } else {
			// break;
			// }
			// }
		}
		return see;
		// return new Object[][] { new Object[]{new
		// AppAndroidDriver().genDriver(a1)},
		// new Object[] { new AppAndroidDriver().genDriver(a2) } };
	}

	private void AppiumDriverGenerate() {
		int i = 0;
		for (Entry<String, String> es : AppiumServer.serverAndDevice.entrySet()) {
			// a1 = FileUtils.load().loadProperties();

			// a1.put(UIFlags.AndroidApk, config.get(UIFlags.AndroidApk));
			config.put(UIFlags.UDID, es.getValue());
			config.put(UIFlags.APPIUM_SERVER_URL, es.getKey());
			
			// AppiumDriver<MobileElement> driver = new
			// AppAndroidDriver().genDriver(a1);
			EntryPoint point = EntryFactory.genDriver(config);
			if (point != null) {
				// point.getSessionId();
				// StackTraceElement[] stacks = new Throwable().getStackTrace();
				// int stacksLen = stacks.length;
				// for (int i1 = 0; i1 < stacksLen; i1++) {
				// String className = stacks[i1].getClassName();
				// System.out.println(className);
				// }
			}
			System.out.println("entry url is ..."+point.getCapability("url"));
			Object[] a2 = new Object[] { point };
			see[i] = a2;
			i++;
		}
	}

	/**
	 * 
	 */
	public static void serviceManage() {
		if (AppiumServer.allServertemp.size() == 0) {
			System.out.println("serviceManage..");
			AppiumServer.launch();
		} else {
			System.out.println("直接返回");
			// for (AppiumDriverLocalService service : AppiumServer.allServer) {
			// if (service.isRunning()) {
			// urls = new HashMap<String, String>();
			// ArrayList<String> devices = AppiumServer.getDevices();
			// for (String device : devices) {
			// urls.put(service.getUrl().toString(), device);
			// }
			// retry = false;
			// } else {
			// retry = true;
			// System.out.println("服务已经不再运行中...");
			// urls = AppiumServer.launch();
			// break;
			// }
			// }
		}
	}

}
