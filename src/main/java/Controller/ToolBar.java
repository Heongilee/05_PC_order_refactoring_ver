package Controller;

import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;

import Model.CustomersDao;
import Model.Message;
import Model.ViewState;
import View.CusManager;
import View.GUIView;
import View.LoginView;
import View.ProdManager;
import View.SignUpView;

/* 싱글톤 패턴으로 툴바를 관리하기 위한 클래스. */
public class ToolBar {
    private static ToolBar toolBar = null;
    private static ViewState viewState = ViewState.getInstance();

    // 참조 객체 선언부
    public static final LoginView _loginView = LoginView.getInstance();
    public static final CusManager _CusManagerView = CusManager.getInstance();
    public static final SignUpView _signUpView = SignUpView.getInstance();
    public static CustomersDao _customerDao = CustomersDao.getInstance();
    public static final ProdManager _prodManagerView = ProdManager.getInstance();

    // 로그아웃 기능에 필요한 참조객체
    public static final GUIView _guiView = GUIView.getInstance();
    public static Gson _gson = PCController.gson;
    public static PrintWriter _outMsg = PCController.outMsg;
    public static BufferedReader _inMsg = PCController.inMsg;
    public static Socket _socket = PCController.socket;
    public static boolean _status = PCController.status;

    private ToolBar() {
    }

    public static ToolBar getInstance() {
        if (toolBar == null) {
            toolBar = new ToolBar();
        }
        return toolBar;
    }

    void loadReferenceObjectForLogoutBtn() {
        _gson = PCController.gson;
        _outMsg = PCController.outMsg;
        _inMsg = PCController.inMsg;
        _socket = PCController.socket;
        _status = PCController.status;

        return;
    }

    void clearSignupForm() {
        _signUpView.identificationTextField.setText("");
        _signUpView.passwordTextField.setText("");
        _signUpView.nameTextField.setText("");
        _signUpView.emailTextField.setText("");

        return;
    }

    //! This code has been deprecated... 
    /*void logoutFromProdManager() {
        // 참조 객체 리로드
        loadReferenceObject_for_logoutBtn();

        c_dao.make_check(LV.loginTextField.getText()); // DB 체크값을 바꿔준다.
        LV.cardLayout.show(LV.window, "layer");
        setVisibleToolBar(false);
        LV.setSize(700, 600);
        
        return;
    }
    
    void logoutFromCusManager() {
        // 참조 객체 리로드
        loadReferenceObject_for_logoutBtn();
        
        outMsg.println(gson.toJson(new Message(GUI.seat, GUI.id, "", "", "adminlogout", "")));
        CM.chatContent.setText("");
        c_dao.make_check(LV.loginTextField.getText()); // DB 체크값을 바꿔준다.
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
        
        return;
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
    */
    
    void prevButtonController(String currentPage) {
        switch (currentPage) {
            case "SignUpView": // cardLayout/"layeredpane"
                _signUpView.setVisible(false);
                _loginView.setVisible(true);
                _loginView.cardLayout.show(_loginView.window, "layer");
                setVisibleToolBar(false);

                viewState.setCurrent_view_state("LoginView");
                System.out.println(viewState.getCurrentViewState());
                break;
            case "CusManagerView":
                _loginView.cardLayout.show(_loginView.window, "admin");
                _loginView.setSize(700, 600);
                setVisibleToolBar(false);

                viewState.setCurrent_view_state("AdminView");
                System.out.println(viewState.getCurrentViewState());
                break;
            case "ProdManagerView":
                _loginView.cardLayout.show(_loginView.window, "admin");
                _loginView.setSize(700, 600);
                setVisibleToolBar(false);

                viewState.setCurrent_view_state("AdminView");
                System.out.println(viewState.getCurrentViewState());
                break;
            case "AdminView": // cardLayout/"admin"
                break;
        }
        return;
    }

    /* true면 보이게, false면 감추게... */
    void setVisibleToolBar(boolean flag) {
        _loginView.bar.setVisible(flag);
    }

    /* 카드레이아웃에서 로그인 뷰를 불러옴. */
    void gotoLoginView() {
        _loginView.cardLayout.show(_loginView.window, "layer");
        return;
    }

    /* 텍스트 필드에 적힌 아이디값을 가지고 UPDATE문을 수행해 로그아웃 상태(check)로 변경함. */
    void changeToLogout() {
        _customerDao.make_check(_loginView.loginTextField.getText());
        return;
    }

    public void logoutButtonController(String currentPage) {
        // 참조 객체 리로드
        loadReferenceObjectForLogoutBtn();

        switch (currentPage) {
            case "GUIView":
                _outMsg.println(_gson.toJson(new Message(_guiView.seat, _guiView.id, "", "", "logout", "adminlogin")));
                _guiView.msgInput.setText("");
                break;
            case "CusManagerView":
                _outMsg.println(_gson.toJson(new Message(_guiView.seat, _guiView.id, "", "", "adminlogout", "")));
                _CusManagerView.chatContent.setText("");
                break;
            case "ProdManagerView":
                // nothing...
                break;
            default:
                break;
        }
        _customerDao.make_check(_loginView.loginTextField.getText()); // DB 체크값을 바꿔준다.
        _loginView.cardLayout.show(_loginView.window, "layer");
        _loginView.setSize(700, 600);
        clearAllOfLoginViewTextFields();
        
        try {
            _outMsg.close();
            _inMsg.close();
            _socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setVisibleToolBar(false);

        PCController.status = _status = false;
        return;
    }

    private void clearAllOfLoginViewTextFields() {
        _loginView.loginTextField.setText("");
        _loginView.passwordField.setText("");
    }
}
