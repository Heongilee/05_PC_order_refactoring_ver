package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

//로그인 뷰 -> 관리자 뷰
public class AdminView extends JPanel {
	private static AdminView AV = new AdminView();
	protected CardLayout cardLayout;
	protected Container tab;
	public JButton cm_btn,pm_btn;
	private LoginPanel LP = new LoginPanel();
	JLabel title = new JLabel("Server Mode");
	private static Container c;
	public JButton Previousbtn = new JButton("< 이전");
	JButton LogOutbtn = new JButton("로그아웃");
	
	//각각의 싱글톤 객체 호출.
	LoginView LV = LoginView.getInstance();
	CusManager CM = CusManager.getInstance();
	ProdManager PM = ProdManager.getInstance();
	
	private AdminView() {
		JLayeredPane layeredpane = new JLayeredPane();
		layeredpane.setBounds(0, 0, 700, 600);
		layeredpane.setLayout(null);

		setLayout(new BorderLayout());

		//고객관리 버튼 제작
		cm_btn = makeButton("고객관리", "고객관리", new Rectangle(200, 200, 300, 60), 19);

		//상품관리 버튼 제작
		pm_btn = makeButton("상품관리", "상품관리", new Rectangle(200, 300, 300, 60), 19);

		title.setFont(new Font("고딕체", Font.BOLD, 29));
<<<<<<< HEAD
		title.setForeground(Color.BLACK);	

		cm_btn.setBounds(200, 200, 300, 60);
		pm_btn.setBounds(200, 300, 300, 60);
=======
		title.setForeground(Color.BLACK);
>>>>>>> 31e54934c7a39b711fb9f8b14551f56a96c21a61
		title.setBounds(270, 100, 300, 60);

		layeredpane.add(cm_btn);
		layeredpane.add(pm_btn);
		layeredpane.add(title);
		
		add(layeredpane);
		setSize(700, 600);
		setVisible(true);
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
	
	//싱글톤 객체 적용
	public static AdminView getInstance() {
		return AV;
	}

	public class LoginPanel extends JPanel {
		public LoginPanel() {
			setLayout(new GridLayout(2, 1, 100, 22));
		}
	}
	
	//리스너 핸들러 연결 메소드
	public void addButtonActionListener(ActionListener listener) {
		cm_btn.addActionListener(listener);
		pm_btn.addActionListener(listener);
		Previousbtn.addActionListener(listener);
	}
}