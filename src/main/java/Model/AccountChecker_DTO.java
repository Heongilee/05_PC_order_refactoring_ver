package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountChecker_DTO {
    private String customerIdetification;
    private String customerPassword;
    private Integer customerMode;

    // Accessor & Mutator
	public String getCustomerIdetification() {
		return customerIdetification;
	}
	public Integer getCustomerMode() {
		return customerMode;
	}
	public void setCustomerMode(Integer customerMode) {
		this.customerMode = customerMode;
	}
	public String getCustomerPassword() {
		return customerPassword;
	}
	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}
	public void setCustomerIdetification(String customerIdetification) {
		this.customerIdetification = customerIdetification;
    }
    
    private AccountChecker_DTO(String id, String pw, Integer mode) {
        this.customerIdetification = id;
        this.customerPassword = pw;
        this.customerMode = mode;
    }

    public static AccountChecker_DTO createAccountChecker_DTO(ResultSet rs) throws SQLException {
        return (rs.next()) ? new AccountChecker_DTO(rs.getString(1), rs.getString(2), rs.getInt(3)) : null;
    }

    public String toString() {
        return "customerIdetification : " + customerIdetification + ", customerPassword : " + customerPassword + ", customerMode : " + ((customerMode == 0) ? "ADMIN" : "USER");
    }
}
