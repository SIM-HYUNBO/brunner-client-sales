package brunner.client.sales.api;

import com.google.gson.JsonObject;

import brunner.client.api.BrunnerClientApi;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class ProductSales extends BrunnerClientApi {

	static ProductSales instance;
	public static ProductSales getInstance() {
		return instance == null ? new ProductSales(1000): instance;
	}	
	
	public ProductSales(int requestTimeoutMs) {
		super(requestTimeoutMs);
	}

	/***
	 * 특정 시스템 코드 사용자의 매출 등록
	 * 
	 * @param client
	 * @param jConnectionInfo
	 * @param systemCode
	 * @param userId
	 * @param salesSerialNo
	 * @param salesDate
	 * @param customerId
	 * @param productName
	 * @param productUnit
	 * @param unitPrice
	 * @param salesCount
	 * @param salesAmount
	 * @param receivedAmount
	 * @param salesComment
	 * @return
	 * @throws Exception
	 */
	public JsonObject registerProductSales(
			RPCClient client, 
			JsonObject jConnectionInfo, 
			String systemCode,
			String userId,
			String salesSerialNo,
			String salesDate,
			String customerId,
			String productName,
			String productUnit,
			int unitPrice,
			float salesCount,
			float salesAmount,
			float receivedAmount,
			String salesComment
			) throws Exception {
		
		JsonObject request = getMessage(
				"RPC Request", 
				"com.brunner.service.sales", 
				"SVC_ProductSales", 
				"TXN_ProductSales_registerProductSales", 
				10000,
				new String[] {
						"txnId",
						"systemCode", 
						"userId", 
						"salesSerialNo",
						"salesDate", 
						"customerId",
						"productName",
						"productUnit",
						"unitPrice",
						"salesCount",
						"salesAmount",
						"receivedAmount",
						"salesComment"
						},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode, 
						userId, 
						salesSerialNo,
						salesDate, 
						customerId,
						productName,
						productUnit,
						unitPrice,
						salesCount,
						salesAmount,
						receivedAmount,
						salesComment
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
	 * @param salesSerialNo
	 * @return
	 * @throws Exception
	 */
	public JsonObject unregisterProductSales(
			RPCClient client, 
			JsonObject jConnectionInfo, 
			String systemCode,
			String userId,
			String salesSerialNo
			) throws Exception {
		
		JsonObject request = getMessage(
				"RPC Request", 
				"com.brunner.service.sales", 
				"SVC_ProductSales", 
				"TXN_ProductSales_unregisterProductSales", 
				10000,
				new String[] {
						"txnId",
						"systemCode", 
						"userId", 
						"salesSerialNo"
						},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode, 
						userId, 
						salesSerialNo
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
	public JsonObject viewProductSalesList(
			RPCClient client, 
			JsonObject jConnectionInfo, 
			String systemCode, 
			String userId,
			String fromDate,
			String toDate,
			String customerId) throws Exception {
		
		JsonObject request = getMessage(
				"RPC Request", 
				"com.brunner.service.sales", 
				"SVC_ProductSales", 
				"TXN_ProductSales_viewProductSalesList", 
				10000,
				new String[] {
						"txnId",
						"systemCode", 
						"userId", 
						"fromDate",
						"toDate",
						"customerId"
						},
				new Object[] {
						StringUtil.getTxnId(),
						systemCode, 
						userId, 
						fromDate,
						toDate,
						customerId
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
