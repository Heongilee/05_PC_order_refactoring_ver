package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Controller.PCController;
import Model.Product_DTO;

//로그인 뷰 -> 사용자 뷰 (싱글톤 패턴)
public class GUIView extends JPanel {
	public static GUIView _guiView;
	LoginView _loginView = LoginView.getInstance();
	boolean toggle = true;
	JPanel noticePanel = new JPanel();// 위쪽 전체 패널
	JPanel userInfoPanel = new JPanel();// 위쪽 오른쪽 라벨 패털 (유저 정보)
	public JPanel weatherPanel = new JPanel();// 위쪽 왼쪽 날씨 패널
	public ImageIcon img = null; // 날씨 이미지 정보
	public JLabel weaimg;// 날씨 이미지 레이블
	public JLabel weainfo[] = new JLabel[3];//지역 날씨 정보를 담는 레이블
	JLabel title = new JLabel("PC방 주문 프로그램");
	JPanel leftPanel = new JPanel();// 왼쪽 패널
	JPanel productDisplayPanel = new JPanel(); // 가운데 스크롤 패널
	JPanel orderDisplayPanel = new JPanel(); // 가운데 오른쪽 패널
	JPanel priceSummationPanel = new JPanel(); // 가운데 아래 패널
	JPanel middlePanel = new JPanel(); // 가운데 패널
	JPanel orderListPanel = new JPanel();// 가운데 오른쪽 위쪽 패널
	JPanel rightPanel = new JPanel();//오른쪽 채팅 패널
	JToolBar bar = new JToolBar();
	public JButton LogOutbtn = new JButton("로그아웃");

	JLabel orderListLabel = new JLabel(">> 주문내역");
	JLabel orderListAttributes = new JLabel("상품명                  가격                   개수");
	public JTextArea textAreaCenter = new JTextArea();// 가운데 센터 텍스트
	public JLabel priceSummationtextField = new JLabel("합계  ");// 합계 텍스트 필드
	public int orderSum = 0;
	public JLabel orderSumLabel = new JLabel(Integer.toString(orderSum));

	public JButton paymentButton = new JButton("결제");
	public JTextArea chattingTextArea = new JTextArea("", 10, 30);// 오른쪽 텍스트
	JLabel chattingLabel = new JLabel("Chatting");
	public String categories[] = { "BEST3", "라면류", "음료류", "간식류", "과자류" };
	public JList<Product_DTO> productTypes = new JList<Product_DTO>();// 왼쪽 패널 리스트
	public JLabel cityInfoLabel = new JLabel("");
	public JLabel noticeMessage = new JLabel("## 메시지");
	JLabel productClassification = new JLabel("상품 분류");
	JPanel selectedCategoryProductsPanel = new JPanel();// 가운데 왼쪽 패널에 위 레이블 들
	JLabel productNameLabel = new JLabel("상품이름");
	JLabel priceLabel = new JLabel("( 가격 )");
	String userInfoArray[] = { "아이디: ", "로그인 시간", "포인트: " };
	public JButton categoryButtons[] = new JButton[5];
	public JLabel userInfoLabel[] = new JLabel[3];
	public JTextField msgInput = new JTextField();
	public String id, pointLabel, seat = "";

	private GUIView() {
		setLayout(new BorderLayout(10, 10));

		// 위쪽 패널 구성
		weatherPanel.setLayout(new BorderLayout());
		cityInfoLabel.setLayout(new GridLayout(3, 1));

		// 오른쪽 사용자 정보 레이블
		userInfoPanel.setLayout(new GridLayout(3, 2));

		userInfoLabel[0] = new JLabel(userInfoArray[0]);
		userInfoLabel[0].setAlignmentX(userInfoPanel.LEFT_ALIGNMENT);
		userInfoPanel.add(userInfoLabel[0]);

		Date today = new Date();
		SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");

		userInfoLabel[1] = new JLabel(userInfoArray[1] + ": " + time.format(today));
		userInfoLabel[1].setAlignmentX(userInfoPanel.RIGHT_ALIGNMENT);
		userInfoPanel.add(userInfoLabel[1]);

		userInfoLabel[2] = new JLabel(userInfoArray[2]);
		userInfoLabel[2].setAlignmentX(userInfoPanel.RIGHT_ALIGNMENT);
		userInfoPanel.add(userInfoLabel[2]);

		userInfoPanel.setBackground(Color.WHITE);

		noticePanel.setLayout(new BorderLayout(10, 10));

		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("돋움", Font.PLAIN, 30));
		noticePanel.add(title, BorderLayout.NORTH);

		noticeMessage.setHorizontalAlignment(JLabel.CENTER);
		noticeMessage.setFont(new Font("돋움", Font.PLAIN, 12));
		noticeMessage.setForeground(Color.RED);
		noticePanel.add(noticeMessage);

