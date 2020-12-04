package Controller;

// import java.util.Vector;

import javax.swing.JOptionPane;

import Model.AccountChecker_DTO;
import Model.CustomersDao;
import View.LoginView;

/*
LV0 : 이미 로그인 중이 아닌지?
LV1 : 아이디를 올바르게 입력 했는지?
LV2 : 비밀번호가 일치한지?
LV3 : 선택한 로그인 모드가 관리자/사용자로 적절하게 선택했는지?
LV4 : LOGIN_SUCCESS (로그인 성공).
 */

public class AccountChecker {
    private final String[] errorMessages = {"없는 아이디 입니다.", "이미 로그인중 입니다.", "비밀번호가 일치하지 않습니다.", "모드 접근이 잘못됐습니다."};
    private final String[] errorTitles = {"ERROR", "ACCESS DENIED"};
    private static final int ADMIN = 0;
    private static final int USER = 1;
    private static AccountChecker accountChecker = null;
    private static int validationPointer;
    static final int LOGIN_SUCCESS = 4;

    // 참조 객체 선언부
    public static CustomersDao _customerDao = CustomersDao.getInstance();
    public static LoginView _loginView = LoginView.getInstance();
    public static C_login _loginController = PCController.cl;

    // Accessor & Mutator
    public void gotoNextLevel() {
        validationPointer++;

        return;
    }

    public void goBackToFirstLevel() {
        validationPointer = 0;

        return;
    }

    public int getValidationPointer() {
        return validationPointer;
    }

    private AccountChecker() {
    }

    public static AccountChecker getInstance() {
        if (accountChecker == null) {
            accountChecker = new AccountChecker();
            validationPointer = 0;
        }

        return accountChecker;
    }

    public void loadReferenceObjects() {
        _loginController = PCController.cl;
        return;
    }

    public void runAccountChecker() {
        loadReferenceObjects();

        int modeflagParameter = (_loginView.server.isSelected()) ? ADMIN : USER;
        AccountChecker_DTO accountCheckerDto = _loginController.stepIntoTryLogin(_loginView.loginTextField.getText(), _loginView.passwordField.getText(), modeflagParameter);
        for (int i = 0; i < LOGIN_SUCCESS; i++) {
            String errorMsg = isItValidationAccount(i, accountCheckerDto, _loginView.passwordField.getText(), modeflagParameter);

            if(errorMsg != null){
                accountChecker.goBackToFirstLevel();
                String errorTitle = (i == 3) ? errorTitles[1] : errorTitles[0];
                JOptionPane.showMessageDialog(null, errorMsg, errorTitle, JOptionPane.ERROR_MESSAGE);
                return;                
            } 
            else accountChecker.gotoNextLevel();
        }

        //! This code has been depricated...
        /*if (doesYourIdExist(accountChecker_dto))
            accountChecker.gotoNextLevel();
        else {
            accountChecker.goBackToFirstLevel();
            JOptionPane.showMessageDialog(null, "없는 아이디 입니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isAlreadyLogined(accountChecker_dto))
            accountChecker.gotoNextLevel();
        else {
            accountChecker.goBackToFirstLevel();
            JOptionPane.showMessageDialog(null, "이미 로그인중 입니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isPasswordCorrect(accountChecker_dto, LV.passwordField.getText()))
            accountChecker.gotoNextLevel();
        else {
            accountChecker.goBackToFirstLevel();
            JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isValidModeAccess(accountChecker_dto, modeflag_parameter))
            accountChecker.gotoNextLevel();
        else {
            accountChecker.goBackToFirstLevel();
            JOptionPane.showMessageDialog(null, "모드 접근이 잘못됐습니다.", "ACCESS DENIED", JOptionPane.ERROR_MESSAGE);
            return;
        }

        return;
        */
    }

    private String isItValidationAccount(int Level, AccountChecker_DTO accountCheckerDto, String password, int modeflagParameter) {
        boolean validationResultFlag = true; // true면 로그인 실패, false면 로그인 성공.

        try {
            switch(Level) {
                case 0:
                    validationResultFlag = doesYourIdNotExist(accountCheckerDto);
                    break;
                case 1:
                    validationResultFlag = isAlreadyLogined(accountCheckerDto);
                    break;
                case 2:
                    validationResultFlag = isPasswordNotCorrect(accountCheckerDto, password);
                    break;
                case 3:
                    validationResultFlag = isInValidModeAccess(accountCheckerDto, modeflagParameter);
                    break;
                default:
                break;
            }
        } catch(NullPointerException e) {
            validationResultFlag = true;
        }
        return (validationResultFlag) ? errorMessages[Level] : null;
    }

    private boolean isAlreadyLogined(AccountChecker_DTO accountChecker_dto) {
        return CustomersDao.get_check(accountChecker_dto.getCustomerIdetification());
    }

    private boolean isInValidModeAccess(AccountChecker_DTO accountChecker_dto, int mode_flag) {
        return (accountChecker_dto.getCustomerMode() != mode_flag) ? true : false;
    }

    private boolean isPasswordNotCorrect(AccountChecker_DTO accountChecker_dto, String input_password) {
        return (accountChecker_dto.getCustomerPassword().equals(input_password)) ? false : true;
    }

    private boolean doesYourIdNotExist(AccountChecker_DTO dto) {
        return (dto == null) ? true : false;
    }

}
