package com.PCorder;

// import Controller.AccountChecker;
import Controller.C_AdminView;
import Controller.C_ProdManager;
import Controller.C_SignUp;
import Controller.C_UserView;
import Controller.C_login;
import Controller.PCController;
import Model.CustomersDao;
import Model.PCChatData;
import Model.ViewState;
import View.AdminView;
import View.CusManager;
import View.GUIView;
import View.LoginView;
import View.ProdManager;
import View.SignUpView;


public class App 
{
	public static PCController app;
	public static CustomersDao dao;
	public static void main(String[] args) {
		System.out.println("App.java에서 프로그램을 시작합니다...");
		
		////////////////////////////////////////////////////
		//			※	프로그램 실행 순서		※
		// -> Controller\PCServer.java
		// -> com.PCorder\App.java
		////////////////////////////////////////////////////
		
		LoginView loginView = LoginView.getInstance();
		SignUpView signUpView = SignUpView.getInstance();
		GUIView guiView = GUIView.getInstance();
		CusManager cusManager = CusManager.getInstance();
		ProdManager prodManager = ProdManager.getInstance();
		// AccountChecker accountChecker = AccountChecker.getInstance();
				
		//컨트롤러 객체 생성
		app = new PCController	(
								loginView, cusManager, 
								prodManager, guiView,
								new AdminView(), signUpView,
								new C_login(), new C_SignUp(),
								new C_ProdManager(), new C_UserView(),
								new C_AdminView(), new PCChatData(), 
								new PCChatData()
								);
		//고객 DAO객체 생성
		dao = CustomersDao.getInstance();

		// viewState 활성화
		ViewState.getInstance();
		
		app.appMain();
	}
}