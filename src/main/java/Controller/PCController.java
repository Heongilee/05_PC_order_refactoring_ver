package Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;

import Model.CustomersDao;
import Model.Message;
import Model.OrdersDao;
import Model.ViewState;
import Model.OrdersDto;
import Model.PCChatData;
import View.AdminView;
import View.CusManager;
import View.GUIView;
import View.LoginView;
import View.ProdManager;
import View.SignUpView;

public class PCController implements Runnable {
   public final LoginView _loginView;
   public final CusManager CM;
   public final ProdManager PM;
   public final GUIView _guiView;
   public final AdminView AV;
   public final SignUpView SUV;
   public static C_login cl;
   public final C_SignUp cs;
   public final C_ProdManager cp;
   public final C_UserView cu;
   public final C_AdminView ca;
   private final PCChatData CMchatData;
   private final PCChatData GUIchatData;
   
   // 참조객체 선언.
   public static AccountChecker _accountChecker = AccountChecker.getInstance();
   public static CustomersDao _customerDao = CustomersDao.getInstance();
   public static OrdersDao o_dao;
   public static ToolBar _toolBar = ToolBar.getInstance();
   public static ViewState _viewState = ViewState.getInstance();

   //Gson 객체 초기화
   public static Gson gson = new Gson();
   
   // GUIView에서 JList에서 불러온 Product_DTO객체를 가지는 Vector
   public static Vector<OrdersDto> orderList = new Vector<OrdersDto>();
   
   // 메시지 클래스 참조 객체
   Message gson_message;
   
   // 로거 객체
   Logger logger;
   String ip = "127.0.0.1";
   public static Socket socket;
   public static BufferedReader inMsg;
   public static PrintWriter outMsg;
   public static boolean status;
   Thread thread;
   
   HashMap<String, String> current_temp;

   public PCController(LoginView LV, CusManager CM, ProdManager PM, GUIView GUI, AdminView AV, SignUpView SUV,
         C_login cl, C_SignUp cs, C_ProdManager cp, C_UserView cu, C_AdminView ca, PCChatData CMchatData,
         PCChatData GUIchatData) {
      // 로거 객체 초기화
      logger = Logger.getLogger(this.getClass().getName());

      this._loginView = LV; // LoginView 참조객체 연결
      this.CM = CM; // CusManager 참조객체 연결
      this.PM = PM; // ProdManager 참조객체 연결
      this._guiView = GUI; // GUIView 참조객체 연결
      this.AV = AV; // AdminView 참조객체 연결
      this.SUV = SUV; // SignUpView 참조객체 연결
      this.cl = cl; // C_login 참조객체 연결
      this.cs = cs; // C_SignUp 참조객체 연결
      this.cp = cp; // C_ProdManager 참조객체 연결
      this.cu = cu; // C_UserView 참조객체 연결
      this.ca = ca;   // C_AdminView 참조객체 연결
      this.CMchatData = CMchatData; // PCChatData 참조객체 연결
	  this.GUIchatData = GUIchatData; // PCChatData2 참조객체 연결
	  
	  returnImage();
   }

