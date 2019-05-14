package com.hhh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SQL {
	/** �����ַ��� */
	private static final String DRV = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/** ���ݿ������ַ��� */
	private static final String URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=work";
	/** ���ݿ��¼�û��� */
	private static final String USER = "sa";
	/** ���ݿ��¼���� */
	private static final String PWD = "123654789";

	/**
	 * ����һ�����ݿ�����
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

	// �ر����ݿ�����
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
	 * ��ѯԱ��ȫ����Ϣ����ʾ�������ƶ����ǲ��ű��
	 * 
	 * @throws SQLException
	 */
	public static void selectAll() throws SQLException {
		// 1.��������
		Connection conn = getConn();
		// 2.����Ԥ����������
		String showAllInfoStr = "select * from Department,Employee where Department.did = Employee.did";
		PreparedStatement ps = conn.prepareStatement(showAllInfoStr);
		// 3.ִ��SQL���
		ResultSet rs = ps.executeQuery();
		// 4.ѭ�������
		// ��ʾ����Ա����Ϣ
		while (rs.next()) {
			int eid = rs.getInt("eid");
			String name = rs.getString("ename");
			String bdate = rs.getString("bdate");
			String sex = rs.getString("sex");
			String startdata = rs.getString("startdata");
			double salary = rs.getDouble("salary");
			String dname = rs.getString("dname");

			System.out.printf("Ա����ţ�%d��������%s���������ڣ�%s���Ա�%s����ְ���ڣ�%s�����ʣ�%f�����ڲ������ƣ�%s\n", eid, name, bdate, sex, startdata,
					salary, dname);
		}
		// 5.�ر����ӣ��ͷ���Դ
		closeAll(rs, ps, conn);
	}

	/**
	 * ���ݲ������Ʋ鲿��did
	 * 
	 * @param dname ��������
	 * @return ����id
	 * @throws SQLException
	 */
	public static int selectDidByDname(String dname) throws SQLException {
		// Ҫ���صĲ���id��0����û�ҵ�
		int did = 0;
		// 1.��������
		Connection conn = getConn();
		// 2.����Ԥ����������
		String showAllInfoStr = "select did from Department where dname=?";
		PreparedStatement ps = conn.prepareStatement(showAllInfoStr);

		// �������
		ps.setString(1, dname);

		// 3.ִ��SQL���
		ResultSet rs = ps.executeQuery();

		// 5.ѭ������� Ӧ��ֻ��1�����
		// ��ʾ����Ա����Ϣ
		while (rs.next()) {
			did = rs.getInt("did");
			// Ӧ��ֻ��1�����ݣ��ҵ�������
			break;
		}
		// 6.�ر����ӣ��ͷ���Դ
		closeAll(rs, ps, conn);

		return did;

	}

	public static void delete() throws SQLException {
		// 1.��������
		Connection conn = getConn();
		// 2.����Ԥ����������
		String showAllInfoStr = "delete from Employee where eid=?";
		PreparedStatement ps = conn.prepareStatement(showAllInfoStr);
		try {
			// ׼����������ݣ��ɼ�������
			Scanner sc = new Scanner(System.in);
			int eid = sc.nextInt();
			System.out.printf("��������Ҫɾ����Ա�����");
			ps.setLong(1, eid);
			int rs = ps.executeUpdate();
			if (rs > 0) {
				System.out.println("ɾ���ɹ�");
			} else {
				System.out.println("ɾ��ʧ��");
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

		// ���µ���Ա����Ϣ
		selectAll();
	}

	public static void update() throws SQLException {
		// 1.��������
		Connection conn = getConn();
		// 2.����Ԥ����������

		String sql = "update Employee set ename=?,bdate=?,sex=?,startdata=?,salary=?, did = ? " + "where eid= ?";
		PreparedStatement ps = conn.prepareStatement(sql);

		try {
		// ׼����������ݣ��ɼ�������

		
		Scanner sc = new Scanner(System.in);

		System.out.println("������Ҫ�޸ĵ�Ա��");
		int eid = sc.nextInt();
		sc.nextLine();
		System.out.printf("������������");
		String name = sc.nextLine();
		System.out.printf("������������ڣ�");
		String bdate = sc.nextLine();
		System.out.printf("�������Ա�");
		String sex = sc.nextLine();
		System.out.printf("��������ְ���ڣ�");
		String startdata = sc.nextLine();
		System.out.printf("�����빤�ʣ�");
		String salaryStr = sc.nextLine();
		double salary = Double.valueOf(salaryStr); // �ַ���תdouble

		System.out.printf("���������ڲ������ƣ�");
		String dname = sc.nextLine();

		System.out.println("��ȴ�...");

		// �Ȳ�ѯ���ò������Ƶ�did���
		int did = selectDidByDname(dname);

		// ������ӵ�Ա����Ϣ���������ȫ����
		// ������ݣ���ռλ�����ֵ
		ps.setString(1, name);
		ps.setString(2, bdate);
		ps.setString(3, sex);
		ps.setString(4, startdata);
		ps.setDouble(5, salary);
		ps.setInt(6, did);
		ps.setLong(7, eid);

		// 3.ִ��sql���
		int i = ps.executeUpdate();
		if (i > 0) {
			System.out.println("�޸ĳɹ�");
		} else {
			System.out.println("�޸�ʧ��");
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
	 * ����Ա����Ϣ�����벿�����ƶ����ǲ��ű��
	 * 
	 * @throws SQLException
	 */
	public static void insert() throws SQLException {
		// 1.��������
		Connection conn = getConn();
		// 2.����Ԥ����������
		String sql = "insert into Employee(ename,bdate,sex,startdata,salary,did) values(?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);

		// ׼����������ݣ��ɼ�������

		Scanner sc = new Scanner(System.in);

		System.out.printf("������������");
		String name = sc.nextLine();
		System.out.printf("������������ڣ�");
		String bdate = sc.nextLine();
		System.out.printf("�������Ա�");
		String sex = sc.nextLine();
		System.out.printf("��������ְ���ڣ�");
		String startdata = sc.nextLine();
		System.out.printf("�����빤�ʣ�");
		String salaryStr = sc.nextLine();
		double salary = Double.valueOf(salaryStr); // �ַ���תdouble

		System.out.printf("���������ڲ������ƣ�");
		String dname = sc.nextLine();

		System.out.println("��ȴ�...");

		// �Ȳ�ѯ���ò������Ƶ�did���
		int did = selectDidByDname(dname);

		// ������ӵ�Ա����Ϣ���������ȫ����
		// ������ݣ���ռλ�����ֵ
		ps.setString(1, name);
		ps.setString(2, bdate);
		ps.setString(3, sex);
		ps.setString(4, startdata);
		ps.setDouble(5, salary);
		ps.setInt(6, did);

		// 3.ִ��sql���
		int i = ps.executeUpdate();
		// ����ɹ�
		if (i > 0) {
			System.out.println("����ɹ�");
		} else {
			System.out.println("����ʧ��");
		}

	}

}
