package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

//로그인 뷰 -> 관리자 뷰
public class AdminView extends JPanel {
	protected CardLayout cardLayout;
	protected Container tab;
	public JButton cm_btn = new JButton("고객관리");
	public JButton pm_btn = new JButton("상품관리");
	private LoginPanel LP = new LoginPanel();
	JLabel title = new JLabel("Server Mode");
	private static Container c;
	public JButton Previousbtn = new JButton("< 이전");
	JButton LogOutbtn = new JButton("로그아웃");
	JPanel window;
	
	//각각의 싱글톤 객체 호출.
	LoginView LV = LoginView.getInstance();
	CusManager CM = CusManager.getInstance();
	ProdManager PM = ProdManager.getInstance();
	
	public AdminView() {
		JLayeredPane layeredpane = new JLayeredPane();
		layeredpane.setBounds(0, 0, 700, 600);
		layeredpane.setLayout(null);

		setLayout(new BorderLayout());

		cm_btn.setBackground(Color.black);
		cm_btn.setFont(new Font("고딕체", Font.BOLD, 19));
		cm_btn.setForeground(Color.WHITE);

		pm_btn.setBackground(Color.black);
		pm_btn.setFont(new Font("고딕체", Font.BOLD, 19));
		pm_btn.setForeground(Color.WHITE);

		title.setFont(new Font("고딕체", Font.BOLD, 29));
		title.setForeground(Color.BLACK);

		cm_btn.setBounds(200, 200, 300, 60);
		pm_btn.setBounds(200, 300, 300, 60);
		title.setBounds(270, 100, 300, 60);

		layeredpane.add(cm_btn);
		layeredpane.add(pm_btn);
		layeredpane.add(title);
		
		add(layeredpane);
		setSize(700, 600);
		setVisible(true);
		
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