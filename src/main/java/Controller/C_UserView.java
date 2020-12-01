package Controller;

import java.awt.Color;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import Model.CustomersDao;
import Model.CustomersDto;
import Model.CustomersDtoBuilder;
import Model.OrdersDao;
import Model.OrdersDto;
import Model.Product_DAO;
import Model.Product_DTO;
import View.GUIView;
import View.LoginView;

/*
 * View\GUIView.java에서 달아준 이벤트 리스너가 
 * Controller\PCController.java에 있는 ActionPerformed를 받아 호출되는 메소드들.
 * */
public class C_UserView implements I_UserView{
	//싱글톤 객체를 불러옴.
	private static Product_DAO dao = Product_DAO.getInstance();
	private static OrdersDao ordersDao = OrdersDao.getInstance();
	private static CustomersDao _customersDao = CustomersDao.getInstance();
	private static GUIView GU = GUIView.getInstance();
	private static LoginView _loginView = LoginView.getInstance();
	private static Vector<OrdersDto> _orderList = PCController.orderList;
	
	//PRODUCTS테이블에서 pTYPE속성에 맞는 항목들
	//(0 : BEST3	1 : 라면류		2 : 음식류		3 : 간식류		4 : 과자류)을
	// 불러와 JList에 뿌려준다.
	@Override
	public void Load_FoodCategory(int type) {
		// 0 : BEST3	1 : 라면류		2 : 음식류		3 : 간식류		4 : 과자류
		DefaultListModel<Product_DTO> listModel;
		switch(type) {
		case 0:
			GU.menuList = ordersDao.SQL_BEST3();
			listModel = new DefaultListModel<Product_DTO>();
			for(int j=0;j<GU.menuList.size();j++)
				listModel.addElement(GU.menuList.get(j));
			GU.JList_ProdType.setModel(listModel);
			break;
		case 1:
			GU.menuList = dao.USERVIEW_FUNC1(GU.ca[1]);
			listModel = new DefaultListModel<Product_DTO>();
			for(int i=0;i<GU.menuList.size();i++) {
				listModel.addElement(GU.menuList.get(i));
			}
			GU.JList_ProdType.setModel(listModel);
			break;
		case 2:
			GU.menuList = dao.USERVIEW_FUNC1(GU.ca[2]);
			listModel = new DefaultListModel<Product_DTO>();
			for(int i=0;i<GU.menuList.size();i++) {
				listModel.addElement(GU.menuList.get(i));
			}
			GU.JList_ProdType.setModel(listModel);
			break;
		case 3:
			GU.menuList = dao.USERVIEW_FUNC1(GU.ca[3]);
			listModel = new DefaultListModel<Product_DTO>();
			for(int i=0;i<GU.menuList.size();i++) {
				listModel.addElement(GU.menuList.get(i));
			}
			GU.JList_ProdType.setModel(listModel);
			break;
		case 4:
			GU.menuList = dao.USERVIEW_FUNC1(GU.ca[4]);
			listModel = new DefaultListModel<Product_DTO>();
			for(int i=0;i<GU.menuList.size();i++) {
				listModel.addElement(GU.menuList.get(i));
			}
			GU.JList_ProdType.setModel(listModel);
			break;
			default:
				break;
		}
	}

	//JList의 상품을 주문목록에 추가시키기.
	@Override
	public OrdersDto Add_Orderlog() {
		Product_DTO dto = GU.JList_ProdType.getSelectedValue();
		OrdersDto res;
		String cNAME = LoginView.getInstance().loginTextField.getText();
		String pNAME = dto.getpNAME();
		int oCNT;
		oCNT = ordersDao.ORDERS_FUNC1(dto);		//주문 목록에 상품을 추가시키는 메소드.
		
		res = new OrdersDto(cNAME, pNAME, oCNT);
		return res;
	}

	//주문목록의 모든 상품들을 결제하는 메소드. (포인트가 부족하면 결제는 되지 않는다.)
	@Override
	public Boolean Submit_Order() {
		Boolean orderFlag = false;
		int price = Integer.parseInt(GU.order_sum_label.getText());//합계를 가져오는 것
		
		//* CustomersDto 에 담기
		CustomersDtoBuilder customersDtoBuilder = new CustomersDtoBuilder();
		CustomersDto customersDto = customersDtoBuilder
			.setCustomerId(LoginView.getInstance().loginTextField.getText())
			.build();
		
		//! This code has been depricated...
		/*int point = Integer.parseInt(GU.la[2].getText().substring(6));//해당 아이디 포인트를 가져온다
		String id = LoginView.getInstance().loginTextField.getText();*/
		
		customersDto = _customersDao.checkUserBalance(customersDto);
		if(customersDto.getCustomerBalance() >= price) {
			orderFlag = true;
			reloadReferenceObjects();

			customersDto.setCustomerBalance(customersDto.getCustomerBalance() - price);
			_customersDao.updateUserBalance(customersDto);
			showNotificationMessage(customersDto.getCustomerId() + "님, 결제가 완료되었습니다.");
			renewCustomerPoints(customersDto.getCustomerBalance());			
			for(int i=0;i<_orderList.size();i++){
				OrdersDto ordersDto = _orderList.get(i);
				
				//벡터에 있는 주문 목록을 튜플에 삽입.
				ordersDao.ORDERS_FUNC_1_1(ordersDto.getcNAME(), ordersDto.getpNAME(), ordersDto.getoCNT());
			}
		} else {
			showNotificationMessage(customersDto.getCustomerId() + "님, 포인트가 부족합니다.");
		}

		//! This code has been depricated...
		/*if(CustomersDao.getInstance().checkUserBalance(id, value)) {
			Order_Flag = true;
			GU.mess.setText(LoginView.getInstance().loginTextField.getText() + "님, " + "결제가 되었습니다.");
			System.out.println(point - value);
			GU.la[2].setText("포인트 : " + Integer.toString(point-value));
			
			//-------------- (Controller\PCController.java)order_list 에 있는 모든 상품 목록들을 튜플에 삽입한다.
			Orders_DTO tmp;
			for(int i=0;i<PCController.order_list.size();i++){
				tmp = PCController.order_list.get(i);
				
				//벡터에 있는 주문 목록을 튜플에 삽입.
				dao2.ORDERS_FUNC_1_1(tmp.getcNAME(), tmp.getpNAME(), tmp.getoCNT());
			}
		}
		else {GU.mess.setText("회원님, 포인트가 부족합니다.");}
		*/
		return orderFlag;
	}

	private void renewCustomerPoints(Integer customerBalance) {
		GU.la[2].setText("포인트 : " + customerBalance.toString());
		GU.ta1.setText("");
		GU.order_sum = 0;
		GU.order_sum_label.setText(String.valueOf(GU.order_sum));
		return ;
	}

	private void reloadReferenceObjects() {
		_orderList = PCController.orderList;
		
		return;
	}

	private void showNotificationMessage(String message) {
		GU.mess.setText(message);

		return;
	}
}
