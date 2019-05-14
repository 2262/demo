package com.hhh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SQL {
	/** 驱动字符串 */
	private static final String DRV = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/** 数据库连接字符串 */
	private static final String URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=work";
	/** 数据库登录用户名 */
	private static final String USER = "sa";
	/** 数据库登录密码 */
	private static final String PWD = "123654789";

	/**
	 * 返回一个数据库连接
	 * 
	 * @return
	 */
	public static Connection getConn() {
		Connection conn = null;
		try {
			Class.forName(DRV);
			conn = DriverManager.getConnection(URL, USER, PWD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	// 关闭数据库连接
	public static void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询员工全体信息（显示部门名称而不是部门编号
	 * 
	 * @throws SQLException
	 */
	public static void selectAll() throws SQLException {
		// 1.创建连接
		Connection conn = getConn();
		// 2.创建预编译语句对象
		String showAllInfoStr = "select * from Department,Employee where Department.did = Employee.did";
		PreparedStatement ps = conn.prepareStatement(showAllInfoStr);
		// 3.执行SQL语句
		ResultSet rs = ps.executeQuery();
		// 4.循环结果集
		// 显示所有员工信息
		while (rs.next()) {
			int eid = rs.getInt("eid");
			String name = rs.getString("ename");
			String bdate = rs.getString("bdate");
			String sex = rs.getString("sex");
			String startdata = rs.getString("startdata");
			double salary = rs.getDouble("salary");
			String dname = rs.getString("dname");

			System.out.printf("员工编号：%d，姓名：%s，出生日期：%s，性别：%s，入职日期：%s，工资：%f，所在部门名称：%s\n", eid, name, bdate, sex, startdata,
					salary, dname);
		}
		// 5.关闭连接，释放资源
		closeAll(rs, ps, conn);
	}

	/**
	 * 根据部门名称查部门did
	 * 
	 * @param dname 部门名称
	 * @return 部门id
	 * @throws SQLException
	 */
	public static int selectDidByDname(String dname) throws SQLException {
		// 要返回的部门id，0代表没找到
		int did = 0;
		// 1.创建连接
		Connection conn = getConn();
		// 2.创建预编译语句对象
		String showAllInfoStr = "select did from Department where dname=?";
		PreparedStatement ps = conn.prepareStatement(showAllInfoStr);

		// 填充数据
		ps.setString(1, dname);

		// 3.执行SQL语句
		ResultSet rs = ps.executeQuery();

		// 5.循环结果集 应该只有1条结果
		// 显示所有员工信息
		while (rs.next()) {
			did = rs.getInt("did");
			// 应该只有1行数据，找到就跳出
			break;
		}
		// 6.关闭连接，释放资源
		closeAll(rs, ps, conn);

		return did;

	}

	public static void delete() throws SQLException {
		// 1.创建连接
		Connection conn = getConn();
		// 2.创建预编译语句对象
		String showAllInfoStr = "delete from Employee where eid=?";
		PreparedStatement ps = conn.prepareStatement(showAllInfoStr);
		try {
			// 准备具体的数据，由键盘输入
			Scanner sc = new Scanner(System.in);
			int eid = sc.nextInt();
			System.out.printf("请输入需要删除的员工编号");
			ps.setLong(1, eid);
			int rs = ps.executeUpdate();
			if (rs > 0) {
				System.out.println("删除成功");
			} else {
				System.out.println("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		// 重新调用员工信息
		selectAll();
	}

	public static void update() throws SQLException {
		// 1.创建连接
		Connection conn = getConn();
		// 2.创建预编译语句对象

		String sql = "update Employee set ename=?,bdate=?,sex=?,startdata=?,salary=?, did = ? " + "where eid= ?";
		PreparedStatement ps = conn.prepareStatement(sql);

		try {
		// 准备具体的数据，由键盘输入

		
		Scanner sc = new Scanner(System.in);

		System.out.println("请输入要修改的员工");
		int eid = sc.nextInt();
		sc.nextLine();
		System.out.printf("请输入姓名：");
		String name = sc.nextLine();
		System.out.printf("请输入出生日期：");
		String bdate = sc.nextLine();
		System.out.printf("请输入性别：");
		String sex = sc.nextLine();
		System.out.printf("请输入入职日期：");
		String startdata = sc.nextLine();
		System.out.printf("请输入工资：");
		String salaryStr = sc.nextLine();
		double salary = Double.valueOf(salaryStr); // 字符串转double

		System.out.printf("请输入所在部门名称：");
		String dname = sc.nextLine();

		System.out.println("请等待...");

		// 先查询出该部门名称的did编号
		int did = selectDidByDname(dname);

		// 至此添加到员工信息表里的数据全齐了
		// 填充数据，给占位符填充值
		ps.setString(1, name);
		ps.setString(2, bdate);
		ps.setString(3, sex);
		ps.setString(4, startdata);
		ps.setDouble(5, salary);
		ps.setInt(6, did);
		ps.setLong(7, eid);

		// 3.执行sql语句
		int i = ps.executeUpdate();
		if (i > 0) {
			System.out.println("修改成功");
		} else {
			System.out.println("修改失败");
		}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 增加员工信息，输入部门名称而不是部门编号
	 * 
	 * @throws SQLException
	 */
	public static void insert() throws SQLException {
		// 1.创建连接
		Connection conn = getConn();
		// 2.创建预编译语句对象
		String sql = "insert into Employee(ename,bdate,sex,startdata,salary,did) values(?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);

		// 准备具体的数据，由键盘输入

		Scanner sc = new Scanner(System.in);

		System.out.printf("请输入姓名：");
		String name = sc.nextLine();
		System.out.printf("请输入出生日期：");
		String bdate = sc.nextLine();
		System.out.printf("请输入性别：");
		String sex = sc.nextLine();
		System.out.printf("请输入入职日期：");
		String startdata = sc.nextLine();
		System.out.printf("请输入工资：");
		String salaryStr = sc.nextLine();
		double salary = Double.valueOf(salaryStr); // 字符串转double

		System.out.printf("请输入所在部门名称：");
		String dname = sc.nextLine();

		System.out.println("请等待...");

		// 先查询出该部门名称的did编号
		int did = selectDidByDname(dname);

		// 至此添加到员工信息表里的数据全齐了
		// 填充数据，给占位符填充值
		ps.setString(1, name);
		ps.setString(2, bdate);
		ps.setString(3, sex);
		ps.setString(4, startdata);
		ps.setDouble(5, salary);
		ps.setInt(6, did);

		// 3.执行sql语句
		int i = ps.executeUpdate();
		// 插入成功
		if (i > 0) {
			System.out.println("插入成功");
		} else {
			System.out.println("插入失败");
		}

	}

}
