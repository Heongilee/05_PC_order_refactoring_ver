package Controller;

//C_login.java에서 구현할 인터페이스
public interface I_Login {
	// 리스너 핸들러에서 호출시킬 메소드
	void Submit();		//리스너 핸들러에서 JButton 로그인을 눌렀을 때 사용자 뷰/관리자 뷰로 이동.  
	void Register();	//리스너 핸들러에서 JButton 회원가입을 눌렀을 때 회원가입 페이지로 이동
	boolean Mode_Check(String id, String pw, int flag);	//관리자 모드인지 사용자 모드인지 체크하는 함수.
}