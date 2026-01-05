package logic;

import comms.Api;
import comms.Message;
import entities.MonthlyReport;

public class MonthlyReportsController {
	
	private final BistroClient client;
	
	private MonthlyReport currentMonthlyReport;
	
	public MonthlyReportsController(BistroClient bistroClient) {
		this.client = bistroClient;
	}
	
	public MonthlyReport getCurrentMonthlyReport() {
		return currentMonthlyReport;
	}
	
	public void setCurrentMonthlyReport(MonthlyReport currentMonthlyReport) {
		this.currentMonthlyReport = currentMonthlyReport;
	}

	public void requestMonthlyReportData() {
		client.handleMessageFromClientUI(new Message(Api.ASK_MONTHLY_REPORT_DATA, null));
	}

}
