package Controller;

import javax.swing.JOptionPane;

import Model.Customers_DAO;

/*
 * View\LoginView.java에서 달아준 이벤트 리스너가 
 * Controller\PCController.java에 있는 ActionPerformed를 받아 호출되는 메소드들.
 * */
public class C_login implements I_Login{
	//Customers_DAO 싱글톤 객체를 불러옴.
	Customers_DAO dao = Customers_DAO.getInstance();
	
	@Override
	public void Submit() {	}

	@Override
	public void Register() {	}

	//사용자가 모드에 맞는 로그인을 시도하는지 확인하는 메소드.
	@Override
	public boolean Mode_Check(String id, String pw, int flag) {
		boolean RET = false;
		if(dao.Try_Login(id, pw, flag)) { //
			RET = true;
			
			switch(flag) 
			{
				case 0:	//(관리자 cMODE : 0)
					System.out.println("[1] : C_login()\\(관리자 cMODE : 0)");
					break;
				case 1:	//(사용자 cMODE : 1) -> 클라이언트 ArrayList에 추가...
					System.out.println("[1] : C_login()\\(사용자 cMODE : 1)");
					break;
					default:
						break;
			}
		}
		else {
			RET = false;
		}
		
		return RET;
	}
}
