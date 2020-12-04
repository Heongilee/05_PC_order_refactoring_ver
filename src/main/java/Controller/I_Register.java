package Controller;
import java.sql.SQLException;

import javax.swing.JButton;
import View.SignUpView;

//C_SignUp.java에서 구현할 인터페이스
public interface I_Register {
	void Valid_Check(int f);	//아이디, 닉네임, 이메일 항목이 유일한지 유효성 검사
						//(이미 DB스키마 구조에서 유효성 검사를 수행하기 때문에  SQLException의 try-catch블록을 잡으면 됨)
	void Register_Complete() throws SQLException;	//유효성 검사를 수행하면 회원가입 완료 메시지와 함께 CUSTOMERS테이블의 레코드로 저장함.
}
