package brunner.client.sales.api;

import com.google.gson.JsonObject;

import brunner.client.api.BrunnerClientApi;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class Product extends BrunnerClientApi {

	static Product instance;

	public static Product getInstance() {
		return instance == null ? new Product(1000) : instance;
	}

	public Product(int requestTimeoutMs) {
		super(requestTimeoutMs);
	}

	/***
	 * 업종별 제품 목록 조회
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param salesCategory
	 * @return
	 * @throws Exception
	 */
	public JsonObject viewProductList(RPCClient client, JsonObject jConnectionInfo, String systemCode, String userId)
			throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Product",
				"TXN_Product_viewProductList",
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
	 * 특정 시스템 코드 업종의 제품 등록
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param userId
	 * @param systemCode
	 * @param salesCategory
	 * @param productName
	 * @param productUnit
	 * @param productDesc
	 * @param unitPrice
	 * @return
	 * @throws Exception
	 */
	public JsonObject registerProduct(RPCClient client, JsonObject jConnectionInfo, String userId, String systemCode,
			String productName, String productUnit, String productDesc, int unitPrice) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Product",
				"TXN_Product_registerProduct",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId",
						"productName",
						"productUnit",
						"unitPrice",
						"productDesc"
				},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode,
						userId,
						productName,
						productUnit,
						unitPrice,
						productDesc
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
	 * 특정 시스템 코드 업종의 제품 삭제
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param userId
	 * @param systemCode
	 * @param salesCategory
	 * @param productName
	 * @param productUnit
	 * @return
	 * @throws Exception
	 */
	public JsonObject unregisterProduct(RPCClient client, JsonObject jConnectionInfo, String userId, String systemCode,
			String productName, String productUnit) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Product",
				"TXN_Product_unregisterProduct",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId",
						"productName",
						"productUnit"
				},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode,
						userId,
						productName,
						productUnit
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
