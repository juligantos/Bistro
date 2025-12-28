package entities;

import java.io.Serializable;

import enums.UserType;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;           // users.user_id
    private UserType userType;    // GUEST / MEMBER / EMPLOYEE / MANAGER

    private String phoneNumber;   // users.phoneNumber (nullable)
    private String email;         // users.email (nullable)

    // Member-only (nullable for non-members)
    private String memberCode;    // members.member_code
    private String firstName;     // members.f_name
    private String lastName;      // members.l_name

    public User() {}

    // Generic user (guest/employee/manager)
    public User(int userId, String phoneNumber, String email, UserType userType) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userType = userType;
    }

    // Member profile (users + members join)
    public User(int userId, String memberCode, String firstName, String lastName,
                String phoneNumber, String email) {
        this.userId = userId;
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userType = UserType.MEMBER;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMemberCode() { return memberCode; }
    public void setMemberCode(String memberCode) { this.memberCode = memberCode; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
