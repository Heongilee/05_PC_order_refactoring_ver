package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//로그인 뷰 -> 회원가입 뷰
public class SignUpView extends JPanel {
	private static SignUpView _signUpView = new SignUpView();
	public LoginView _loginView = LoginView.getInstance();
	public JPanel signUpPanel = new JPanel();
	public JLabel SignUpLabel, identificationLabel, passwordLabel, nameLabel, emailLabel;

	// 회원가입 양식에 맞는 JTextField들.
	public JTextField identificationTextField, passwordTextField, nameTextField, emailTextField;

	// 중복방지를 이벤트를 부르기 위한 중복확인 버튼.
	public JButton completeButton, duplicateMyIdentificationButton, duplicateMyNameButtion, duplicateMyEmailButton;

	private SignUpView() {
		setLayout(new BorderLayout());
		// 레이블 제작, 삽입
		SignUpLabel = makeLabel("회원가입", new Rectangle(300, 0, 130, 100), 30);
		identificationLabel = makeLabel("아이디", new Rectangle(70, 90, 100, 30), 18);
		passwordLabel = makeLabel("비밀번호", new Rectangle(70, 170, 100, 30), 18);
		nameLabel = makeLabel("닉네임", new Rectangle(70, 250, 100, 30), 18);
		emailLabel = makeLabel("이메일", new Rectangle(70, 330, 100, 30), 18);
		
		//텍스트 필드 생성
		identificationTextField = makeText(new Rectangle(170, 90, 250, 30));
		passwordTextField = makeText(new Rectangle(170, 170, 250, 30));
		nameTextField = makeText(new Rectangle(170, 250, 250 ,30));
		emailTextField = makeText(new Rectangle(170, 330, 250, 30));
		
		// 완료 버튼 위치, 크기 조절
		completeButton = makeButton("완료", "btn", new Rectangle(200, 400, 300, 54), 19);

		// 아이디 중복확인 버튼 위치, 크기 조절
		duplicateMyIdentificationButton = makeButton("중복확인", "IdOverlapbtn", new Rectangle(450, 90, 100, 30), 14);

		// 닉네임 중복확인 버튼 위치, 크기 조절
		duplicateMyNameButtion = makeButton("중복확인", "NameOverlapbtn", new Rectangle(450, 250, 100, 30), 14);

		// 이메일 중복확인 버튼 위치, 크기 조절
		duplicateMyEmailButton = makeButton("중복확인", "EmailOverlapbtn", new Rectangle(450, 330, 100, 30), 14);

		add(signUpPanel);

		setSize(700, 600);
		setVisible(true);
	}

	// 레이블이 많아서 객체를 리턴 하도록 메소드 추출 적용
	// 임시변수 또한 그대로 사용되므로 임시변수 내용 직접삽입
	public JLabel makeLabel(String str, Rectangle rect, int size) {
		JLabel result;
		result = new JLabel(str);
		result.setFont(new Font("고딕체", Font.BOLD, size));
		result.setForeground(Color.BLACK);
		result.setBounds(rect.x, rect.y, rect.width, rect.height);
		add(result);
		return result;
	}
	
	//텍스트 필드제작 메소드를 통해 제작하기 위해 메소드 추출 적용
	//임시변수 또한 그대로 사용, 임시변수 내용 직접삽입
	public JTextField makeText(Rectangle rect) {
		JTextField result;
		result = new JTextField();
		result.setBounds(rect.x, rect.y, rect.width, rect.height);
		add(result);
		return result;
	}
	
	//버튼 제작 메소드를 통해 제작하는 메소드 추출 적용
	//임시변수 내용 직접삽입
	public JButton makeButton(String name, String str, Rectangle rect, int size) {
		JButton result = new JButton(name);
		result.setBackground(Color.black);
		result.setFont(new Font("고딕체", Font.PLAIN, size));
		result.setForeground(Color.WHITE);
		result.setBounds(rect.x, rect.y, rect.width, rect.height);
		result.setName(str);
		add(result);
		return result;
	}
	
	//싱글톤 객체 리턴
	public static SignUpView getInstance() {
		return _signUpView;
	}

	// 리스너 핸들러 연결 메소드
	public void addButtonActionListener(ActionListener listener) {
		duplicateMyIdentificationButton.addActionListener(listener);
		duplicateMyNameButtion.addActionListener(listener);
		duplicateMyEmailButton.addActionListener(listener);
		completeButton.addActionListener(listener); // 회원가입 제출 버튼.
	}
}
