package Model;

//CUSTOMERS 테이블에서 SQL문에 맞는 레코드 정보를 가져와 담을 DTO 클래스.
public class CustomersDto {
	private String customerId; // 고객 아이디
	private String customerPassword; // 고객 비밀번호
	private String customerNickName; // 고객 닉네임
	private String customerEmail; // 고객 이메일
	private Integer customerBalance; // 고객 잔액

	public CustomersDto(String customerId, String customerPassword, String customerNickName, String customerEmail,Integer customerBalance) {
		this.customerId = customerId;
		this.customerPassword = customerPassword;
		this.customerNickName = customerNickName;
		this.customerEmail = customerEmail;
		this.customerBalance = customerBalance;

		return;
	}

	public String getCustomerId() {
		return customerId;
	}

	public Integer getCustomerBalance() {
		return customerBalance;
	}

	public void setCustomerBalance(Integer customerBalance) {
		this.customerBalance = customerBalance;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerNickName() {
		return customerNickName;
	}

	public void setCustomerNickName(String customerNickName) {
		this.customerNickName = customerNickName;
	}

	public String getCustomerPassword() {
		return customerPassword;
	}

	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[CustomersDto] customerId="+ customerId +", customerPassword="+ customerPassword +", customerNickName="+ customerNickName +", customerEmail="+ customerEmail +", customerBalance=" + customerBalance;
	}
}