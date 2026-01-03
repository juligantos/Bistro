package logic.api.subjects;

import java.util.ArrayList;

import entities.Order;
import entities.User;
import logic.BistroClient;
import logic.BistroClientGUI;
import logic.UserController;
import logic.api.ClientRouter;
import enums.UserType;

public class UserSubject {

	public static void register(ClientRouter router) {
		UserController userController = BistroClientGUI.client.getUserCTRL();
		for (UserType type : UserType.values()) {
			String typeKey = type.name().toLowerCase();

			router.on("login", typeKey + ".ok", msg -> {
				User user = (User) msg.getData();
				userController.setLoggedInUser(user);
			});

			router.on("signout", typeKey + ".ok", msg -> {
				userController.setLoggedInUser(null);
			});

			router.on("login", typeKey + ".notFound", msg -> {
			});

			router.on("signout", typeKey + ".fail", msg -> {
			});
		}
		router.on("member", "updateInfo.ok", msg -> {
			BistroClientGUI.client.getUserCTRL().setLoggedInUser((User) msg.getData());
		});
		router.on("member", "updateInfo.fail", msg -> {
		});
		router.on("user", "registerNewMember.ok", msg -> {
			BistroClientGUI.client.getUserCTRL().setRegistrationSuccessFlag(true);
		});
		router.on("user", "registerNewMember.fail", msg -> {
			BistroClientGUI.client.getUserCTRL().setRegistrationSuccessFlag(false);
		});
		router.on("member", "registerationStats.ok", msg -> {
			ArrayList<Integer> count = (ArrayList<Integer>) msg.getData();
			BistroClientGUI.client.getUserCTRL().setMemberRegistrationStats(count);
		});
		router.on("member", "registerationStats.fail", msg -> {
		});
	}
}
