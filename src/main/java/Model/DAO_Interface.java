package Model;

import java.sql.*;

//모든 DAO클래스들은 해당 인터페이스를 implements해 멤버변수를 공유한다.
public interface DAO_Interface {
	String driver = "com.mysql.jdbc.Driver"; 
	String url = "jdbc:mysql://localhost:3306/pc_order_db?serverTimezone=Asia/Seoul&useSSL=false";
	String user = "root";
	String pw = "1111";
}