   public void appMain() {
      // 데이터 객체에서 데이터 변화를 처리할 UI 객체 추가, ta2는 TextArea이다.
      CMchatData.addObjCus(CM.chatContent);
      GUIchatData.addObjGUI(_guiView.ta2);

      // 로그인 뷰 이벤트 처리
      _loginView.addButtonActionListener(new ActionListener() { // 로그인 뷰 레이아웃
         @Override
         public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            // 로그인 버튼이 눌렸을 경우...
            if(obj == _loginView.loginbt) {
               _accountChecker.runAccountChecker();
               if(_accountChecker.getValidationPointer() == _accountChecker.LOGIN_SUCCESS){
                  // ------- 관리자로 로그인 성공! -------
                  if(_loginView.server.isSelected()) {
                     _customerDao.make_check(_loginView.loginTextField.getText());
                     
                     /*   화면 전환   */
                     _loginView.cardLayout.show(_loginView.window, "admin");
                     _toolBar.setVisibleToolBar(false);
                     _loginView.previousBtn.setVisible(true);
                     CM.loginFlag = true;

                     /* 관리자 로그인 들어갔을 경우 이벤트 처리 */
                     connectServer();

                     /* 관리자 화면으로 상태 기록 */
                     _viewState.setCurrent_view_state("AdminView");
                     System.out.println(_viewState.getCurrentViewState());
                  // ------- 사용자로 로그인 성공! -------
                  } else if(_loginView.user.isSelected()) {
                     _customerDao.make_check(_loginView.loginTextField.getText()); //체크값을 1로 바꿔 줌.
                     _loginView.bar.setVisible(true);//bar를 활성

                     /* 화면 전환 */
                     _loginView.cardLayout.show(_loginView.window, "guiView");
                     _loginView.logoutBtn.setVisible(true);
                     _loginView.previousBtn.setVisible(false);
                     _loginView.setSize(900, 700);

                     /* 사용자 로그인 들어갔을 경우 이벤트 처리 */
                     _guiView.id = _loginView.loginTextField.getText();
                     _guiView.la[0].setText("아이디 : " + _guiView.id);
                     _guiView.la[2].setText("포인트 : " + _customerDao.checkUserBalance(_guiView.id));

                     connectServer(); // 로그인 성공에 따른 클라이언트 스레드 생성.

                     /* 사용자 화면으로 상태 기록 */
                     _viewState.setCurrent_view_state("GUIView");
                     System.out.println(_viewState.getCurrentViewState());
                  } else {}
               }
            } else if (obj == _loginView.SignUpbtn) { // 회원가입 버튼을 눌렀을 경우
               _loginView.cardLayout.show(_loginView.window, "signUp");
               _toolBar.setVisibleToolBar(true);
               _loginView.logoutBtn.setVisible(false);
               _loginView.previousBtn.setVisible(true);
               _toolBar.clearSignupForm();

               _viewState.setCurrent_view_state("SignUpView");
            } else if (obj == _loginView.previousBtn) { // 툴바 이전 버튼을 눌렀을 경우
               _toolBar.prevButtonController(_viewState.getCurrentViewState());
               //! This code has been depricated...
               /*if(viewState.getCurrent_view_state().equals(viewState.getviewStateList("SignUpView"))){
                  toolBar.toolBarController("SignUpView");
               }else if(viewState.getCurrent_view_state().equals(viewState.getviewStateList("CusManager"))) {
                  toolBar.toolBarController("CusManager");
               }else if(viewState.getCurrent_view_state().equals(viewState.getviewStateList("ProdManager"))) {
                  toolBar.toolBarController("ProdManager");
               }else {}
               */
            } else if (obj == _loginView.logoutBtn) {
               _toolBar.logoutButtonController(_viewState.getCurrentViewState());
               //! This code has been depricated...
               /*if(viewState.getCurrent_view_state().equals(viewState.getviewStateList("GUIView"))){
                  toolBar.logoutFromGUIView();
               } else if (viewState.getCurrent_view_state().equals(viewState.getviewStateList("CusManager"))) {
                  toolBar.logoutFromCusManager();
               } else if (viewState.getCurrent_view_state().equals(viewState.getviewStateList("ProdManager"))) {
                  toolBar.logoutFromProdManager();
               } else {}
               */
               _toolBar.setVisibleToolBar(false);

               // then, change viewState to LoginView
               _viewState.setCurrent_view_state("LoginView");
            } else {

            }
         }
      });