		noticePanel.add(userInfoPanel, BorderLayout.EAST);
		noticePanel.add(weatherPanel, BorderLayout.WEST);
		noticePanel.setBackground(Color.WHITE);
		if (toggle == true)
			noticePanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK)));

		// 왼쪽 패널 구성

		leftPanel.setLayout(new GridLayout(6, 1));
		productClassification.setHorizontalAlignment(JLabel.CENTER);
		leftPanel.add(productClassification);
		for (int i = 0; i < 5; i++) {
			categoryButtons[i] = new JButton(categories[i]);
			categoryButtons[i].setBackground(Color.black);
			categoryButtons[i].setFont(new Font("굴림", Font.PLAIN, 12));
			categoryButtons[i].setForeground(Color.WHITE);
			leftPanel.add(categoryButtons[i]);
		}

		if (toggle == true)
			leftPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK)));

		// 가운데 패널 구성
		middlePanel.setLayout(new BorderLayout(10, 10));
		productDisplayPanel.setLayout(new BorderLayout());
		productDisplayPanel.setPreferredSize(new Dimension(200, 10));
		orderDisplayPanel.setLayout(new BorderLayout());
		priceSummationPanel.setLayout(new GridLayout(1, 3));
		selectedCategoryProductsPanel.setLayout(new BorderLayout());
		orderListPanel.setLayout(new BorderLayout());

		productNameLabel.setHorizontalAlignment(JLabel.LEFT);
		priceLabel.setHorizontalAlignment(JLabel.RIGHT);
		selectedCategoryProductsPanel.add(productNameLabel, BorderLayout.WEST);
		selectedCategoryProductsPanel.add(priceLabel, BorderLayout.EAST);
		productDisplayPanel.add(selectedCategoryProductsPanel, BorderLayout.NORTH);
		productDisplayPanel.add(productTypes, BorderLayout.CENTER);

		orderListPanel.add(orderListLabel, BorderLayout.NORTH);
		orderListPanel.add(orderListAttributes, BorderLayout.SOUTH);

		textAreaCenter.setEditable(false);
		orderDisplayPanel.add(orderListPanel, BorderLayout.NORTH);
		orderDisplayPanel.add(textAreaCenter, BorderLayout.CENTER);
		orderDisplayPanel.add(priceSummationPanel, BorderLayout.SOUTH);
		priceSummationPanel.add(priceSummationtextField);
		priceSummationPanel.add(orderSumLabel);

		paymentButton.setBackground(Color.black);
		paymentButton.setFont(new Font("고딕체", Font.PLAIN, 10));
		paymentButton.setForeground(Color.WHITE);

		priceSummationPanel.add(paymentButton);

		if (toggle == true)
			productDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK)));

		if (toggle == true)
			orderDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK)));

		middlePanel.add(productDisplayPanel, BorderLayout.WEST);
		middlePanel.add(orderDisplayPanel, BorderLayout.CENTER);

		// 오른쪽 패널 구성
		// JTextArea의 내용을 수정하지 못하도록 한다. 즉, 출력 전용으로 사용한다.
		chattingTextArea.setEditable(false);
		// 수직 스크롤 바는 항상 나타내고 수평 스크롤 바는 필요할 때 나타나도록 프로그래밍한다.
		JScrollPane jsp = new JScrollPane(chattingTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		rightPanel.setLayout(new BorderLayout(10, 0));
		rightPanel.setPreferredSize(new Dimension(300, 10));
		chattingLabel.setHorizontalAlignment(JLabel.CENTER);
		rightPanel.add(chattingLabel, BorderLayout.NORTH);
		rightPanel.add(chattingTextArea, BorderLayout.CENTER);
		rightPanel.add(msgInput, BorderLayout.SOUTH);

		if (toggle == true)
			rightPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK)));

		add(noticePanel, BorderLayout.NORTH);
		add(leftPanel, BorderLayout.WEST);
		add(middlePanel, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		setVisible(false);
	}

	//싱글톤 객체를 반환함.
	public static GUIView getInstance() {
		if(_guiView == null) {
			_guiView = new GUIView();
		}
		return _guiView;
	}
	
	//리스너 핸들러 연결 메소드 addButtonActionListener() & addMyMouseListener() 
	public void addButtonActionListener(ActionListener listener) {
		LogOutbtn.addActionListener(listener);
		msgInput.addActionListener(listener);
		for(int i=0;i<5;i++)
			categoryButtons[i].addActionListener(listener);
		paymentButton.addActionListener(listener);
	}
	public void addMyMouseListener(MouseListener listener) {
		productTypes.addMouseListener(listener);
	}
}