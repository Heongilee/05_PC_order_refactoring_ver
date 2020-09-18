package Model;

import java.sql.*;
import javax.swing.JOptionPane;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import View.LoginView;
import View.SignUpView;

//CUSTOMERS 테이블에 접근하기 위한 DAO 클래스.
public class Customers_DAO implements DAO_Interface{
   //1. 바로 메모리 할당하는 방법을 사용.
   private static Customers_DAO dao;
   private static LoginView LV = LoginView.getInstance();
   public static Connection conn;
   public static PreparedStatement pstmt;
   public static Statement stmt;
   public static ResultSet rs;
   
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
   private Customers_DAO() {}
   
   //4. Customers_DAO의 인스턴스를 얻는 방법은 getInstance() 하나 뿐이다.
   public static Customers_DAO getInstance() {
      if(dao == null) {
         dao = new Customers_DAO();
      }
      return dao;
   }
   
   //유효성 검사가 끝난 회원은 회원가입 양식에 따라 dto객체를 만들어서 INSERT문을 수행한다.
   public void CUSTOMERS_FUNC1(Customer_DTO dto) throws SQLException {
      System.out.println(dto.toString());
      
      try {
         conn = getConnection();
         String sql = "INSERT INTO CUSTOMERS(CUSTOMERS.cNAME, CUSTOMERS.cPW, CUSTOMERS.cNICKNAME, CUSTOMERS.cEMAIL) VALUES (?, ?, ?, ?)";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, dto.getCname());
         pstmt.setString(2, dto.getCpw());
         pstmt.setString(3, dto.getCnickname());
         pstmt.setString(4, dto.getCemail());
         int r = pstmt.executeUpdate();
         if(r > 0) {   //삽입 성공
            JOptionPane.showMessageDialog(null, "회원가입 완료되었습니다.");
            
         }
      } catch(MySQLIntegrityConstraintViolationException e3) {
         JOptionPane.showMessageDialog(null, "ERROR : 회원가입에 실패했습니다. 초기화면으로 돌아갑니다.");
      } catch (SQLException e) {
         e.printStackTrace();
         System.out.println("ERROR : 회원가입에 실패했습니다. 초기화면으로 돌아갑니다.");
      } catch(Exception e1){
         e1.printStackTrace();
         
      } finally {
         Customers_DAO.closeJDBC(conn, pstmt, stmt, rs);
         LV.cardLayout.show(LV.window, "layer");
         
         /*	회원가입 뷰의 모든 필드값 초기화	*/
         LV.signUpView.IdField.setText("");
         LV.signUpView.PassField.setText("");
         LV.signUpView.NameField.setText("");
         LV.signUpView.EmailField.setText("");
      }
      
