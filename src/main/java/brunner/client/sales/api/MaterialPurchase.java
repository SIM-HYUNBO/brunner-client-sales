package brunner.client.sales.api;

import com.google.gson.JsonObject;

import brunner.client.api.BrunnerClientApi;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class MaterialPurchase extends BrunnerClientApi {

	static MaterialPurchase instance;
	public static MaterialPurchase getInstance() {
		return instance == null ? new MaterialPurchase(1000): instance;
	}	
	
	public MaterialPurchase(int requestTimeoutMs) {
		super(requestTimeoutMs);
	}

	/***
	 * 특정 시스템 코드 사용자의 매출 등록
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @param purchaseSerialNo
	 * @param purchaseDate
	 * @param vendorId
	 * @param materialName
	 * @param materialUnit
	 * @param unitPrice
	 * @param purchaseCount
	 * @param purchaseAmount
	 * @param paidAmount
	 * @param purchaseComment
	 * @return
	 * @throws Exception
	 */
	public JsonObject registerMaterialPurchase(
			RPCClient client, 
			JsonObject jConnectionInfo, 
			String systemCode,
			String userId,
			String purchaseSerialNo,
			String purchaseDate,
			String vendorId,
			String materialName,
			String materialUnit,
			int unitPrice,
			float purchaseCount,
			float purchaseAmount,
			float paidAmount,
			String purchaseComment
			) throws Exception {
		
		JsonObject request = getMessage(
				"RPC Request", 
				"com.brunner.service.sales", 
				"SVC_MaterialPurchase", 
				"TXN_MaterialPurchase_registerMaterialPurchase", 
				10000,
				new String[] {
						"txnId",
						"systemCode", 
						"userId", 
						"purchaseSerialNo",
						"purchaseDate", 
						"vendorId",
						"materialName",
						"materialUnit",
						"unitPrice",
						"purchaseCount",
						"purchaseAmount",
						"paidAmount",
						"purchaseComment"
						},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode, 
						userId, 
						purchaseSerialNo,
						purchaseDate, 
						vendorId,
						materialName,
						materialUnit,
						unitPrice,
						purchaseCount,
						purchaseAmount,
						paidAmount,
						purchaseComment
						}			
				);		
		
		String queueName = jConnectionInfo.get(BrunnerClientApi.msgFieldName_queueName).getAsString();
		
		String reply = client.requestSync(
				queueName, 
				request.toString(), 
				10000);
		
		JsonObject jReply = (JsonObject) parser.parse(reply);

		return jReply;
	}
	
	/***
	 * 특정 시스템 코드 사용자의 매출 삭제
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @param purchaseSerialNo
	 * @return
	 * @throws Exception
	 */
	public JsonObject unregisterMaterialPurchase(
			RPCClient client, 
			JsonObject jConnectionInfo, 
			String systemCode,
			String userId,
			String purchaseSerialNo
			) throws Exception {
		
		JsonObject request = getMessage(
				"RPC Request", 
				"com.brunner.service.sales", 
				"SVC_MaterialPurchase", 
				"TXN_MaterialPurchase_unregisterMaterialPurchase", 
				10000,
				new String[] {
						"txnId",
						"systemCode", 
						"userId", 
						"purchaseSerialNo"
						},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode, 
						userId, 
						purchaseSerialNo
						}			
				);				
		
		String queueName = jConnectionInfo.get(BrunnerClientApi.msgFieldName_queueName).getAsString();
		
		String reply = client.requestSync(
				queueName, 
				request.toString(), 
				10000);
		
		JsonObject jReply = (JsonObject) parser.parse(reply);

		return jReply;
	}	
	
	/***
	 * 특정 시스템 코드 사용자의 매출 목록 조회
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	public JsonObject viewMaterialPurchaseList(
			RPCClient client, 
			JsonObject jConnectionInfo, 
			String systemCode, 
			String userId,
			String fromDate,
			String toDate,
			String vendorId) throws Exception {
		
		JsonObject request = getMessage(
				"RPC Request", 
				"com.brunner.service.sales", 
				"SVC_MaterialPurchase", 
				"TXN_MaterialPurchase_viewMaterialPurchaseList", 
				10000,
				new String[] {
						"txnId",
						"systemCode", 
						"userId", 
						"fromDate",
						"toDate",
						"vendorId"
						},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode, 
						userId, 
						fromDate,
						toDate,
						vendorId
						}				
				);					
		
		String queueName = jConnectionInfo.get(BrunnerClientApi.msgFieldName_queueName).getAsString();
		
		String reply = client.requestSync(
				queueName, 
				request.toString(), 
				10000);
		
		JsonObject jReply = (JsonObject) parser.parse(reply);

		return jReply;
	}
}
