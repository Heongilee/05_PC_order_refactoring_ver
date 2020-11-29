package Controller;

import java.util.Vector;

import javax.swing.JOptionPane;

import Model.AccountChecker_DTO;
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
	private static final int ADMIN = 0;
	private static final int USER = 1;

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

        int modeflag_parameter = (LV.server.isSelected()) ? ADMIN : USER;
        AccountChecker_DTO accountChecker_dto = cl.stepIntoTryLogin(LV.loginTextField.getText(), LV.passwordField.getText(), modeflag_parameter);
        // if(accountChecker_dto != null) System.out.println("TEST OUTPUT : " + accountChecker_dto.toString());

        if(doesYourIdExist(accountChecker_dto)) accountChecker.gotoNextLevel();
        else {
           accountChecker.backtoFirstStep();
           JOptionPane.showMessageDialog(null, "없는 아이디 입니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
           return ;
        }
        if(isAlreadyLogined(accountChecker_dto)) accountChecker.gotoNextLevel();
        else {
           accountChecker.backtoFirstStep();
           JOptionPane.showMessageDialog(null, "이미 로그인중 입니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
           return ;
        }
        if(isPasswordCorrect(accountChecker_dto, LV.passwordField.getText())) accountChecker.gotoNextLevel();
        else {
           accountChecker.backtoFirstStep();
           JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
           return ;
        }
        if(isValidModeAccess(accountChecker_dto, modeflag_parameter)) accountChecker.gotoNextLevel();
        else {
           accountChecker.backtoFirstStep();
           JOptionPane.showMessageDialog(null, "모드 접근이 잘못됐습니다.", "ACCESS DENIED", JOptionPane.ERROR_MESSAGE);
           return ;
        }

        return ;
    }
    private boolean isAlreadyLogined(AccountChecker_DTO accountChecker_dto) {
        return Customers_DAO.get_check(accountChecker_dto.getCustomerIdetification());
    }

 private boolean isValidModeAccess(AccountChecker_DTO accountChecker_dto, int mode_flag) {
        return (accountChecker_dto.getCustomerMode() == mode_flag) ? true : false;
    }

 private boolean isPasswordCorrect(AccountChecker_DTO accountChecker_dto, String input_password){
        return (accountChecker_dto.getCustomerPassword().equals(input_password)) ? true : false;
    }

 private boolean doesYourIdExist(AccountChecker_DTO dto) {
      return (dto != null) ? true : false;
    }

}
