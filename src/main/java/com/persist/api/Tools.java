package com.persist.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.testng.annotations.Test;

public class Tools {

	// preparedStatement
	public void prepra()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		JdbcConnection conn = new JdbcConnection();
		String sql = "insert into type values(?,?,?)";
		// PreparedStatement ps =conn.getConnection().prepareStatement(sql);

	}

	public void update(String sql, Object... args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		JdbcConnection conn = new JdbcConnection();
		// PreparedStatement ps =conn.getConnection().prepareStatement(sql);

		// for(int i=0;i<args.length;i++){
		// ps.setObject(i+1, args[i]);
		// }
		// ps.executeUpdate();

	}

	/**
	 * 事务的acid属性
	 * 
	 * 1.原子性 2.一致性 3.隔离性 4.持久性 必须在同一个连接上conn
	 */
	@Test
	public void aa() {
		JdbcConnection conn = new JdbcConnection();
		Connection c = conn.getConn();
		try {
			c.setAutoCommit(false);

			c.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				c.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 隔离级别 脏读，不可重复读，幻读 4种 read uncommitted 未提交 read commited 读已提交 oracle默认
	 * repeatable read 可重复读 mysql默认 serializable 串行化
	 */
	@Test
	public void bb() {
		JdbcConnection conn = new JdbcConnection();
		Connection c = (Connection) conn.getConn();
		try {
			c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量处理
	 * 
	 */
	public void cc() {
		JdbcConnection conn = new JdbcConnection();
		java.sql.Connection c = conn.getConn();
		try {
			PreparedStatement p = c.prepareStatement("");
			for (int i = 0; i < 10000; i++) {
					//....
				p.addBatch();
				if (10000 % 30 == 0) {
					p.executeBatch();
					p.clearBatch();
				}
			}

			if (1000 % 30 == 0) {
				p.executeBatch();
				p.clearBatch();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
