package entities;

public class Employee extends Member {
	
	private String password;
	public Employee(String phoneNumber, String email, int memberID, String memberCode, String firstName, String password) {
		super(phoneNumber, email, memberID, memberCode, firstName);
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
