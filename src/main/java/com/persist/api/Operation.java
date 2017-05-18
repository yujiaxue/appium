package com.persist.api;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class Operation {
	static Connection conn = null;
	Statement st = null;
	static PreparedStatement ps = null;
	static ResultSet rs = null;

	public static int insertData(String sql, Object... args) {
		conn = JdbcConnection.getConn();
		ResultSet results;
		int num = -1;
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			ps.executeUpdate();
			results = ps.getGeneratedKeys();

			if (results.next()) {
				num = results.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return num;
	}

	public <T> T get(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		conn = JdbcConnection.getConn();
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();

			if (rs.next()) {
				HashMap<String, Object> column = new HashMap<String, Object>();

				ResultSetMetaData m = rs.getMetaData();
				int count = m.getColumnCount();
				for (int i = 0; i < count; i++) {
					String columnLabel = m.getColumnLabel(i + 1);
					Object columnValue = rs.getObject(i + 1);
					column.put(columnLabel, columnValue);
				}
				entity = clazz.newInstance();

				for (Map.Entry<String, Object> entry : column.entrySet()) {
					String propertyName = entry.getKey();
					Object value = entry.getValue();
					// BeanUtils.setProperty(bean, name, value);
					// ReflectionTools.setFieldValue(entity, propertyName,
					// value);
				}
			}

		} catch (Exception e) {

		}
		return entity;
	}

	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {

		List<T> list = new ArrayList<>();
		conn = JdbcConnection.getConn();
		List<Map<String, Object>> values = new ArrayList<>();
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			HashMap<String, Object> column = null;
			while (rs.next()) {
				column = new HashMap<String, Object>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Object columnValue = rs.getObject(i + 1);
					column.put(columnLabel, columnValue);
				}
				values.add(column);
			}

		} catch (Exception e) {

		}

		T bean = null;
		if (values.size() > 0) {
			for (Map<String, Object> map : values) {
				bean = clazz.newInstance();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					String propertyName = entry.getKey();
					Object value = entry.getValue();

					BeanUtils.setProperty(bean, propertyName, value);
					// ReflectionTools.setFieldValue(entity, propertyName,
					// value);
				}
				list.add(bean);
			}
		}

		return list;
	}

	public <E> E getForValue(String sql, Object... args) {
		
		return null;
	}

	public static Map<String, String> getData(String sql, Object... args) {
		conn = JdbcConnection.getConn();
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData m = rs.getMetaData();
			int count = m.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= count; i++) {
					data.put(m.getColumnName(i), rs.getObject(i).toString());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void destroy() {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		insertData("insert into apptest (executeId,status) values(?,?)", 222, "finish");

		// Map<String, String> id = Operation.getData("select executeId from
		// apptest where status=?", Status.DONE.getArgument()); //
		// point.getSessionId();
		// System.out.println(id.get("executeId"));
	}

}
