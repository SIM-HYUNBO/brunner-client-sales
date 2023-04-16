package brunner.client.sales.api;

import com.google.gson.JsonObject;

import brunner.client.api.BrunnerClientApi;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class Material extends BrunnerClientApi {

	static Material instance;

	public static Material getInstance() {
		return instance == null ? new Material(1000) : instance;
	}

	public Material(int requestTimeoutMs) {
		super(requestTimeoutMs);
	}

	/***
	 * 업종별 자재 목록 조회
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param salesCategory
	 * @return
	 * @throws Exception
	 */
	public JsonObject viewMaterialList(RPCClient client, 
									   JsonObject jConnectionInfo, 
									   String systemCode, 
									   String userId)
			throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Material",
				"TXN_Material_viewMaterialList",
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

		String reply = client.requestSync(queueName,
										  request.toString(),
										  10000);

		JsonObject jReply = (JsonObject) parser.parse(reply);

		return jReply;
	}

	/***
	 * 특정 시스템 코드 업종의 자재 등록
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param userId
	 * @param systemCode
	 * @param salesCategory
	 * @param materialName
	 * @param materialUnit
	 * @param materialDesc
	 * @param unitPrice
	 * @return
	 * @throws Exception
	 */
	public JsonObject registerMaterial(RPCClient client, 
									   JsonObject jConnectionInfo, 
									   String userId, 
									   String systemCode,
									   String materialName, 
									   String materialUnit, 
									   String materialDesc, 
									   int unitPrice) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Material",
				"TXN_Material_registerMaterial",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId",
						"materialName",
						"materialUnit",
						"unitPrice",
						"materialDesc"
				},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode,
						userId,
						materialName,
						materialUnit,
						unitPrice,
						materialDesc
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
	 * 특정 시스템 코드 업종의 자재 삭제
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param userId
	 * @param systemCode
	 * @param salesCategory
	 * @param materialName
	 * @param materialUnit
	 * @return
	 * @throws Exception
	 */
	public JsonObject unregisterMaterial(RPCClient client, 
										 JsonObject jConnectionInfo, 
										 String userId, 
										 String systemCode,
										 String materialName, 
										 String materialUnit) throws Exception {

		JsonObject request = getMessage(
				"RPC Request",
				"com.brunner.service.sales",
				"SVC_Material",
				"TXN_Material_unregisterMaterial",
				10000,
				new String[] {
						"txnId",
						"systemCode",
						"userId",
						"materialName",
						"materialUnit"
				},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode,
						userId,
						materialName,
						materialUnit
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