      // 로그인 뷰->관리자 뷰 이벤트 처리
      _loginView.adminView.addButtonActionListener(new ActionListener() { // 관리자 뷰 레이아웃
         @Override
         public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if (obj == _loginView.adminView.cm_btn) { // 관리자 뷰에서 고객관리 버튼을 눌렀을 경우
               ca.Goto_CustomerManager();
               _viewState.setCurrent_view_state("CusManagerView");
               System.out.println(_viewState.getCurrentViewState());
            } else if (obj == _loginView.adminView.pm_btn) { // 관리자 뷰에서 상품관리 버튼을 눌렀을 경우
               ca.Goto_ProductManager();
               _viewState.setCurrent_view_state("ProdManagerView");
               System.out.println(_viewState.getCurrentViewState());
            } 
         }
      });

      // 로그인 뷰->회원가입 뷰 이벤트 처리
      SUV.addButtonActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();

            // SignUpView에서 setName한 값을 가지고 if-else문으로 분기조건을 주었다.
            if (btn.getName().equals("IdOverlapbtn")) {	//아이디 중복
               cs.Valid_Check(0);
            } else if (btn.getName().equals("NameOverlapbtn")) { //닉네임 중복
               cs.Valid_Check(1);
            } else if (btn.getName().equals("EmailOverlapbtn")) { //이메일 중복
               cs.Valid_Check(2);
            } else { // 회원가입 완료 버튼 눌렀을 때
               try {
                  cs.Register_Complete();
               } catch (SQLException e1) {
                  e1.printStackTrace();
               } finally {
                  _viewState.setCurrent_view_state("LoginView");
                  System.out.println(_viewState.getCurrentViewState());
               }
            }

         }
      });

      // 고객 관리 뷰 이벤트 처리
      CM.addButtonActionListener(new ActionListener() { // 고객관리 뷰
         @Override
         public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if (obj == CM.chatInput) { // 고객관리 뷰에서 채팅
               if (CM.chatComboBox.getSelectedIndex() == 0) {
                  System.out.println("qq " + CM.chatComboBox.getSelectedIndex());
                  outMsg.println(
                        gson.toJson(new Message("카운터", CM.id, "", CM.chatInput.getText(), "ModeAll", "")));
                  CM.chatInput.setText("");
               } else {
                  System.out.println("pp " + CM.chatComboBox.getSelectedIndex());
                  String str = CM.chatComboBox.getItemAt(CM.chatComboBox.getSelectedIndex());
                  int index = 0;
                  for (String key : current_temp.keySet()) {
                     index++;
                     String value = current_temp.get(key);
                     String strTemp = value + ":" + key;
                     if (strTemp.equals(str)) {
                        outMsg.println(gson.toJson(
                              new Message("카운터", CM.id, "", CM.chatInput.getText(), "ModeSelect", key)));
                        CM.chatInput.setText("");
                        break;
                     }
                  }
               }
            } 
            // ! This code has been deprecated...
            /*else if (obj == LV.previousBtn) { // 고객관리 뷰에서 이전 버튼을 눌렀을 경우
               CM.setVisible(false);
               LV.setVisible(true);
               viewState.setCurrent_view_state(viewState.getviewStateList("AdminView"));
               System.out.println(viewState.getCurrent_view_state());
            } else if (obj == LV.logoutBtn) { // 고객관리 뷰에서 로그아웃 버튼을 눌렀을 경우
            	outMsg.println(gson.toJson(new Message(GUI.seat, GUI.id, "", "", "adminlogout", "")));
               CM.chatContent.setText("");
               c_dao.getInstance().make_check(LV.loginTextField.getText()); //DB 체크값을 바꿔준다.
               CM.setVisible(false);
               LV.getInstance().setVisible(true);
               LV.getInstance().cardLayout.show(LV.getInstance().window, "layer");
               try {
                   outMsg.close();
                   inMsg.close();
                   socket.close();
                } catch (IOException ex) {
                   ex.printStackTrace();
                }
                status = false;
            } 
            */
            else {}
         }
      });

      // 상품 관리 이벤트 처리
      PM.addButtonActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            // ! This code has been deprecated...
            /*if (obj == LV.previousBtn) { // 상품관리 뷰에서 이전 버튼을 눌렀을 경우
               PM.setVisible(false);
               LV.setVisible(true);
               viewState.setCurrent_view_state(viewState.getviewStateList("AdminView"));
               System.out.println(viewState.getCurrent_view_state());
            } else if (obj == PM.logoutBtn) { // 상품관리 뷰에서 로그아웃 버튼을 눌렀을 경우
            	c_dao.getInstance().make_check(LV.loginTextField.getText()); //DB 체크값을 바꿔준다.
               PM.setVisible(false);
               LV.getInstance().setVisible(true);
               LV.getInstance().cardLayout.show(LV.getInstance().window, "layer");
            } else 
            */
            if (obj == PM.crudButton[0]) { // 등록
               cp.insertion();
            } else if (obj == PM.crudButton[1]) { // 조회
               cp.show();
            } else if (obj == PM.crudButton[2]) { // 삭제
               cp.deletion();
            } else {
            }
         }
      });

      // 로그인 뷰 -> 사용자 뷰 이벤트 처리
      _guiView.addButtonActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            ///////////////////////////////////////
            // ↓↓↓↓ 멀티채팅 S/C 통신 이벤트 처리 ↓↓↓↓
            ///////////////////////////////////////
            if (obj == _guiView.LogOutbtn) { // 사용자 뷰에서 로그아웃 버튼을 눌렀을 경우
               outMsg.println(gson.toJson(new Message(_guiView.seat, _guiView.id, "", "", "logout", "adminlogin")));
               _guiView.msgInput.setText("");
               _customerDao.getInstance().make_check(_loginView.loginTextField.getText());
               try {
                  outMsg.close();
                  inMsg.close();
                  socket.close();
               } catch (IOException ex) {
                  ex.printStackTrace();
               }
               status = false;
               System.exit(0);
            } else if (obj == _guiView.msgInput) {
               outMsg.println(gson.toJson(new Message(_guiView.seat, _guiView.id, "", _guiView.msgInput.getText(), "sendtoadmin", "admins")));
               _guiView.msgInput.setText("");
               ///////////////////////////////////
               // ↓↓↓↓ 상품 목록 버튼 ↓↓↓↓
               ///////////////////////////////////
            } else if (obj == _guiView.btn[0]) { // BEST 3
               cu.Load_FoodCategory(0);
            } else if (obj == _guiView.btn[1]) { // 라면류
               cu.Load_FoodCategory(1);
            } else if (obj == _guiView.btn[2]) { // 음료류
               cu.Load_FoodCategory(2);
            } else if (obj == _guiView.btn[3]) { // 간식류
               cu.Load_FoodCategory(3);
            } else if (obj == _guiView.btn[4]) { // 과자류
               cu.Load_FoodCategory(4);
            } else if (obj == _guiView.sumb) { // 결제 버튼
               if(cu.Submit_Order()) {
                  // 채팅 메시지로 사용자가 결제 완료 했다고 넣는 부분
            	   outMsg.println(gson.toJson(new Message(_guiView.seat, _guiView.id, "", "에서 결제하였습니다.", "orderSendServer", "")));
               }
               else {}
            }
         }
      });
      
      //로그인 뷰 -> 사용자 뷰 에서 JList에 마우스 이벤트 처리
      _guiView.addMyMouseListener(new MouseAdapter() {
         @Override
       public void mousePressed(MouseEvent e) {
            OrdersDto tmp;
            super.mousePressed(e);
            tmp = cu.Add_Orderlog();
            orderList.add(tmp);
       }
      });
   } // --------------------------- AppMain() 종료 -------------------------------
	// 날씨 XML 데이터파싱
	private static String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		if (nValue == null) {
			return null;
		}
		return nValue.getNodeValue();
	}

	//날씨 데이터 DOM 파서
	public void returnImage() {
		try {
			String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1121571000";

			// 페이지에 접근해줄 Document객체 생성
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();

			// 파싱할 데이터의 tag에 접근
			NodeList nList = doc.getElementsByTagName("data");
			Node nNode = nList.item(0);// 인덱스 0인 데이터 즉 맨 앞에 있는 데이터를 가져옴
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String str = getTagValue("wfKor", eElement);
				_guiView.weainfo[0] = new JLabel("지역: 서울특별시 광진구 화양동");
				_guiView.weainfo[1] = new JLabel("현재 기온: " + getTagValue("temp", eElement) + " ºC");
				_guiView.img = new ImageIcon("img/" + str + ".gif");
				_guiView.weaimg = new JLabel(_guiView.img);
				_guiView.weainfo[2] = new JLabel("현재 상태: " + str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 3; i++) {
			_guiView.wea.add(_guiView.weainfo[i]);
		}
		//패널에 부착
		_guiView.weather.add(_guiView.weaimg, BorderLayout.WEST);
		_guiView.weather.add(_guiView.wea, BorderLayout.CENTER);
		_guiView.weather.setBackground(Color.WHITE);
		_guiView.weather.setPreferredSize(new Dimension(250, 0));
	}

   public void connectServer() {
      try {
         // 소켓 생성
         socket = new Socket(ip, 8888);
         logger.info("[Client]Server 연결 성공!!");

         // 입출력 스트림 생성
         inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         outMsg = new PrintWriter(socket.getOutputStream(), true);

         // 서버에 로그인 메시지 전달
         if (CM.loginFlag) {
           // 관리자가 로그인 했을 경우.
            gson_message = new Message("카운터", CM.id, "", "", "adminlogin", "");
         } else {
           // 사용자가 로그인 했을 경우
            gson_message = new Message("0", _guiView.id, "", "", "login", "adminlogin");
         }
         outMsg.println(gson.toJson(gson_message));

         // 메시지 수신을 위한 스레드 생성
         thread = new Thread(this);
         thread.start();
      } catch (Exception e) {
         logger.log(Level.WARNING, "[MultiChatUI]connectServer() Exception 발생!!");
         e.printStackTrace();
      }
   }

   @Override
   public void run() {
      // 수신 메시지를 처리하는 데 필요한 변수 선언
      String msg;
      status = true;
      while (status) {
         try {
            // 메시지 수신 및 파싱
            msg = inMsg.readLine();
            System.out.println(msg);
            Message m_temp = gson.fromJson(msg, Message.class);
            if(m_temp.getType().equals("current_count") && gson_message.getId().equals("관리자"))
            {
               /*관리자로 로그인시에 현재 접속중인 사용자들의 좌석과 아이디를 콤보박스에 최신화 합니다.*/
               current_temp = new HashMap<String,String>();
               current_temp.putAll(gson.fromJson(m_temp.getMsg(),HashMap.class));
               
               Set<String> set = current_temp.keySet();
               for(String key : set)
               {
                  String value = key + ":" + current_temp.get(key);
                  int i;
                  for(i = 0 ; i < CM.chatComboBox.getItemCount();i++) {
                     if(CM.chatComboBox.getItemAt(i).equals(value))
                     {
                        break;
                     }
                  }
                  if(i == CM.chatComboBox.getItemCount()) {
                     CM.chatComboBox.addItem(value);
                  }
               }
             }
            if(m_temp.getType().equals("fromServer_order")) {
               /*주문한 유저의 좌석에 주문 정보를 뿌려 줍니다.*/
               String orderList = "";
               orderList = gson.fromJson(m_temp.getReceiveId(), String.class);
               CM.SP.seatTextArea[(Integer.parseInt((String)m_temp.getSeat())-1)].append(orderList);
            }
            if(m_temp.getType().equals("user_login") &&gson_message.getId().equals("관리자"))
            {
               /*사용자 로그인시에 콤보박스에 방금 접속한 사용자의 좌석과 아이디를 추가합니다.*/
               String value = m_temp.getSeat()+":"+m_temp.getId();
               current_temp.put(m_temp.getId(),m_temp.getSeat());
               CM.chatComboBox.addItem(value);
               /*고객 관리창 좌석에 방금 점속한 사용자의 좌석과 아이디를 뿌려 줍니다.*/
               int seat = Integer.parseInt(m_temp.getSeat())-1;
               for(int i=0 ;i<12; i++) {
                  if(Integer.toString(i).equals(Integer.toString(seat)))
                     CM.SP.seatTextArea[i].setText(m_temp.getSeat() + ":" + m_temp.getId() + "\n");
               }
            }
            if(m_temp.getType().equals("user_logout"))
            {
               /*사용자 로그아웃시 콤보박스에 나간 사용자를 지우고 콤보박스를 최신화 합니다.*/
               int[] customersSeat = new int[12];
               customersSeat = gson.fromJson(m_temp.getReceiveId(), int[].class);
               CM.chatComboBox.removeAllItems();
               CM.chatComboBox.addItem("전체");
               for(int i=0;i<12;i++) {
                  if(customersSeat[i] != 0) {
                     
                     String comboStr = CM.SP.seatTextArea[i].getText();
                     String comboId[] = comboStr.split("\n");
                     
                     CM.chatComboBox.addItem(comboId[0]);
                  }
                  if(customersSeat[i] == 0) {
                     /*사용자 로그아웃시 좌석창에 나간 사용자 제외하고 좌석을 최신화 합니다.*/
                     CM.SP.seatTextArea[i].setText("빈 자 리\n");
                  }
               }
            }
            if(gson_message.getSeat() == "0" && _guiView.seat == "") {
               /*사용자가 들어갈 좌석에 사용자가 없거나 맨 처음 사용자가 들어오는 상태이면 좌석 정보를 채워 놓습니다.*/
               /*나중에 사용자 좌석 정보를 사용자 로그인시와 로그아웃시 이용하기 위함입니다.*/
               _guiView.seat = m_temp.getSeat();
               gson_message.setSeat(m_temp.getSeat());
            }
            
            if(CM.loginFlag)
            {   
               // 관리자 창을 refresh 최신화 해줍니다.
               CMchatData.refreshData(m_temp.getSeat() + " 좌석(" + m_temp.getId() + "):" + m_temp.getMsg() + "\n");
               // 커서를 현재 대화 메시지에 표시
               CM.chatContent.setCaretPosition(CM.chatContent.getDocument().getLength());
            }
            else {
               // 사용자 창을 refresh 최신화 해줍니다.
               GUIchatData.refreshData(m_temp.getSeat() + " 좌석(" + m_temp.getId() + "):" + m_temp.getMsg() + "\n");
               // 커서를 현재 대화 메시지에 표시
               _guiView.ta2.setCaretPosition(_guiView.ta2.getDocument().getLength());
            }
            
         } catch (IOException e) {
            logger.log(Level.WARNING, "[MultiChatUI]메시지 스트림 !!");
         }
      }
      logger.info("[MultiChatUI]" + thread.getName() + " 메시지 수신 스레드 종료됨!!");
   } // run()
}