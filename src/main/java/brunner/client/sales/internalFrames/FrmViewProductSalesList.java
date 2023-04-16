package brunner.client.sales.internalFrames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.JDatePicker;

import com.google.gson.JsonObject;

import brunner.client.BrunnerTableModel;
import brunner.client.FrmBrunnerInternalFrame;
import brunner.client.JBrunnerTable;
import brunner.client.LayoutUtil;
import brunner.client.MessageBoxUtil;
import brunner.client.api.BrunnerClientApi;
import brunner.client.sales.api.Customer;
import brunner.client.sales.api.ProductSales;
import mw.launchers.RPCClient;

public class FrmViewProductSalesList extends FrmBrunnerInternalFrame {

	private static final long serialVersionUID = 1L;
	
	JLabel lblSalesList;
	JBrunnerTable tblSalesList;
	JScrollPane scSalesList;
	JButton btnViewSalesList;
	JLabel lblSalesPeriod;
	JDatePicker dtFromDate;
	JDatePicker dtToDate;
	JLabel lblCustomerId;
	JComboBox<String> cboCustomerId;
	
	JLabel lblSalesAmountTotal;
	JTextField txtSalesAmountTotal;
	JLabel lblReceivedAmountTotal;
	JTextField txtReceivedAmountTotal;
	
	HashMap<String, JsonObject> tmpUserCustomerList;

	public FrmViewProductSalesList(RPCClient rpcClient, 
			JsonObject connectionInfo, 
			JsonObject loginUserInfo, 
			String title, 
			int width, 
			int height,
			ImageIcon icon) {
		
		super(rpcClient, 
			  connectionInfo, 
			  loginUserInfo, 
			  title, 
			  width, 
			  height,
			  icon);
	}

	protected void initLayout(int width, int height) {
		super.initLayout(width, height);

		setLayout(null);
		
		int y_controlPosition = LayoutUtil.y_ControlStart;

		lblSalesPeriod = new JLabel("조회 기간");
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblSalesPeriod);
		
