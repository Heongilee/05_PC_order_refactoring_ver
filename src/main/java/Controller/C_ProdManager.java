package Controller;

import Model.Product_DAO;
/*
 * View\ProdManager.java에서 달아준 이벤트 리스너가 
 * Controller\PCController.java에 있는 ActionPerformed를 받아 호출되는 메소드들.
 * */
public class C_ProdManager implements I_ProdManager{
	//Product_DAO와 ProdManager 각각의 싱글톤 객체를 불러 옴.
	Product_DAO dao = Product_DAO.getInstance();
	View.ProdManager PM = View.ProdManager.getInstance();
	
	//상품 목록 조회
	@Override
	public void show() {
		if(PM.prodCombo.getSelectedIndex() == 0) {	//전체 상품 조회
			dao.SQL_SHOW(true, 0);
			PM.stateText.setText("## 메시지 : 모든 상품 목록을 조회합니다...");
		}
		else {	//특정 상품 조회
			String id = (String) PM.prodCombo.getSelectedItem();
			dao.SQL_SHOW(false, Integer.valueOf(id));
			
			PM.stateText.setText("## 메시지 : "+ id +"번 상품을 조회합니다...");
		}
		return;
	}
	
	@Override
	public void insertion() {
		if(PM.prodCombo.getSelectedIndex() == 0) { //PRODUCT 테이블에 튜플 삽입 메소드
			dao.SQL_INSERT(true, 0);
			
			dao.SQL_SHOW(true, 0);	//갱신 직후 보여준다.
		}
		else { //특정 상품 수정.
			String id = (String) PM.prodCombo.getSelectedItem();
			dao.SQL_INSERT(false, Integer.valueOf(id));
			
			dao.SQL_SHOW(true, 0);	//갱신 직후 보여준다.
		}
		for(int i=0;i<3;i++)
			PM.prodtf[i].setText("");
	}
	
	//특정 상품 삭제.
	@Override
	public void deletion() {
		String id = (String) PM.prodCombo.getSelectedItem();
		dao.SQL_DELETE(id);
		
		dao.SQL_SHOW(true, 0);	//갱신 직후 보여준다.
	}
}
