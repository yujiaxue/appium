package com.android.tools;

import java.io.IOException;

import com.android.uitest.driver.UIFlags;
import com.autotest.entrypoint.Context;

public class Tools extends Context{

	/**
	 * 安装apk
	 */
	public void installApk(){
		execute(String.format("%s install -r -g %s",config.get("adb"),config.get(UIFlags.AndroidApk)));
	}
	/**
	 * 执行命令
	 * @param command
	 */
	public void execute(String command){
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
