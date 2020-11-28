package Controller;

import java.util.Vector;
import Model.Customers_DAO;
import View.LoginView;

/*
LV0 : 이미 로그인 중이 아닌지?
LV1 : 아이디를 올바르게 입력 했는지?
LV2 : 비밀번호가 일치한지?
LV3 : 선택한 로그인 모드가 관리자/사용자로 적절하게 선택했는지?
LV4 : LOGIN_SUCCESS (로그인 성공).
 */
public class AccountChecker {
    private static AccountChecker accountChecker = null;
    private static Vector<Integer> validationList = new Vector<Integer>();
    private static int validationPointer;
    static final int LOGIN_SUCCESS = 4;

    // 참조 객체 선언부
    public static Customers_DAO c_dao = Customers_DAO.getInstance();
    public static LoginView LV = LoginView.getInstance();
    public static C_login cl = PCController.cl;

    // Accessor & Mutator
    public Vector<Integer> getValidationList() {
        return validationList;
    }
    public void gotoNextLevel() {
        validationPointer++;

        return;
    }
    public void backtoFirstStep() {
        validationPointer = 0;

        return;
    }
    public int getValidationPointer() {
        return validationPointer;
    }

    private AccountChecker() {}

    public static AccountChecker getInstance() {
        if(accountChecker == null){
            accountChecker  = new AccountChecker();

            validationList.add(0);
            validationList.add(1);
            validationList.add(2);
            validationList.add(LOGIN_SUCCESS);
            validationPointer = 0;
        }

        return accountChecker;
    }

    public void loadReferenceObjects() {
        cl = PCController.cl;
        return ;
    }

    public void runAccountChecker() {
        loadReferenceObjects();
        int modeflag_parameter = (LV.server.isSelected()) ? 0 : 1;
        cl.stepIntoTryLogin(LV.loginTextField.getText(), LV.passwordField.getText(), modeflag_parameter);

        return ;
    }
}
