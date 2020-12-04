package Controller;

import Model.ViewState;
import View.CusManager;
import View.LoginView;
import View.ProdManager;
/*
 * View\AdminView.java에서 달아준 이벤트 리스너가 
 * Controller\PCController.java에 있는 ActionPerformed를 받아 호출되는 메소드들.
 * */
public class C_AdminView implements I_AdminView{
	CusManager CM = CusManager.getInstance();
	LoginView LV = LoginView.getInstance();
	ProdManager PM = ProdManager.getInstance();
	ViewState viewState = ViewState.getInstance();

	ToolBar toolBar = ToolBar.getInstance();
	
	//고객관리 페이지로 이동한다.
	@Override
	public void Goto_CustomerManager() {
		// CM.setVisible(true);
		// LV.setVisible(false);
		LV.cardLayout.show(LV.window, "cusManager");
		LV.setSize(900, 700);
		LV.setLocationRelativeTo(null);
		LV.setResizable(false);		// 크기 고정
		toolBar.setVisibleToolBar(true);
		// LV.logoutBtn.setVisible(true);
	}
	
	//상품관리 페이지로 이동한다.
	@Override
	public void Goto_ProductManager() {
		// PM.setVisible(true);
		// LV.setVisible(false);
		LV.cardLayout.show(LV.window, "prodManager");
		LV.setSize(900, 700);
		toolBar.setVisibleToolBar(true);
		// LV.logoutBtn.setVisible(true);
	}
	
}
