package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

//로그인 뷰 (싱글톤 패턴)
public class LoginView extends JFrame{
	// 카드레이아웃 뷰 
	private SignUpView _signUpView = SignUpView.getInstance();
	private JLayeredPane _layeredpane = new JLayeredPane();
	public AdminView _adminView = AdminView.getInstance();
	private GUIView _guiview = GUIView.getInstance();
	private CusManager _cusManager = CusManager.getInstance();
	private ProdManager _prodManager = ProdManager.getInstance();
	private static LoginView _loginView = new LoginView();
	private GUIView _guiView = GUIView.getInstance();

	public CardLayout cardLayout;
	public JPanel window;
	BufferedImage img = null;
	public JTextField loginTextField;			//아이디 필드
	public JPasswordField passwordField;		//패스워드 필드
	public JButton loginbt;
	public JButton SignUpbtn;
	JLabel identificationLabel, passwordLabel, orLabel;
	boolean flag;
	public JRadioButton user, admin;
	ButtonGroup goup = new ButtonGroup();
	
	public JToolBar bar = new JToolBar();
	public JButton previousBtn = new JButton("< 이전");
	public JButton logoutBtn = new JButton("로그아웃");
	
	public String id;
	
	private LoginView() {
		setTitle("로그인");
		setSize(700, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 툴바 interface
		bar.setVisible(false);
		bar.add(previousBtn);
		bar.addSeparator(new Dimension(756, 20));
		bar.add(logoutBtn);
		add(bar, BorderLayout.NORTH);
		
		Container c = getContentPane();

		try {} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		//전체 틀
		JLayeredPane layeredpane = new JLayeredPane();
		layeredpane.setBounds(0, 0, 700, 600);
		layeredpane.setLayout(null);

		Rectangle rect = new Rectangle(200, 50, 200, 30);
		//user 모드 라디오 버튼
		user = makeRadio("User MODE", new Rectangle(200, 50, 200, 30), 18);
		layeredpane.add(user);

		//관리자 모드 라디오 버튼
		admin = makeRadio("Admin MODE", new Rectangle(400, 50, 200, 30), 18);
		layeredpane.add(admin);

		//아이디 라벨
		identificationLabel = makeLabel("아이디", new Rectangle(100, 109, 100, 30), 18);
		loginTextField = new JTextField(15);
		loginTextField.setBounds(200, 109, 320, 30);

		layeredpane.add(identificationLabel);
		layeredpane.add(loginTextField);

		//비밀번호 라벨
		passwordLabel = makeLabel("비밀번호", new Rectangle (100, 209, 100, 30), 18);
		passwordField = new JPasswordField(15);
		passwordField.setBounds(200, 209, 320, 30);

		layeredpane.add(passwordLabel);
		layeredpane.add(passwordField);

		//로그인 버튼
		loginbt = makeButton("로그인", new Rectangle(240, 300, 200, 48), 18);
		layeredpane.add(loginbt);

		//or 레이블
		orLabel = makeLabel("or", new Rectangle(330, 350, 200, 48), 23);
		layeredpane.add(orLabel);

		//회원가입 버튼
		SignUpbtn = makeButton("회원가입", new Rectangle(240, 400, 200, 48), 18);
		layeredpane.add(SignUpbtn);

		//layeredpane.add(panel);

		//만들었던 것을 다 올리고 카드레이 아웃으로 화면 전환 시켜준다
		window = new JPanel();
		cardLayout = new CardLayout();
		window.setLayout(cardLayout);
		window.add(layeredpane, "layer");
		window.add(_adminView, "admin");
		window.add(_signUpView, "signUp");
		window.add(_guiview, "guiView");
		window.add(_cusManager, "cusManager");
		window.add(_prodManager, "prodManager");
		
		add(window);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//레이블이 많아서 객체를 리턴 하도록 메소드 추출 적용
	//임시변수 또한 그대로 사용되므로 임시변수 내용 직접삽입
	public JLabel makeLabel(String str, Rectangle rect, int size) {
		JLabel result;
		result = new JLabel(str);
		result.setFont(new Font("고딕체", Font.BOLD, size));
		result.setForeground(Color.BLACK);
		result.setBounds(rect.x, rect.y, rect.width, rect.height);
		return result;
	}
	
	//버튼 또한 2개 이상의 사용으로 메소드 추출을 통해 처리
	//임시변수 또한 그대로 사용되므로 임시변수 내용 직접삽입
	public JButton makeButton(String str, Rectangle rect, int size) {
		JButton result;
		result = new JButton(str);
		result.setBackground(Color.black);
		result.setFont(new Font("고딕체", Font.BOLD, size));
		result.setForeground(Color.WHITE);
		result.setBounds(rect.x, rect.y, rect.width, rect.height);
		return result;
	}
	
	//라디오 버튼 생성해서 그룹으로 묶는 것을 메소드 추출해서 처리
	//임시변수 또한 그대로 사용되므로 임시변수 내용 직접삽입
	public JRadioButton makeRadio(String str, Rectangle rect, int size) {
		JRadioButton result;
		result = new JRadioButton(str);
		result.setFont(new Font("고딕체", Font.BOLD, size));
		result.setForeground(Color.BLACK);
		result.setBounds(rect.x, rect.y, rect.width, rect.height);
		goup.add(result);
		return result;
	}
	
	//싱글톤 객체 접근 메소드.
	public static LoginView getInstance() { 
		return _loginView;
	}
	
	public void addButtonActionListener(ActionListener listener) {
		loginbt.addActionListener(listener);			//로그인 버튼
		SignUpbtn.addActionListener(listener);			//회원가입 버튼
		previousBtn.addActionListener(listener);		//이전 버튼
		logoutBtn.addActionListener(listener);		//로그아웃 버튼
	}
}