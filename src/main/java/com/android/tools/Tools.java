package com.android.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.android.uitest.driver.UIFlags;
import com.autotest.entrypoint.Context;

public class Tools extends Context {

	public static String url_apk = "http://www.qmbeta.com/applist/android/%s.apk";
	public static String localPath = "~/tempapk";

	/**
	 * 安装apk
	 * 
	 * @param uid
	 */
	public static void pre_AUTO(String uid) {
		if (uid.equals("nothing") || uid.equals("") || uid.equals(null) || uid.length() < 5) {
			System.out.println("不采用远端apk包，uid is " + uid);
			installApk();
		} else {
			String url = String.format(url_apk, uid);
			System.out.println("采用远端apk包,文件名是 " + uid + ".apk");
			if (checkDownload(url)) {
				wget(url);
				installApk(uid + ".apk");
			} else {
				System.out.println("远端包不存在 uid is " + String.format(url_apk, uid));
			}
		}
	}

	/**
	 * 覆盖安装apk 同时授予所有权限，仅适用于android6.0+
	 */
	public static void installApk() {
		execute(String.format("%s install -r -g %s", config.get("adb"), config.get(UIFlags.AndroidApk)));
	}

	/**
	 * 覆盖安装~/tempapk目录下的apk文件 同时授予所有权限，仅适用于android6.0+
	 * 
	 * @param uid
	 *            文件名 带后缀.apk
	 */
	public static void installApk(String uid) {
		execute(String.format("%s install -r -g %s", config.get("adb"), localPath + "/" + uid));
	}

	/**
	 * wget 远程文件到~/tempapk目录
	 */
	public static String wget(String url) {
		execute(config.get("wget") + "  " + url + " -P   " + localPath);
		return "";
	}

	public static boolean checkDownload(String url) {
		ArrayList<String> message = execute(config.get("wget") + "  --spider  " + url);
		String mess = message.get(message.size() - 2);
		if (mess.contains("Remote file exists") || mess.contains("存在远程文件")) {
			return true;
		}
		return false;
	}
	

	/**
	 * 执行命令
	 * 
	 * @param command
	 * @return 输出返回
	 */
	public static ArrayList<String> execute(String command) {
		Process p = null;
		ArrayList<String> message = new ArrayList<String>();
		try {
			p = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = p.getErrorStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		System.out.println("执行命令  " + command);
		try {
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				message.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	public static void main(String[] args) {
		System.out.println(config.get("wget"));
		System.out.println(checkDownload("http://www.qmbeta.com/applist/android/98aff041c4f54fc8a75b5195a92b0fd1.apk"));
	}

}
