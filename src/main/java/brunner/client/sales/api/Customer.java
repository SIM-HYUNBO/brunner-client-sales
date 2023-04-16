package brunner.client.sales.api;

import com.google.gson.JsonObject;

import brunner.client.api.BrunnerClientApi;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class Customer extends BrunnerClientApi {

	static Customer instance;

	public static Customer getInstance() {
		return instance == null ? new Customer(1000) : instance;
	}

	public Customer(int requestTimeoutMs) {
		super(requestTimeoutMs);
	}

	/***
	 * 사용자 고객 목록 조회
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public JsonObject viewUserCustomerList(RPCClient client, JsonObject jConnectionInfo, String systemCode,
			String userId) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Customer",
				"TXN_Customer_viewUserCustomerList",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId"
				},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode,
						userId
				});

		String queueName = jConnectionInfo.get(BrunnerClientApi.msgFieldName_queueName).getAsString();

		String reply = client.requestSync(
				queueName,
				request.toString(),
				10000);

		JsonObject jReply = (JsonObject) parser.parse(reply);

		return jReply;
	}

	/***
	 * 특정 시스템 코드 사용자의 고객 정보 등록
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @param customerId
	 * @param salesType
	 * @param salesCategory
	 * @param registerName
	 * @param registerNo
	 * @param address
	 * @param phoneNumber
	 * @return
	 * @throws Exception
	 */
	public JsonObject registerCustomer(RPCClient client, JsonObject jConnectionInfo, String systemCode, String userId,
			String customerId, String salesType, String salesCategory, String registerName, String registerNo,
			String address, String phoneNumber) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Customer",
				"TXN_Customer_registerCustomer",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId",
						"customerId",
						"salesType",
						"salesCategory",
						"registerName",
						"registerNo",
						"address",
						"phoneNumber"
				},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode,
						userId,
						customerId,
						salesType,
						salesCategory,
						registerName,
						registerNo,
						address,
						phoneNumber
				});

		String queueName = jConnectionInfo.get(BrunnerClientApi.msgFieldName_queueName).getAsString();

		String reply = client.requestSync(
				queueName,
				request.toString(),
				10000);

		JsonObject jReply = (JsonObject) parser.parse(reply);

		return jReply;
	}

	/***
	 * 특정 시스템 코드 사용자의 고객 정보 삭제
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public JsonObject unregisterCustomer(RPCClient client, JsonObject jConnectionInfo, String systemCode, String userId,
			String customerId) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Customer",
				"TXN_Customer_unregisterCustomer",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId",
						"customerId"
				},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode,
						userId,
						customerId
				});

		String queueName = jConnectionInfo.get(BrunnerClientApi.msgFieldName_queueName).getAsString();

		String reply = client.requestSync(
				queueName,
				request.toString(),
				10000);

		JsonObject jReply = (JsonObject) parser.parse(reply);

		return jReply;
	}
}
