package Controller;

import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;

import Model.Customers_DAO;
import Model.Message;
import Model.ViewState;
import View.CusManager;
import View.GUIView;
import View.LoginView;
import View.ProdManager;
import View.SignUpView;

/* 싱글톤 패턴으로 툴바를 관리하기 위한 클래스. */
public class ToolBar {
    private static ToolBar toolbar = null;
    private static ViewState viewState = ViewState.getInstance();

    // 참조 객체 선언부
    public static final LoginView LV = LoginView.getInstance();
    public static final CusManager CM = CusManager.getInstance();
    public static final SignUpView SUV = SignUpView.getInstance();
    public static Customers_DAO c_dao = Customers_DAO.getInstance();
    public static final ProdManager PM = ProdManager.getInstance();
    
    // 로그아웃 기능에 필요한 참조객체
    public static final GUIView GUI = GUIView.getInstance();
    public static Gson gson = PCController.gson;
    public static PrintWriter outMsg = PCController.outMsg;
    public static BufferedReader inMsg = PCController.inMsg;
    public static Socket socket = PCController.socket;
    public static boolean status = PCController.status;
    

    private ToolBar() {}

    public static ToolBar getInstance() {
        if(toolbar == null){
            toolbar = new ToolBar();
        }
        return toolbar;
    }

    void loadReferenceObject_for_logoutBtn() {
        gson = PCController.gson;
        outMsg = PCController.outMsg;
        inMsg = PCController.inMsg;
        socket = PCController.socket;
        status = PCController.status;

        return;
    }
    
    void clearSignupForm() {
        LV.signUpView.IdField.setText("");
        LV.signUpView.PassField.setText("");
        LV.signUpView.NameField.setText("");
        LV.signUpView.EmailField.setText("");
        
        return;
    }
    
    void logoutFromProdManager() {
        // 참조 객체 리로드
        loadReferenceObject_for_logoutBtn();

        c_dao.make_check(LV.loginTextField.getText()); //DB 체크값을 바꿔준다.
        LV.cardLayout.show(LV.window, "layer");
        setVisibleToolBar(false);
        LV.setSize(700, 600);

        return ;
    }
    
    void logoutFromCusManager() {
        // 참조 객체 리로드
        loadReferenceObject_for_logoutBtn();
        
        outMsg.println(gson.toJson(new Message(GUI.seat, GUI.id, "", "", "adminlogout", "")));
        CM.chatContent.setText("");
        c_dao.make_check(LV.loginTextField.getText()); //DB 체크값을 바꿔준다.
        LV.cardLayout.show(LV.window, "layer");
        LV.setSize(700, 600);
        try {
            outMsg.close();
            inMsg.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PCController.status = status = false;

        return ;
    }

    void logoutFromGUIView() {
        // 참조 객체 리로드
        loadReferenceObject_for_logoutBtn();

        outMsg.println(gson.toJson(new Message(GUI.seat, GUI.id, "", "", "logout", "adminlogin")));
        GUI.msgInput.setText("");
        c_dao.make_check(LV.loginTextField.getText());
        LV.cardLayout.show(LV.window, "layer");
        LV.setSize(700, 600);
        try {
           outMsg.close();
           inMsg.close();
           socket.close();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        PCController.status = status = false;
    }

    void toolBarController(String currentPage) {
        switch(currentPage) {
            case "SignUpView": // cardLayout/"layeredpane"
                SUV.setVisible(false);
                LV.setVisible(true);
                LV.cardLayout.show(LV.window, "layer");
                setVisibleToolBar(false);

                viewState.setCurrent_view_state(viewState.getviewStateList("LoginView"));
                System.out.println(viewState.getCurrent_view_state());
                break;
            case "CusManager":
                LV.cardLayout.show(LV.window, "admin");
                LV.setSize(700, 600);
                setVisibleToolBar(false);
                
                viewState.setCurrent_view_state(viewState.getviewStateList("AdminView"));
                System.out.println(viewState.getCurrent_view_state());
                break;
            case "ProdManager":
                LV.cardLayout.show(LV.window, "admin");
                LV.setSize(700, 600);
                setVisibleToolBar(false);
                
                viewState.setCurrent_view_state(viewState.getviewStateList("AdminView"));
                System.out.println(viewState.getCurrent_view_state());
                break;
            case "AdminView": // cardLayout/"admin"
                break;
        }
        return;
    }

    /* true면 보이게, false면 감추게... */
    void setVisibleToolBar(boolean flag) {
        LV.bar.setVisible(flag);
    }

    /* 카드레이아웃에서 로그인 뷰를 불러옴. */
    void gotoLoginView() {
        LV.cardLayout.show(LV.window, "layer");
        return ;
    }

    /* 텍스트 필드에 적힌 아이디값을 가지고 UPDATE문을 수행해 로그아웃 상태(check)로 변경함. */
    void changeToLogout() {
        c_dao.make_check(LV.loginTextField.getText());
        return ;
    }
}
