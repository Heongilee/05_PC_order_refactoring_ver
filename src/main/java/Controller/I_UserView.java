package Controller;

import java.util.Vector;

import Model.Orders_DTO;

//C_UserView.java에서 구현할 인터페이스
public interface I_UserView {
	void Load_FoodCategory(int type);	
	//JButton{BEST3, 라면류, 음료류, 간식류, 과자류}에 해당하는 버튼을 
	//클릭하면 PRODUCTS 테이블의 pTYPE(상품_카테고리 속성)에 해당하는 레코드를
	//가져온다.
	
	Orders_DTO Add_Orderlog();	
	// 해당 상품을 클릭하면 주문 내역에 추가된다.	
	// 항목이 추가될 때 마다 합계 JTextField에 추가된 항목에 해당하는 가격이 책정
	// 되도록 한다.
	
	Boolean Submit_Order();
	// 주문 내역의 상품들을 ORDERS테이블 레코드에 추가시키고
	// 결제 완료 메시지를 출력한다.
	// Exception1. 	CUSTOMERS테이블에서 해당 고객의 cBALANCE(잔액)을 검사해
	// 				결제 금액보다 잔액이 부족하면 결제 실패 메시지를 출력한다.
}
