package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import Controller.PCController;
import Controller.PCServer;
import View.GUIView;
import View.LoginView;

//DB의 ORDERS 테이블에 접근하기 위한 DAO 클래스
public class Orders_DAO implements DAO_Interface{
	private static Orders_DAO dao;
	private static GUIView GU = GUIView.getInstance();
	public static Connection conn;
	public static PreparedStatement pstmt;
	public static Statement stmt;
	public static ResultSet rs;
	public static ResultSet rs2;
	
	//JDBC 연결을 하기 위한 메소드
	public static Connection getConnection() throws Exception{
		conn = null;
		stmt = null;
		pstmt = null;
		rs = null;
		
		//1-1. DBMS에 맞게 드라이버를 로드.
		Class.forName(driver);
		
		//1-2. DriverManager를 Connection에 연결
		conn = DriverManager.getConnection(url, user, pw);
		
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
	private Orders_DAO() {}
	
	//4. Customers_DAO의 인스턴스를 얻는 방법은 getInstance() 하나 뿐이다.
	public static Orders_DAO getInstance() {
		if(dao == null) {
			dao = new Orders_DAO();
		}
		return dao;
	}
	
	// 주문목록에 상품을 추가시키는 메소드
	public int ORDERS_FUNC1(Product_DTO dto) {
		int cnt = 0;
		String cNAME = LoginView.getInstance().loginTextField.getText();
		try {
			String res = JOptionPane.showInputDialog(null, "개수를 입력하세요");
			if(res != null) {
					cnt = Integer.valueOf(res);
					String str = dto.getpNAME() +"\t"+dto.getpPrice()+"\t"+ cnt + "\n";
					
					// ORDERS 테이블에 튜플 삽입 메소드.
//					this.ORDERS_FUNC_1_1(cNAME, dto.getpNAME(), cnt);
					
					// ----------------------	주문목록에 디스플레이	---------------------------
					GUIView.getInstance().ta1.append(str);
					GUIView.getInstance().order_sum += cnt * dto.getpPrice();
					GUIView.getInstance().order_sum_label.setText(String.valueOf(GUIView.getInstance().order_sum));
			}
			else { // Cancel을 눌렀을 경우.
				JOptionPane.showMessageDialog(null, "상품 갯수는 반드시 입력되어야 합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "필드에 수량을 정수 형태로 다시 입력하세요!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		return cnt;
	}
	public void ORDERS_FUNC_1_1(String cNAME, String pNAME, int oCNT) {
		String sql = "INSERT INTO ORDERS(ORDERS.cNAME, ORDERS.pNAME, ORDERS.oCNT) VALUES (?, ?, ?)";
		String sql2 = "SET @CNT = 0";
		String sql3 = "UPDATE ORDERS SET ORDERS.oID = @CNT:=@CNT+1"; 
		int r;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cNAME);
			pstmt.setString(2, pNAME);
			pstmt.setInt(3, oCNT);
			r = pstmt.executeUpdate();
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql2);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql3);
			
			if(r > 0) {}
			else {}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Orders_DAO.closeJDBC(conn, pstmt, pstmt, rs);
		}
		
		return;
	}
	
	// 가장 주문량이 많은 상품 3개를 조회해서 이를 JList에 뿌려준다.
	public Vector<Product_DTO> SQL_BEST3() {
		String sql1 = "SELECT ORDERS.pNAME, SUM(ORDERS.oCNT)AS CNT FROM ORDERS GROUP BY ORDERS.pNAME ORDER BY CNT DESC";
		String sql2 = "SELECT * FROM PRODUCTS WHERE PRODUCTS.pNAME = ?";
		Vector<Product_DTO> menu = new Vector<Product_DTO>();
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql1);
			int i;
			Product_DTO dto;
			DefaultListModel<Product_DTO> listModel;
			
			for(i=0;(i<3) && (rs.next());i++) {
				String prodName = rs.getString(1);
				
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, prodName);
				rs2 = pstmt.executeQuery();
				
				while(rs2.next()) {
					dto = new Product_DTO(rs2.getInt(1), rs2.getString(2), rs2.getInt(3), rs2.getString(4), rs2.getString(5));
					
					menu.add(dto);
				}
			}
		} catch(SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Customers_DAO.closeJDBC(conn, pstmt, pstmt, rs);
		}
		
		return menu;
	}
	
	//JList에 BEST3 상품들을 가져오는 메소드
	public Product_DTO SQL_BEST3_1(String pNAME) {
		String sql = "SELECT * FROM PRODUCTS WHERE PRODUCTS.pNAME = ?";
		Product_DTO dto = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pNAME);
			rs = pstmt.executeQuery();
			
			while(rs.next())
				dto = new Product_DTO(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5));
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			Orders_DAO.closeJDBC(conn, pstmt, pstmt, rs);
		}
		
		return dto;
	}
	
	//주문 테이블에 있는 모든 레코드를 가져오는 메소드
	public void ORDERS_FUNC_2() {
		String sql = "SELECT * FROM ORDERS";
		PCServer.PCorder_list.removeAllElements();
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			Orders_DTO dto;
			while(rs.next()) {
				dto = new Orders_DTO(rs.getString(2), rs.getString(3), rs.getInt(4));
				
				PCServer.PCorder_list.add(dto);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			Orders_DAO.closeJDBC(conn, pstmt, pstmt, rs);
		}
		
		return;
	}
}
