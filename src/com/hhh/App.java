package com.hhh;

import java.sql.SQLException;

public class App {

	public static void main(String[] args) throws SQLException {

		//1.��ʾ����Ա����Ϣ
		SQL.selectAll();
		
		//2.����Ա����Ϣ
		SQL.insert();
		
		//3.ɾ��Ա����Ϣ
		SQL.delete();
		
		//4.�޸�Ա����Ϣ
		SQL.update();
	}
}
