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
public class ProdManager extends JPanel {
   public static ProdManager _productManager = ProdManager.getInstance();
   LoginView _loginView = LoginView.getInstance();
   JLabel title = new JLabel("상품관리");
   public JLabel noticeTextLabel = new JLabel("## 메시지 : ");
   JPanel leftPanel = new JPanel();
   JPanel rightPanel = new JPanel();
   JPanel wrapPanel = new JPanel();
   JPanel northPanel = new JPanel();
   
   public JTextField[] productTextFields = new JTextField[3];
   public Vector<Model.Product_DTO> productVector = new Vector<Model.Product_DTO>();
   public JComboBox<String> productComboBox = new JComboBox<String>();

   String[] categoryListLabels = {"라면류", "음료류", "간식류", "과자류"};
   public JComboBox<String> categories = new JComboBox<String>(categoryListLabels);

   public JTextArea productAttributes = new JTextArea("관리번호\t상품명\t단가\t제조사", 27, 30);

   final int _buttonNumber = 3;
   public JButton[] crudButton = new JButton[_buttonNumber];

   private ProductPanel _ProductPanel = new ProductPanel();
   private ShowPanel _ShowPanel = new ShowPanel();

   
   private ProdManager() {
      setLayout(new BorderLayout());
      
      makePanel();
      makeSetting();
   
      setSize(900, 700);
   }
   
   public void makePanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      
      wrapPanel.setLayout(new GridLayout(1,2));
      leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
      // 오른쪽 정렬
      leftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

      northPanel.add(title);
      northPanel.add(noticeTextLabel);
      
      leftPanel.add(_ProductPanel);
      wrapPanel.add(leftPanel);

      rightPanel.add(_ShowPanel);
      wrapPanel.add(rightPanel);
      
      panel.add(northPanel, BorderLayout.NORTH);
      panel.add(wrapPanel, BorderLayout.CENTER);
      
      add(panel, BorderLayout.CENTER);
   }
   
   public void makeSetting() {
      // title 폰트 크기
      title.setFont(new Font("굴림", Font.BOLD, 45));
      title.setAlignmentX(CENTER_ALIGNMENT);

      noticeTextLabel.setAlignmentX(CENTER_ALIGNMENT);
      noticeTextLabel.setFont(new Font("고딕체", Font.ITALIC, 14));
      noticeTextLabel.setForeground(Color.BLACK); // 메세지 뜰때마다 빨강으로 전환하기
   }
   public class ProductPanel extends JPanel {
      String[] productLabelStrings = { "상품 번호", "상품명", "단가", "제조사" };
      JLabel[] productLabels = new JLabel[4];

      public ProductPanel() {
         setLayout(new GridLayout(5, 2, 10, 40));
         setPreferredSize(new Dimension((int) (400), (int) (400)));
         makeProductCombobox();
         
         for (int i = 1; i < 4; i++) {
            productLabels[i] = makeLabel(i);
            add(productLabels[i]);
            productTextFields[i - 1] = new JTextField();
            add(productTextFields[i - 1]);
         }
         add(new JLabel("카테고리"));
         add(categories);
      }   
      
       public JLabel makeLabel(int num) {
            JLabel temp=new JLabel(productLabelStrings[num]);
            temp.setFont(new Font("고딕체", Font.PLAIN, 16));
            return temp;
       }
       public void makeProductCombobox() {
         productLabels[0] = makeLabel(0);
         add(productLabels[0]);
         productComboBox.addItem("전체");
         add(productComboBox);
       }
   }

   public class ShowPanel extends JPanel {
      String[] crudButtonStrings = {"등록", "조회", "삭제"};
      JPanel showPanel = new JPanel();
      
         public ShowPanel() {
            setLayout(new GridLayout(2, 1));
            productAttributes.setEditable(false);
            add(new JScrollPane(productAttributes));
          
            for (int i = 0; i < _buttonNumber; i++) {
               crudButton[i] = makeButton(i);
               showPanel.add(crudButton[i]);
            }      
            add(showPanel);
         }
         
         public JButton makeButton(int num) {
            JButton temp=new JButton(crudButtonStrings[num]);
            temp.setBackground(Color.black);
            temp.setFont(new Font("고딕체", Font.PLAIN, 20));
            temp.setForeground(Color.WHITE);
            return temp;
         }
      }
   
   
   
   //싱글톤 객체 반환 메소드
   public static ProdManager getInstance() {
      if(_productManager == null) {
         _productManager = new ProdManager();
      }
      return _productManager;
   }
   
   //리스너 핸들러 연결 메소드
   public void addButtonActionListener(ActionListener listener) {
      for(int i=0;i<3;i++)
         crudButton[i].addActionListener(listener);
      productComboBox.addActionListener(listener);
   }

}