      return;
   }
   
   //삽입및 삭제가 발생하면 이 메소드를 호출해서 cID(PK, Auto-Increment)속성 값들을 갱신할 필요가 있다.
   public void Renewal_cID() {
      String sql1 = "SET @CNT = 0";
      String sql2 = "UPDATE CUSTOMERS SET CUSTOMERS.cID = @CNT:=@CNT+1";
      int r;
      try {
         conn = getConnection();
         
         stmt = conn.createStatement();
         r = stmt.executeUpdate(sql1);
         
         stmt = conn.createStatement();
         r = stmt.executeUpdate(sql2);
         
      } catch(SQLException e1) {
         e1.printStackTrace();
      } catch (Exception e2) {
         e2.printStackTrace();
      } finally {
         Customers_DAO.closeJDBC(conn, pstmt, stmt, rs);
      }
   }
   
   //id 중복체크 메소드
   public boolean Idselect(String id) {
      boolean ok = false;
      try {
         conn = getConnection();
         String sql = "SELECT cNAME FROM CUSTOMERS WHERE cNAME= ?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         rs = pstmt.executeQuery();
         
         if(rs.next())
            ok = true;
      }
      catch(Exception e) {
         e.printStackTrace();
      } finally {
         Customers_DAO.closeJDBC(conn, pstmt, stmt, rs);
      }
      
      return ok;
   }
   
   //닉네임 중복체크 메소드
   public boolean Nickselect(String Nick) {
      boolean ok = false;
      try {
         conn = getConnection();
         String sql = "SELECT cNICKNAME FROM CUSTOMERS WHERE cNICKNAME= ?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, Nick);
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
            ok = true;
         }
      }
      catch(Exception e) {
         e.printStackTrace();
      } finally {
         Customers_DAO.closeJDBC(conn, pstmt, stmt, rs);
      }
      return ok;
   }
   
   //email 중복체크 메소드
   public boolean Emailselect(String email) {
      boolean ok = false;
      try {
         conn = getConnection();
         String sql = "SELECT cEMAIL FROM CUSTOMERS WHERE cEMAIL= ?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, email);
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
            ok = true;
         }
      }
      catch(Exception e) {
         e.printStackTrace();
      } finally {
         Customers_DAO.closeJDBC(conn, pstmt, stmt, rs);
      }
      return ok;
   }
   
   // 로그인 시도하는 메소드
      public boolean Try_Login(String id, String pw, int f) {
         boolean RET = false;
         String sql = "SELECT cNAME, cPW, cMODE FROM CUSTOMERS WHERE cNAME = ?";
         try {
            conn = getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) { // 튜플이 존재 -> 아이디 조회 성공!
               if ((rs.getString(2).equals(pw)) && (rs.getInt(3) == f) && get_check(id)) { // 비밀번호와 모드 접근이 일치하다면...
                  RET = true;
               } else if (!(rs.getString(2).equals(pw))) { // 비밀번호가 다르다면...
                  JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                  RET = false;
               } else if (rs.getInt(3) != f) { // 비밀번호는 맞는데 모드 접근이 다르다면...
                  JOptionPane.showMessageDialog(null, "모드 접근이 잘못됐습니다.", "ACCESS DENIED", JOptionPane.ERROR_MESSAGE);
                  RET = false;
               } else {

               }
            } else { // 튜플이 없음 -> 에러 메시지 출력(없는 아이디 입니다...)
               JOptionPane.showMessageDialog(null, "없는 아이디 입니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
               RET = false;
            }
         } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "SQLException()이 발생했습니다.", "Exception", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            Customers_DAO.closeJDBC(conn, pstmt, stmt, rs);
         }

         return RET;
      }
      
      public String getCash(String id) {// 포인트 가져오는 메소드
         String result = "";
         String sql = "SELECT cBALANCE FROM CUSTOMERS WHERE cNAME = ?";
         try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
               result = Integer.toString(rs.getInt(1));
            }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            Customers_DAO.closeJDBC(conn, pstmt, pstmt, rs);
         }
         return result;
      }
      
      public boolean Cash_Check(String id, int cash) {// 합계의 결제를 눌렀을 때 적용
         boolean ok = false;
         String sql = "SELECT cBALANCE FROM CUSTOMERS WHERE cNAME = ?";
         try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {// 아이디에 해당한 잔액 조회 성공
               if (cash <= rs.getInt(1)) {// 잔액이 더 많을때
                  int value = (rs.getInt(1) - cash);
                  sql = "UPDATE CUSTOMERS SET cBALANCE = ? WHERE cNAME = ?";
                  pstmt = conn.prepareStatement(sql);
                  pstmt.setInt(1, value);
                  pstmt.setString(2, id);
                  pstmt.executeUpdate();
                  ok = true;
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            Customers_DAO.closeJDBC(conn, pstmt, pstmt, rs);
         }
         return ok;
      }
      
      public void make_check(String id) {//check값을 변경해준다
         boolean ok;
         String sql = "UPDATE CUSTOMERS SET CUSTOMERS.CHECK = ? WHERE cNAME = ?";
         try {
            ok = get_check(id);//check를 가져와서
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            if(ok)//이미 로그인 되있는 것
               pstmt.setBoolean(1, true);
            else
               pstmt.setBoolean(1, false);
            pstmt.setString(2, id);
            int r = pstmt.executeUpdate();
            
            if(r > 0) {
               
            }
         }
         catch(Exception e) {
            e.printStackTrace();
         } finally {
            Customers_DAO.closeJDBC(conn, pstmt, stmt, rs);
         }
      }

      public boolean get_check(String id) {//check값을 가져온다
         boolean ok = false;
         String sql = "SELECT CUSTOMERS.CHECK FROM CUSTOMERS WHERE CUSTOMERS.cNAME= ?";
         try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
               ok = !(rs.getBoolean(1));//false일때 입장 true일때 로그인 실패
            }
            else {
            	JOptionPane.showMessageDialog(null, "없는 아이디 입니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
         }
         catch(Exception e) {
            e.printStackTrace();
         }
         finally {
            Customers_DAO.closeJDBC(conn, pstmt, stmt, rs);
         }
         return ok;
      }
}