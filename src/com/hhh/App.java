package com.hhh;

import java.sql.SQLException;

public class App {

	public static void main(String[] args) throws SQLException {

		//1.显示所有员工信息
		SQL.selectAll();
		
		//2.增加员工信息
		SQL.insert();
		
		//3.删除员工信息
		SQL.delete();
		
		//4.修改员工信息
		SQL.update();
	}
}