		dtFromDate = new JDatePicker();
		dtFromDate.getModel().setDate(
				Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DATE)
				);
		
		dtFromDate.getModel().setSelected(true);
		dtFromDate.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_datePicker,
				LayoutUtil.h_datePicker, 
				dtFromDate);

		dtToDate = new JDatePicker();
		dtToDate.getModel().setDate(
				Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DATE)
				);
		
		dtToDate.getModel().setSelected(true);
		dtToDate.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2 + LayoutUtil.w_datePicker + LayoutUtil.marginConstrols, 
				y_controlPosition, 
				LayoutUtil.w_datePicker, 
				LayoutUtil.h_datePicker, 
				dtToDate);
		
		btnViewSalesList = new JButton("조회");
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_RightButton_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalButton, 
				LayoutUtil.h_NormalPanelControl, 
				btnViewSalesList);
		
		btnViewSalesList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnViewSalesList_Click();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});		

		this.add(btnViewSalesList);		

		lblCustomerId = new JLabel("고객아이디");
		y_controlPosition += btnViewSalesList.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblCustomerId);
		
		cboCustomerId = new JComboBox<String>();
		cboCustomerId.setAlignmentX(SwingConstants.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboCustomerId);		
		
		lblSalesList = new JLabel("매출 목록");
		y_controlPosition += cboCustomerId.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblSalesList);
		
		String groupHeaders[] = {"매출일", "일련번호", "고객아이디", "상호명", "제품명", "단위", "단가", "수량", "매출액", "영수금액", "비고"};
		String groupContents[][] = {};
	
		tblSalesList = JBrunnerTable.getNewInstance(lblSalesList.getText());

	    tblSalesList.addMouseListener(new MouseListener() {
	        @Override
	        public void mouseClicked(MouseEvent e)
	        {   
        	   float salesAmountTotal = 0;
        	   float receivedAmountTotal = 0;

        	   if(tblSalesList.getSelectedRowCount() >= 1) {
				
	        	   for(int i = 0; i < tblSalesList.getSelectedRows().length; i++) {
	        		   salesAmountTotal += Float.parseFloat(tblSalesList.getModel().getValueAt(tblSalesList.getSelectedRows()[i], 8).toString()); // column for salesAmount
	        		   receivedAmountTotal += Float.parseFloat(tblSalesList.getModel().getValueAt(tblSalesList.getSelectedRows()[i], 9).toString()); // column for receivedAmount
	        	   }
				}
	           else {
	        	   for(int i = 0; i < tblSalesList.getRowCount(); i++) {
	        		   salesAmountTotal += Float.parseFloat(tblSalesList.getModel().getValueAt(i, 8).toString()); // column for salesAmount
	        		   receivedAmountTotal += Float.parseFloat(tblSalesList.getModel().getValueAt(i, 9).toString()); // column for receivedAmount
	        	   }
	           }
			
			   txtSalesAmountTotal.setText(String.format("%.1f", salesAmountTotal));
			   txtReceivedAmountTotal.setText(String.format("%.1f", receivedAmountTotal));	      
	        }

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	    });
	    
		tblSalesList.setModel(new BrunnerTableModel(groupContents, groupHeaders, false));
		tblSalesList.setShowHorizontalLines(true);
		tblSalesList.setGridColor(Color.GRAY);
		
		for(int colIndex = 0; colIndex < tblSalesList.getColumnCount(); colIndex ++)
			tblSalesList.getColumnModel().getColumn(colIndex).setCellRenderer(JBrunnerTable.getDefaultTableCellRenderer());

		((DefaultTableCellRenderer)tblSalesList.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		scSalesList = new JScrollPane(tblSalesList);
		y_controlPosition += lblSalesList.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_fullWidthControl, 
				330, 
				scSalesList);
		
		lblSalesAmountTotal = new JLabel("매출액 합계");
		y_controlPosition += scSalesList.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl,  
				lblSalesAmountTotal);
		
		txtSalesAmountTotal = new JTextField("0");
		txtSalesAmountTotal.setEditable(false);
		txtSalesAmountTotal.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_WidePanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtSalesAmountTotal);

		lblReceivedAmountTotal = new JLabel("영수액 합계");
		y_controlPosition += txtSalesAmountTotal.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl,  
				lblReceivedAmountTotal);
		
		txtReceivedAmountTotal = new JTextField("0");
		txtReceivedAmountTotal.setEditable(false);
		txtReceivedAmountTotal.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_WidePanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtReceivedAmountTotal);
	
		try {
			tmpUserCustomerList = new HashMap<String, JsonObject>();
			viewUserCustomerList();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}

	void btnViewSalesList_Click() throws Exception {
		JsonObject jReply = ProductSales.getInstance().viewProductSalesList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				this.loginUserInfo.get("USER_ID").getAsString(),
				new SimpleDateFormat("yyyyMMdd").format(((GregorianCalendar)dtFromDate.getModel().getValue()).getTime()),
				new SimpleDateFormat("yyyyMMdd").format(((GregorianCalendar)dtToDate.getModel().getValue()).getTime()),
				cboCustomerId.getSelectedItem() != null ? cboCustomerId.getSelectedItem().toString(): "");

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			DefaultTableModel modelProductSalesList = (DefaultTableModel)tblSalesList.getModel();
			tblSalesList.clearSelection();
			
			while(modelProductSalesList.getRowCount() > 0)
				modelProductSalesList.removeRow(0);
			
			for(int rowIndex = 0; rowIndex < jReply.get("productSalesList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("productSalesList").getAsJsonArray().get(rowIndex);
			
				modelProductSalesList.addRow(new Object[]{ 
						jRowData.get("salesDate").getAsString(), 
						jRowData.get("salesSerialNo").getAsString(),
						jRowData.get("customerId").getAsString(),
						jRowData.get("registerName").getAsString(),
						jRowData.get("productName").getAsString(),
						jRowData.get("productUnit").getAsString(),
						jRowData.get("unitPrice").getAsString(),
						jRowData.get("salesCount").getAsString(),
						jRowData.get("salesAmount").getAsString(),
						jRowData.get("receivedAmount").getAsString(),
						jRowData.get("salesComment").getAsString()
						});
			}
			
			txtSalesAmountTotal.setText("");
			txtReceivedAmountTotal.setText("");
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}	

	void viewUserCustomerList() throws Exception {
		JsonObject jReply = Customer.getInstance().viewUserCustomerList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				this.loginUserInfo.get("USER_ID").getAsString());

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			cboCustomerId.removeAllItems();
			cboCustomerId.addItem("");
			for(int rowIndex = 0; rowIndex < jReply.get("customerList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("customerList").getAsJsonArray().get(rowIndex);
				
				cboCustomerId.addItem(jRowData.get("customerId").getAsString());
				tmpUserCustomerList.put(jRowData.get("customerId").getAsString(), jRowData);
			}
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}	
}
