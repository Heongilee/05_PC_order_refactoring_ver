package Model;

import javax.swing.JComponent;
import javax.swing.JTextArea;

public class PCChatData {
   public JTextArea msgOut;
   
   public void addObjCus(JComponent c) {
      //JTextArea의 데이터를 관리.
      //데이터 변경이 일어나 업데이트할 UI컴포넌트를 등록하는 메소드이다.(관리자 창)
      this.msgOut = (JTextArea) c;
   }
   public void addObjGUI(JComponent c) {
      //JTextArea의 데이터를 관리.
      //데이터 변경이 일어나 업데이트할 UI컴포넌트를 등록하는 메소드이다.(사용자 창)
      this.msgOut = (JTextArea) c;
   }
   public void refreshData(String msg) {
      //JTextArea창에 텍스트를 추가하는 작업만 한다.
      msgOut.append(msg);
   }
}