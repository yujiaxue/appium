package com.driver.manage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.android.uitest.driver.UIFlags;

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

	/**
	 * 读取文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static StringBuffer getFileContext(String filePath) {
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		String temp;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
	}

	/**
	 * 写文件
	 * 
	 * @param file
	 */
	public static void writeFile(String file, String content) {
		FileWriter fw;
		BufferedWriter bufw;
		try {
			fw = new FileWriter(file);
			bufw = new BufferedWriter(fw);
			bufw.write(content);
			bufw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写入nodeconfig文件
	 * 
	 * @param file
	 * @param browserName
	 * @param deviceName
	 * @param version
	 * @param hubport
	 * @param hubhost
	 */
	public static void generateJson(String file, String browserName, String deviceName, String version, int hubport,
			String hubhost) {
		StringBuffer sb = getFileContext(UIFlags.TEMPLATE);
		writeFile(file, String.format(sb.toString(), browserName, deviceName, version, hubport, hubhost));
	}

	public static void aa() throws IOException {
		FileReader fr = new FileReader("jsonTemplate.txt");
		BufferedReader br = new BufferedReader(fr);
		StringBuffer sb = new StringBuffer();
		String temp;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}
		System.out.println(
				String.format(sb.toString(), "RW5DOV7SSGUCUCQC", "RW5DOV7SSGUCUCQC", "5.0.2", 4444, "0.0.0.0"));
	}


	/**
	 * 获取文件绝对路径
	 * 
	 * @param jsonPath
	 * @return
	 */
	public static String getAbsolutePath(String jsonPath) {
		File f  = new File(jsonPath);
		return f.getAbsolutePath();
	}

}
