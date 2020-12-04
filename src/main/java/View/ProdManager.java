package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

//로그인 뷰 -> 관리자 뷰 -> 상품관리 뷰 (싱글톤 패턴)
public class ProdManager extends JFrame {
	public static ProdManager PM = ProdManager.getInstance();
	LoginView LV = LoginView.getInstance();
	JLabel title = new JLabel("상품관리");
	public JLabel stateText = new JLabel("## 메시지 : ");
	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	JPanel wrapPanel = new JPanel();
	JPanel northPanel = new JPanel();
	String[] prodLabel_str = { "상품 번호", "상품명", "단가", "제조사" };
	JLabel[] prodLabel = new JLabel[4];
	public Vector<Model.Product_DTO> v = new Vector<Model.Product_DTO>();
	public JComboBox<String> prodCombo = new JComboBox<String>();
	public JTextField[] prodtf = new JTextField[3];
	String[] List_prodType = {"라면류", "음료류", "간식류", "과자류"};
	public JComboBox<String> prodType = new JComboBox<String>(List_prodType);
	String ta_col = "관리번호\t상품명\t단가\t제조사";
	public JTextArea ta = new JTextArea(ta_col, 27, 30);
	String[] btn_str = {"등록", "조회", "삭제"};
	public JButton[] btn = new JButton[3];

	private ProductPanel PP = new ProductPanel();
	private ShowPanel SP = new ShowPanel();

	JToolBar bar = new JToolBar();
	public JButton previousBtn = new JButton("< 이전");
	public JButton logoutBtn = new JButton("로그아웃");
	
	private ProdManager() {
		super("상품관리");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// 툴바 interface
		bar.add(previousBtn);
		bar.addSeparator(new Dimension(750, 30));
		bar.add(logoutBtn);
		add(bar, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		
		wrapPanel.setLayout(new GridLayout(1,2));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		// title 폰트 크기
		title.setFont(new Font("굴림", Font.BOLD, 45));
		
		// 오른쪽 정렬
		leftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		title.setAlignmentX(CENTER_ALIGNMENT);

		stateText.setAlignmentX(CENTER_ALIGNMENT);
		stateText.setFont(new Font("고딕체", Font.ITALIC, 14));
		stateText.setForeground(Color.BLACK); // 메세지 뜰때마다 빨강으로 전환하기

		northPanel.add(title);
		northPanel.add(stateText);
		panel.add(northPanel, BorderLayout.NORTH);
		leftPanel.add(PP);
		wrapPanel.add(leftPanel);

		rightPanel.add(SP);
		wrapPanel.add(rightPanel);
		panel.add(wrapPanel, BorderLayout.CENTER);
		
		add(panel, BorderLayout.CENTER);
		
		setSize(900, 700);
		setLocationRelativeTo(null);
		// 크기 고정
        //setResizable(false);
        
		setVisible(false);
	}

	public class ProductPanel extends JPanel {
		public ProductPanel() {
			setLayout(new GridLayout(5, 2, 10, 40));
			setPreferredSize(new Dimension((int) (400), (int) (400)));

			for (int i = 0; i < 4; i++) {
				prodLabel[i] = new JLabel(prodLabel_str[i]);
				prodLabel[i].setFont(new Font("고딕체", Font.PLAIN, 16));
				add(prodLabel[i]);

				if (i == 0) {
					prodCombo.addItem("전체");
					add(prodCombo);
				}
				else {
					prodtf[i - 1] = new JTextField();
					add(prodtf[i - 1]);
				}
			}
			add(new JLabel("카테고리"));
			add(prodType);
		}
	}// ProductPanel

	public class ShowPanel extends JPanel {

	      public ShowPanel() {

	         setLayout(new GridLayout(2, 1));
	         ta.setEditable(false);
	         add(new JScrollPane(ta));
	         JPanel pa = new JPanel();
	         for (int i = 0; i < 3; i++) {

	            btn[i] = new JButton(btn_str[i]);
	            btn[i].setBackground(Color.black);
	            btn[i].setFont(new Font("고딕체", Font.PLAIN, 20));
	            btn[i].setForeground(Color.WHITE);

	            pa.add(btn[i]);
	         }
	         add(pa);
	      }

	   }
	//싱글톤 객체 반환 메소드
	public static ProdManager getInstance() {
		if(PM == null) {
			PM = new ProdManager();
		}
		return PM;
	}
	
	//리스너 핸들러 연결 메소드
	public void addButtonActionListener(ActionListener listener) {
		previousBtn.addActionListener(listener);
		logoutBtn.addActionListener(listener);
		for(int i=0;i<3;i++)
			btn[i].addActionListener(listener);
		prodCombo.addActionListener(listener);
	}
}