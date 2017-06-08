package com.autotest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;

import com.android.uitest.driver.UIFlags;
import com.autotest.entrypoint.Context;
import com.driver.manage.FileUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

/**
 * /usr/local/lib/node_modules/appium/build/lib/main.js
 * 
 * @author zhangfujun appium server appium -p 4492 -bp 2251 -U TA00405HRT
 */
public class AppiumServer extends Context {

	public static ArrayList<String> devices = new ArrayList<String>();
	public static List<AppiumDriverLocalService> allServer = new ArrayList<AppiumDriverLocalService>();
	public static HashMap<String, String> serverAndDevice = new HashMap<String, String>();
	public static List<String> allServertemp = new ArrayList<String>();

	static private int hubport = 4444;
	static private String hubhost = "0.0.0.0";

	/**
	 * 获取设备列表
	 * 
	 * @return
	 */
	public static String getDeivcesJson() {
		JsonArray json = new JsonArray();
		for (String device : devices) {
			json.add(device);
		}
		return json.toString();
	}
	/**
	 * chage hub
	 */
	public static void hub(){
		try{
			hubhost = config.get(UIFlags.HUBHOST);
			hubport = Integer.parseInt(config.get(UIFlags.HUBPORT));
			System.out.println(String.format("Hub host is change : %s %d ",hubhost,hubport));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 启动appiumServer服务
	 */
	public static void startServer() {
		hub();
		AppiumDriverLocalService service;
		for (String android : devices) {
			service = new AppiumServiceBuilder().withIPAddress("127.0.0.1").usingAnyFreePort()
					.withArgument(GeneralServerFlag.SESSION_OVERRIDE).build();
			allServertemp.add(service.getUrl().toString());

			// http://127.0.0.1:30580/wd/hub
			String tempurl = service.getUrl().toString();
			String port = service.getUrl().toString().substring(tempurl.lastIndexOf(":") + 1, tempurl.indexOf("/wd"));
			String fileName = "nodeconfig_" + android + ".json";
			FileUtils.generateJson(fileName, android, android, "5.0.2", hubport, hubhost);
			AppiumServerThread ast = new AppiumServerThread(port, fileName,config);
			// "/Users/zhangfujun/Documents/NewLand/hxUIAuto/src/main/resources/nodeconfig"
			// + logname + ".json"
			ast.start();
			serverAndDevice.put(tempurl, android);
		}
	}

	/**
	 * 只有一台设备可以使用此方法
	 * 
	 * @param logname
	 */
	public static void startServerOnlyOneDevice(int logname) {
		AppiumDriverLocalService service;
		service = new AppiumServiceBuilder().withIPAddress("127.0.0.1").usingAnyFreePort()
				.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
				// .withLogFile(new File(UIFlags.LOG_PATH.concat(logname)))
				// .withAppiumJS(new
				// File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
				// .withArgument(GeneralServerFlag.CONFIGURATION_FILE,
				// "/Users/zhangfujun/Documents/NewLand/uitest/src/main/resources/nodeconfig1.json")
				.build();
		allServertemp.add(service.getUrl().toString());

		service.start();
		if (service.isRunning()) {
			allServertemp.add(service.getUrl().toString());
		}
		// System.out.println(service.getUrl().toString() +
		// service.isRunning());
	}

	/**
	 * 根据设备数目启动对应数目的服务，
	 */
	public static void launch() {
		// System.setProperty(AppiumServiceBuilder.NODE_PATH,
		// "/usr/local/Cellar/node/6.6.0");
		//
		// System.setProperty(AppiumServiceBuilder.APPIUM_PATH,
		// "/Users/zhangfujun/Applications/aa/Appium.app");
		int serviceNumber = getDevices().size();
		if (0 == serviceNumber) {
			System.out.println("no devices connected ..........");
			Assert.fail();
		}
		// System.out.println("serviceNumber ..." + serviceNumber);
		// for (int i = 0; i < serviceNumber; i++) {
		// startServer("server" + i + ".log");
		startServer();
		// serverAndDevice.put(allServer.get(i).getUrl().toString(),
		// devices.get(i));
		// }
	}

	/**
	 * 获取已连接设备并返回设备名
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<String> getDevices() {
		try {
			/// Users/zhangfujun/Library/Android/sdk/platform-tools/adb
			Process p = Runtime.getRuntime().exec(config.get("adb") + " devices");
			BufferedReader readStdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String temp = "";
			while ((temp = readStdout.readLine()) != null) {
				if (temp.endsWith("device")) {
					devices.add(temp.substring(0, temp.indexOf("\t")));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
		return devices;
	}

	public static void launchByDefault() {
		AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
		service.start();
		if (service.isRunning()) {
			System.out.println(service.getUrl() + "isRunning ...");
			allServer.add(service);
		}
	}

	/**
	 * 停止服务，杀掉Chrome driver
	 */
	public static void stopService() {
		for (AppiumDriverLocalService s : allServer) {
			if (s != null) {
				System.out.println(s.getUrl() + "isStoping");
				s.stop();
				System.out.println(s.getUrl() + "isStoped");
			}
		}
		killChrome();
	}

	private static void killChrome() {
		try {
			Runtime.getRuntime().exec("killall chromedriver");
			Runtime.getRuntime().exec("killall node");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class AppiumServerThread extends Thread {
	String port;
	String jsonPath;
	HashMap<String, String> config;

	AppiumServerThread(String port, String jsonPath, HashMap<String, String> config) {
		this.port = port;
		this.jsonPath = FileUtils.getAbsolutePath(jsonPath);
		this.config = config;
	}

	public void run() {
		InputStream in = null;
		try {
			Process pro = Runtime.getRuntime().exec(new String[] { config.get("node"),
					config.get("appium"), "-p", port, "--session-override", "--nodeconfig", jsonPath });// "/Users/zhangfujun/Documents/NewLand/uitest/src/main/resources/nodeconfig1.json"

			// Process pro = Runtime.getRuntime().exec(new String[] {
			// "/usr/local/Cellar/node/6.6.0/bin/node",
			// "/usr/local/bin/appium", "-p", port, "--session-override",
			// "--nodeconfig", jsonPath });//
			// "/Users/zhangfujun/Documents/NewLand/uitest/src/main/resources/nodeconfig1.json"
			pro.waitFor(30, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("启动成功" + port);
	}
}
