package com.driver.manage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class FileUtils {
	BufferedReader in = null;
	static FileUtils fu = null;

	public Map loadProperties(String filePath) {

		// InputStream a =
		// getClass().getClassLoader().getResourceAsStream("/Users/zhangfujun/Documents/NewLand/uitest/src/java/resources/devices.ini");
		InputStream a = getClass().getClassLoader().getResourceAsStream("config.properties");

		return null;
	}

	public synchronized static FileUtils load() {
		if (fu == null) {
			fu = new FileUtils();
		} else {
			return fu;
		}
		return fu;
	}

	public HashMap<String, String> loadProperties() {
		Properties p = new Properties();
		HashMap<String, String> pro = new HashMap<String, String>();
		// File f = new File("config.properties");
		try {
			// if (f.exists() & f.isFile()) {f
			// p.load(new InputStreamReader(new FileInputStream(new
			// File("/Users/zhangfujun/Documents/NewLand/uitest/src/java/resources/devices.properties")),
			// "utf-8"));
			InputStream a = getClass().getClassLoader().getResourceAsStream("config.properties");
			// p.load(new InputStreamReader(new FileInputStream(new
			// File("config.properties").getAbsolutePath())));
			p.load(a);
			// }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Entry<Object, Object> es : p.entrySet()) {
			pro.put(es.getKey().toString(), es.getValue().toString());
		}
		// for (Object a : p.keySet()) {
		// pro.put(a.toString(), p.getProperty((String) a));
		// }
		return pro;
	}

	public static void main(String[] args) {
		// loadProperties();
	}

}
