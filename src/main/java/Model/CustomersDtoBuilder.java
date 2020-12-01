package Model;

public class CustomersDtoBuilder {
	private String customerId; // 고객 아이디
	private String customerPassword; // 고객 비밀번호
	private String customerNickName; // 고객 닉네임
	private String customerEmail; // 고객 이메일
	private Integer customerBalance; // 고객 잔액

	public CustomersDtoBuilder setCustomerId(String customerId) {
		this.customerId = customerId;

		return this;
	}

	public CustomersDtoBuilder setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;

		return this;
	}

	public CustomersDtoBuilder setCustomerNickName(String customerNickName) {
		this.customerNickName = customerNickName;

		return this;
	}

	public CustomersDtoBuilder setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;

		return this;
	}

	public CustomersDtoBuilder setCustomerBalance(Integer customerBalance) {
		this.customerBalance = customerBalance;

		return this;
	}

	public CustomersDto build() {
		return new CustomersDto(customerId, customerPassword, customerNickName, customerEmail, customerBalance);
	}

}