package brunner.client.sales.api;

import com.google.gson.JsonObject;

import brunner.client.api.BrunnerClientApi;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class Vendor extends BrunnerClientApi {

	static Vendor instance;

	public static Vendor getInstance() {
		return instance == null ? new Vendor(1000) : instance;
	}

	public Vendor(int requestTimeoutMs) {
		super(requestTimeoutMs);
	}

	/***
	 * 사용자 구매처 목록 조회
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public JsonObject viewUserVendorList(RPCClient client, JsonObject jConnectionInfo, String systemCode, String userId) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Vendor",
				"TXN_Vendor_viewUserVendorList",
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
	 * 특정 시스템 코드 사용자의 구매처 정보 등록
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @param vendorId
	 * @param salesType
	 * @param salesCategory
	 * @param registerName
	 * @param registerNo
	 * @param address
	 * @param phoneNumber
	 * @return
	 * @throws Exception
	 */
	public JsonObject registerVendor(RPCClient client, JsonObject jConnectionInfo, String systemCode, String userId,
			String vendorId, String salesType, String salesCategory, String registerName, String registerNo,
			String address, String phoneNumber) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Vendor",
				"TXN_Vendor_registerVendor",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId",
						"vendorId",
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
						vendorId,
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
	 * 특정 시스템 코드 사용자의 구매처 정보 삭제
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @param vendorId
	 * @return
	 * @throws Exception
	 */
	public JsonObject unregisterVendor(RPCClient client, JsonObject jConnectionInfo, String systemCode, String userId,
			String vendorId) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Vendor",
				"TXN_Vendor_unregisterVendor",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId",
						"vendorId"
				},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode,
						userId,
						vendorId
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
