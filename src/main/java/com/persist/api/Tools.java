package com.persist.api;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Tools {

	//preparedStatement
	public void prepra() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException{
		JdbcConnection conn= new JdbcConnection();
		String sql  ="insert into type values(?,?,?)";
		//PreparedStatement ps =conn.getConnection().prepareStatement(sql);
		
		
	}
	public void update(String sql,Object ...args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException{
		JdbcConnection conn= new JdbcConnection();
		//PreparedStatement ps =conn.getConnection().prepareStatement(sql);

//		for(int i=0;i<args.length;i++){
//			ps.setObject(i+1, args[i]);
//		}
//		ps.executeUpdate();
		
	}
}
