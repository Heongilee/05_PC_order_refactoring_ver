package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import View.GUIView;
import View.LoginView;
import View.ProdManager;

//DB의 PRODUCTS테이블에 접근하기 위한 DAO 클래스.
public class Product_DAO implements DAO_Interface{
	public static ProdManager PM = ProdManager.getInstance();
	private static Product_DAO dao = Product_DAO.getInstance();
	public static Connection conn;
	public static PreparedStatement pstmt;
	public static Statement stmt;
	public static ResultSet rs;
	
	
	//JDBC 연결을 하기 위한 메소드
	public static Connection getConnection() throws Exception{
		conn = null;
		stmt = null;
		pstmt = null;
		rs = null;
		
		Class.forName(driver);	//1-1. DBMS에 맞게 드라이버를 로드.
		conn = DriverManager.getConnection(url, user, pw);	//1-2. DriverManager를 Connection에 연결
		return conn;
	}
	
	//2. Connection, PrepareStatement, ResultSet 모두 close한다.
	public static void closeJDBC(Connection conn, PreparedStatement pstmt, Statement stmt, ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//3. 외부의 인스턴스화를 막는다.
	private Product_DAO() {}
	
	//4. Customers_DAO의 인스턴스를 얻는 방법은 getInstance() 하나 뿐이다.
	public static Product_DAO getInstance() {
		if(dao == null) {
			dao = new Product_DAO();
		}
		return dao;
	}
	
	//PRODUCTS 테이블의 모든 튜플을 가져오는 메소드.
	public void SQL_SHOW(boolean f, int id) {
		String sql = "SELECT * FROM PRODUCTS";
		String sql2 = "SELECT pNAME, pPRICE, pMANUF FROM PRODUCTS WHERE pID = ?";
		try {
			conn = getConnection();
			
			if(f == true) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				Product_DTO dto = new Product_DTO();
				
				String str = "";
				PM.ta.setText("관리번호\t상품명\t단가\t제조사\n");
				PM.prodCombo.removeAllItems();
				PM.prodCombo.addItem("전체");	//"전체"
				while(rs.next()) {
					dto.setpID(rs.getInt(1));
					str += String.valueOf(dto.getpID());
					str += "\t";
					dto.setpNAME(rs.getString(2));
					str += dto.getpNAME();
					str += "\t";
					dto.setpPrice(rs.getInt(3));
					str += String.valueOf(dto.getpPrice());
					str += "\t";
					//pTYPE은 생략
					dto.setpMANUF(rs.getString(5));
					str += dto.getpMANUF();
					str += "\n";
					
					PM.v.add(dto); //DTO 객체를 벡터에 추가.
					PM.prodCombo.addItem(String.valueOf(dto.getpID()));
				}
				PM.ta.append(str);
			}
			else {
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, id);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					PM.prodtf[0].setText(rs.getString(1));
					PM.prodtf[1].setText(String.valueOf(rs.getInt(2)));
					PM.prodtf[2].setText(rs.getString(3));
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch(Exception e2) {
			e2.printStackTrace();
		} finally {
			Product_DAO.closeJDBC(conn, pstmt, pstmt, rs);
		}
		
		return;
	}
	
	// 삽입 && 수정
	public void SQL_INSERT(boolean f, int id) {
		String sql = "INSERT INTO PRODUCTS(PRODUCTS.pNAME, PRODUCTS.pPRICE, PRODUCTS.pTYPE, PRODUCTS.pMANUF) VALUES(?, ?, ?, ?)";
		String sql2 = "UPDATE PRODUCTS SET pNAME = ?, pPRICE = ?, pTYPE = ?, pMANUF = ? WHERE pID = ?";
		try {
			conn = getConnection();
			if(f == true) { // 삽입
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, PM.prodtf[0].getText());							// 제품 이름
				pstmt.setInt(2, Integer.valueOf(PM.prodtf[1].getText()));			// 제품 가격
				pstmt.setString(3, String.valueOf(PM.prodType.getSelectedItem()));	// 제품 타입
				pstmt.setString(4, PM.prodtf[2].getText());							// 제품 제조사
				int r = pstmt.executeUpdate();
				
				stmt = conn.createStatement();
				stmt.executeUpdate("SET @CNT = 0");
				stmt = conn.createStatement();
				stmt.executeUpdate("UPDATE PRODUCTS SET PRODUCTS.pID = @CNT:=@CNT+1");
				
				if(r > 0) {
					PM.stateText.setText("## 메시지 : 해당 제품의 삽입이 정상적으로 처리되었습니다.");
				}
				else {
					PM.stateText.setText("## 메시지 : 제품을 추가하는데 실패했습니다.");
				}
			}
			else {			// 수정
				pstmt = conn.prepareStatement(sql2);
				System.out.println("[0] : "+PM.prodtf[0].getText()+", [1] : "+ PM.prodtf[1].getText() +", [2] : "+ PM.prodtf[2].getText());
				pstmt.setString(1, PM.prodtf[0].getText());
				pstmt.setInt(2, Integer.valueOf(PM.prodtf[1].getText()));
				pstmt.setString(3, String.valueOf(PM.prodType.getSelectedItem()));
				pstmt.setString(4, PM.prodtf[2].getText());
				pstmt.setInt(5, id);
				
				stmt = conn.createStatement();
				stmt.executeUpdate("SET @CNT = 0");
				stmt = conn.createStatement();
				stmt.executeUpdate("UPDATE PRODUCTS SET PRODUCTS.pID = @CNT:=@CNT+1");
				
				int r = pstmt.executeUpdate();

				if(r > 0) {
					PM.stateText.setText("## 메시지 : 해당 제품의 수정이 정상적으로 처리되었습니다.");
				}
				else {
					PM.stateText.setText("## 메시지 : 제품정보를 업데이트하는데 실패했습니다.");
				}
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			Product_DAO.closeJDBC(conn, pstmt, pstmt, rs);
		}
		
	}
	
	//삭제
	public void SQL_DELETE(String id) {
		String sql = "DELETE FROM PRODUCTS WHERE pID = ?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.valueOf(id));
			int r = pstmt.executeUpdate();
			
			stmt = conn.createStatement();
			stmt.executeUpdate("SET @CNT = 0");
			stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE PRODUCTS SET PRODUCTS.pID = @CNT:=@CNT+1");
			
			if(r > 0) {
				PM.stateText.setText("## 메시지 : 해당 제품의 삭제가 정상적으로 처리되었습니다.");
			}
			else {
				PM.stateText.setText("## 메시지 : 해당 제품을 삭제하는데 실패했습니다.");
			}
		} catch(MySQLIntegrityConstraintViolationException e3){
			PM.stateText.setText("## 메시지 : 이 상품을 주문한 고객이 있어 상품을 삭제할 수 없습니다.");
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch(Exception e1) {
			e1.printStackTrace();
		} finally {
			Product_DAO.closeJDBC(conn, pstmt, pstmt, rs);
		}
		return;
	}
	
	// 타입별로 메뉴 리스트를 가져오는 메소드
	public Vector<Product_DTO> USERVIEW_FUNC1(String type) {
		Vector<Product_DTO> menu = new Vector<Product_DTO>();
		String sql = "SELECT * FROM PRODUCTS WHERE pTYPE = ?";// 타입에 맞는 것을 다 가져와서

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, type);
			rs = pstmt.executeQuery();

			Product_DTO dto;

			while (rs.next()) {
				dto = new Product_DTO(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5));
				
				menu.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Customers_DAO.closeJDBC(conn, pstmt, pstmt, rs);
		}
		
		return menu;
	}

}