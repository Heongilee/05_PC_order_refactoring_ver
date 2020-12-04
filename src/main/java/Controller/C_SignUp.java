package Controller;

import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Model.Customer_DTO;
import Model.Customers_DAO;
import View.LoginView;
import View.SignUpView;
/*
 * View\SignUpView.java에서 달아준 이벤트 리스너가 
 * Controller\PCController.java에 있는 ActionPerformed를 받아 호출되는 메소드들.
 * */
public class C_SignUp implements I_Register {
	//싱글톤 객체를 불러옴.
	Customers_DAO dao = Customers_DAO.getInstance();
	
	//회원가입 양식에 맞게 유효성을 체크한다.
	@Override
	public void Valid_Check(int f) {
		switch(f) {
			case 0:	//아이디 중복 체크
				try {
					if(dao.Idselect(LoginView.getInstance().signUpView.IdField.getText())) {
						JOptionPane.showMessageDialog(null, "이미 사용중인 아이디입니다.");
					}
					else {
						JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			case 1:	//닉네임 중복 체크
				try {
					if(dao.Nickselect(LoginView.getInstance().signUpView.NameField.getText())) {
						
						JOptionPane.showMessageDialog(null, "이미 사용중인 닉네임입니다.");
					}
					else {
						JOptionPane.showMessageDialog(null, "사용 가능한 닉네입입니다.");
						
					}
						
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			case 2:	//이메일 중복 체크
				try {
					if(dao.Emailselect(LoginView.getInstance().signUpView.EmailField.getText())) {
						
						JOptionPane.showMessageDialog(null, "이미 사용중인 이메입니다.");
					}
					else {
						JOptionPane.showMessageDialog(null, "사용 가능한 이메일입니다.");
					}
						
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			default:
				break;
		}
	}
	
	//회원가입이 완료되면 필드에 작성된 값들을 dto객체에 담아 CUSTOMERS테이블에 튜플삽입을 진행한다.
	@Override
	public void Register_Complete() throws SQLException {
		Customer_DTO dto = new Customer_DTO(LoginView.getInstance().signUpView.IdField.getText(),
				LoginView.getInstance().signUpView.PassField.getText(),
				LoginView.getInstance().signUpView.NameField.getText(),
				LoginView.getInstance().signUpView.EmailField.getText());
		
		System.out.println("[1] : "+dto.toString());
		
		dao.CUSTOMERS_FUNC1(dto);
		dao.Renewal_cID();		//테이블 관리번호 갱신하는 함수.
	}
}
