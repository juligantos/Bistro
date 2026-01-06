package logic.api.subjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entities.User;
import entities.UserData;
import logic.BistroClient;
import logic.BistroClientGUI;
import logic.UserController;
import logic.api.ClientRouter;
import enums.UserType;

import javafx.scene.control.Alert;
public class UserSubject {

	public static void register(ClientRouter router,UserController userController) {
		for (UserType type : UserType.values()) {
			String typeKey = type.name().toLowerCase();

			router.on("login", typeKey + ".ok", msg -> {
				BistroClient.awaitResponse = false;
				User user = (User) msg.getData();
				userController.setLoggedInUser(user);
			});

			router.on("signout", typeKey + ".ok", msg -> {
	            BistroClient.awaitResponse = false;
				userController.setLoggedInUser(null);
			});

			router.on("login", typeKey + ".notFound", msg -> {
	            BistroClient.awaitResponse = false;
			});

			router.on("signout", typeKey + ".fail", msg -> {
	            BistroClient.awaitResponse = false;
			});
		}
		router.on("member", "updateInfo.ok", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getUserCTRL().setLoggedInUser((User) msg.getData());
		});
		router.on("member", "updateInfo.fail", msg -> {
			BistroClient.awaitResponse = false;
		});
		router.on("user", "registerNewMember.ok", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getUserCTRL().setRegistrationSuccessFlag(true);
		});
		router.on("user", "registerNewMember.fail", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getUserCTRL().setRegistrationSuccessFlag(false);
		});
		router.on("member", "registerationStats.ok", msg -> {
            BistroClient.awaitResponse = false;
			ArrayList<Integer> count = (ArrayList<Integer>) msg.getData();
			BistroClientGUI.client.getUserCTRL().setMemberRegistrationStats(count);
		});
		router.on("member", "registerationStats.fail", msg -> {
            BistroClient.awaitResponse = false;
		});
		
		router.on("customers", "getalldata.ok", msg -> {
            BistroClient.awaitResponse = false;
			List<UserData> customersData = (List<UserData>) msg.getData();
			BistroClientGUI.client.getUserCTRL().setCustomersData(customersData);
		});


		router.on("customers", "getalldata.fail", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getUserCTRL().setCustomersData(new ArrayList<>());
		    Alert alert = new Alert(Alert.AlertType.ERROR);
		    alert.setTitle("Error");
		    alert.setHeaderText("Failed to Retrieve Customer Data");
		    alert.setContentText("An error occurred while retrieving customer data. Please try again later.");
		    alert.showAndWait();
		});

		
		// Employee creation response handlers
		router.on("staff", "create.ok", msg -> {
            BistroClient.awaitResponse = false;
			User newEmployee = (User) msg.getData();
			BistroClientGUI.client.getUserCTRL().setStaffCreationSuccess(true);
			BistroClientGUI.client.getUserCTRL().setStaffCreationErrorMessage(null);
		});
		
		router.on("staff", "create.invalidData", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getUserCTRL().setStaffCreationSuccess(false);
			BistroClientGUI.client.getUserCTRL().setStaffCreationErrorMessage("Invalid staff data provided. Please check all fields.");
		});
		
		router.on("staff", "create.usernameExists", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getUserCTRL().setStaffCreationSuccess(false);
			BistroClientGUI.client.getUserCTRL().setStaffCreationErrorMessage("Username already exists. Please choose a different username.");
		});
		
		router.on("staff", "create.failed", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getUserCTRL().setStaffCreationSuccess(false);
			BistroClientGUI.client.getUserCTRL().setStaffCreationErrorMessage("Failed to create staff account. Please try again.");
		});
		router.on("user", "forgotMemberID.ok", msg -> {
            BistroClient.awaitResponse = false;
		    String memberID = (String) msg.getData();
		    BistroClientGUI.client.getUserCTRL().handleForgotIDResponse(memberID);
		    });
		
		router.on("user", "forgotMemberID.fail", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getUserCTRL().handleForgotIDResponse("NOT_FOUND");
		    });
		router.on("reservation", "forgotConfirmationCode.ok", msg -> {
            BistroClient.awaitResponse = false;
		    String confirmationCode = (String) msg.getData();
		    BistroClientGUI.client.getReservationCTRL().handleForgotConfirmationCodeResponse(confirmationCode);
		});
		router.on("reservation", "forgotConfirmationCode.fail", msg -> {
            BistroClient.awaitResponse = false;
			BistroClientGUI.client.getReservationCTRL().handleForgotConfirmationCodeResponse("NOT_FOUND");
		});
		
	}
}
