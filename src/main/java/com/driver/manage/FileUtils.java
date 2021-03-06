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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import com.android.uitest.driver.UIFlags;

public class FileUtils {
	BufferedReader in = null;
	static FileUtils fu = null;
	static String config = null;
	static String pro_ip = "10.11.27.39";
	static String net_mark = "enp0s31f6";

	// public Map loadProperties(String filePath) {
	//
	// // InputStream a =
	// //
	// getClass().getClassLoader().getResourceAsStream("/Users/zhangfujun/Documents/NewLand/uitest/src/java/resources/devices.ini");
	// InputStream a =
	// getClass().getClassLoader().getResourceAsStream("config.properties");
	//
	// return null;
	// }

	public synchronized static FileUtils load() {
		if (fu == null) {
			fu = new FileUtils();
		} else {
			return fu;
		}
		if (pro_ip.equals(ipSwitch(net_mark))) {
			config = "config_pro.properties";
		} else {
			config = "config.properties";
		}
		System.out.println("配置文件是 " + config);
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

			InputStream a = getClass().getClassLoader().getResourceAsStream(config);
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
	 * 获取本机ip
	 */
	public static String ipSwitch1() {
		String ipAddr = "";
		String hostName = "";
		try {
			ipAddr = InetAddress.getLocalHost().getHostAddress().toString();
			hostName = InetAddress.getLocalHost().getHostName().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(ipAddr);
		InetAddress[] addrs = null;
		try {
			addrs = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (addrs.length > 0) {
			for (int i = 0; i < addrs.length; i++) {
				System.out.println(addrs[i]);
				// InetAddress address = addrs[i];
				// System.out.println("**********************");
				// System.out.println(address.getHostAddress());
				// if (address instanceof Inet6Address) {
				// System.out.println("true6");
				// } else if(address instanceof Inet4Address){
				// System.out.println("true4");
				// } else {
				// System.out.println("unknown");
				// }
			}
		}
		System.out.println("**********************");
		return ipAddr;
	}

	public static String ipSwitch(String net) {
		Enumeration<?> allNetInterfaces;
		Enumeration<?> addresses;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (netInterface.getName().equals(net)) {
					addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = (InetAddress) addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							return ip.getHostAddress();
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "";
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
		File f = new File(jsonPath);
		return f.getAbsolutePath();
	}

}
