package brunner.client.sales.internalFrames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import brunner.client.BrunnerTableModel;
import brunner.client.FrmBrunnerInternalFrame;
import brunner.client.JBrunnerTable;
import brunner.client.LayoutUtil;
import brunner.client.MessageBoxUtil;
import brunner.client.api.BrunnerClientApi;
import brunner.client.api.CommonCode;
import brunner.client.frames.MainFrame;
import brunner.client.sales.api.Vendor;
import mw.launchers.RPCClient;

public class FrmRegisterVendor extends FrmBrunnerInternalFrame {

	private static final long serialVersionUID = 1L;

	JLabel lblVendorList;
	JBrunnerTable tblVendorList;
	JScrollPane scVendor;
	JButton btnViewVendorList;

	JLabel lblVendorId;
	JComboBox<String> cboVendorId;
	JLabel lblSalesType;
	JComboBox<String> cboSalesType;
	JLabel lblSalesCategory;
	JComboBox<String> cboSalesCategory;
	JLabel lblRegisterName;
	JTextField txtRegisterName;
	JLabel lblRegisterNo;
	JTextField txtRegisterNo;
	JLabel lblAddress;
	JTextArea txtAddress;
	JScrollPane scAddress;
	JLabel lblPhoneNumber;
	JTextField txtPhoneNumber;
	
	JButton btnRegisterVendor;
	JButton btnUnregisterVendor;
	
	public FrmRegisterVendor(
			RPCClient client, 
			JsonObject connectionInfo, 
			JsonObject loginUserInfo,
			String title, 
			int width, 
			int height,
			ImageIcon icon) {
		
		super(client, 
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
		
		lblVendorList = new JLabel("구매처 목록");
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblVendorList);
		
		btnViewVendorList = new JButton("조회");
		btnViewVendorList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnViewVendorList_Click();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_RightButton_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalButton, 
				LayoutUtil.h_NormalPanelControl, 
				btnViewVendorList);		
		
		String groupHeaders[] = {"구매처아이디", "업태", "업종", "상호명", "사업자등록번호", "주소", "전화번호"};
		String groupContents[][] = {};
	
		tblVendorList = JBrunnerTable.getNewInstance(lblVendorList.getText());
		tblVendorList.setModel(new BrunnerTableModel(groupContents, groupHeaders, false));
		tblVendorList.setShowHorizontalLines(true);
		tblVendorList.setGridColor(Color.GRAY);
		tblVendorList.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					int row = tblVendorList.getSelectedRow();
					
					try {
						tblVendorList_rowSelected(row);
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
				}
			}
			
		});
		tblVendorList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int row = tblVendorList.getSelectedRow();
				
				try {
					tblVendorList_rowSelected(row);
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
		});
		
		for(int colIndex = 0; colIndex < tblVendorList.getColumnCount(); colIndex ++)
			tblVendorList.getColumnModel().getColumn(colIndex).setCellRenderer(JBrunnerTable.getDefaultTableCellRenderer());

		((DefaultTableCellRenderer)tblVendorList.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		scVendor = new JScrollPane(tblVendorList);
		y_controlPosition += btnViewVendorList.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_fullWidthControl, 
				LayoutUtil.h_NormalTable, 
				scVendor);		
		
		lblVendorId = new JLabel("아이디");
		y_controlPosition += scVendor.getHeight();
		LayoutUtil.layoutComponent(this, LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblVendorId);
		
		cboVendorId = new JComboBox<String>();
		cboVendorId.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		cboVendorId.setEditable(true);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboVendorId);
		
		lblSalesType = new JLabel("업태");
		y_controlPosition += cboVendorId.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl,  
				lblSalesType);
		
		cboSalesType = new JComboBox<String>();
		cboSalesType.setAlignmentX(SwingConstants.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboSalesType);

		lblSalesCategory = new JLabel("업종");
		y_controlPosition += cboSalesType.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblSalesCategory);
		
		cboSalesCategory = new JComboBox<String>();
		cboSalesCategory.setAlignmentX(SwingConstants.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl,
				LayoutUtil.h_NormalPanelControl,  
				cboSalesCategory);
		
		lblRegisterName = new JLabel("상호명");
		y_controlPosition += cboSalesCategory.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl,  
				lblRegisterName);

		txtRegisterName = new JTextField("");
		txtRegisterName.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtRegisterName);

		lblRegisterNo = new JLabel("주민(사업자)번호");
		y_controlPosition += txtRegisterName.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl,  
				lblRegisterNo);

		txtRegisterNo = new JTextField("");
		txtRegisterNo.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl,  
				txtRegisterNo);

		lblAddress = new JLabel("주소");
		y_controlPosition = lblVendorId.getY();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblAddress);
		
		txtAddress = new JTextArea();
		txtAddress.setLineWrap(true);
		scAddress = new JScrollPane (txtAddress, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		txtAddress.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_TallestPanelControl,
				scAddress);
		
		lblPhoneNumber = new JLabel("전화번호");
		y_controlPosition += scAddress.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl,  
				lblPhoneNumber);
		
		txtPhoneNumber = new JTextField("");
		txtPhoneNumber.setHorizontalAlignment(JTextField.CENTER);
		
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl,  
				txtPhoneNumber);		
		
		btnRegisterVendor = new JButton("등록");
		btnRegisterVendor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnRegisterVendor_click();
				} catch (Exception ex) {
					MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), ex);
				}
			}
			
		});
		
		y_controlPosition = LayoutUtil.y_BottomButton;
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_RightButton_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalButton, 
				LayoutUtil.h_NormalPanelControl, 
				btnRegisterVendor);

		btnUnregisterVendor = new JButton("삭제");
		btnUnregisterVendor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnUnregisterVendor_click();
				} catch (Exception ex) {
					MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), ex);
				}
			}
			
		});
		
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_RightButton_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalButton, 
				LayoutUtil.h_NormalPanelControl, 
				btnUnregisterVendor);
		
		try {
			viewSalesTypeList();
			viewSalesCategoryList();
		} catch (Exception e) {
			MessageBoxUtil.showExceptionMessageDialog(this, e);
		}
	}

	void btnViewVendorList_Click() throws Exception {
		JsonObject jReply = Vendor.getInstance().viewUserVendorList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				this.loginUserInfo.get("USER_ID").getAsString());

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			DefaultTableModel modelCodeGroupList = (DefaultTableModel)tblVendorList.getModel();
			tblVendorList.clearSelection();
			
			while(modelCodeGroupList.getRowCount() > 0)
				modelCodeGroupList.removeRow(0);
			
			for(int rowIndex = 0; rowIndex < jReply.get("vendorList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("vendorList").getAsJsonArray().get(rowIndex);
				
				modelCodeGroupList.addRow(new Object[]{ 
						jRowData.get("vendorId").getAsString(), 
						jRowData.get("salesType").getAsString(),
						jRowData.get("salesCategory").getAsString(),
						jRowData.get("registerName").getAsString(),
						jRowData.get("registerNo").getAsString(),
						jRowData.get("address").getAsString(),
						jRowData.get("phoneNumber").getAsString(),
						});
			}
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		

		clearForm();
	}	
	
	void tblVendorList_rowSelected(int row) throws InterruptedException, TimeoutException, JsonSyntaxException, IOException, URISyntaxException{
		String VendorId = (String) tblVendorList.getModel().getValueAt(row, 0);
		String salesType = (String) tblVendorList.getModel().getValueAt(row, 1);
		String salesCategory = (String) tblVendorList.getModel().getValueAt(row, 2);
		String registerName = (String) tblVendorList.getModel().getValueAt(row, 3);
		String registerNo = (String) tblVendorList.getModel().getValueAt(row, 4);
		String address = (String) tblVendorList.getModel().getValueAt(row, 5);
		String phoneNumber = (String) tblVendorList.getModel().getValueAt(row, 6);

		cboVendorId.setSelectedItem(VendorId);
		cboSalesType.setSelectedItem(salesType);
		cboSalesCategory.setSelectedItem(salesCategory);
		txtRegisterName.setText(registerName);
		txtRegisterNo.setText(registerNo);
		txtAddress.setText(address);
		txtPhoneNumber.setText(phoneNumber);
	}	
	
	void btnRegisterVendor_click() throws Exception {

		if(cboVendorId.getSelectedItem() == null || cboVendorId.getSelectedItem().toString().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "구매처아이디를 입력하세요.");
			cboVendorId.requestFocus();
			return;
		}		

		if(MessageBoxUtil.showConfirmMessageDialog(this, "등록 하시겠습니끼?") == JOptionPane.YES_OPTION) {		
			JsonObject jReply = Vendor.getInstance().registerVendor(
					this.rpcClient, 
					this.connectionInfo, 
					this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
					this.loginUserInfo.get("USER_ID").getAsString(),
					cboVendorId.getSelectedItem().toString(),
					cboSalesType.getSelectedItem().toString(),
					cboSalesCategory.getSelectedItem().toString(),
					txtRegisterName.getText(),
					txtRegisterNo.getText(),
					txtAddress.getText(),
					txtPhoneNumber.getText()
					);
				
			if(jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString().equals(BrunnerClientApi.resultCode_success)) {
				btnViewVendorList_Click();
			}
			else
				MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}
	
	void btnUnregisterVendor_click() throws Exception {

		if(cboVendorId.getSelectedItem() == null || cboVendorId.getSelectedItem().toString().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "구매처아이디를 입력하세요.");
			cboVendorId.requestFocus();
			return;
		}		

		if(MessageBoxUtil.showConfirmMessageDialog(this, "삭제 하시겠습니끼?") == JOptionPane.YES_OPTION) {		
			JsonObject jReply = Vendor.getInstance().unregisterVendor(
					this.rpcClient, 
					this.connectionInfo, 
					this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
					this.loginUserInfo.get("USER_ID").getAsString(),
					cboVendorId.getSelectedItem().toString()
					);
				
			if(jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString().equals(BrunnerClientApi.resultCode_success)) {
				btnViewVendorList_Click();
			}
			else
				MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}
	
	void viewSalesTypeList() throws Exception{
		JsonObject jReply = CommonCode.getInstance().viewCommonCodeItemList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				"SALES_TYPE");

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			cboSalesType.addItem("");
			for(int rowIndex = 0; rowIndex < jReply.get("commonCodeItemList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("commonCodeItemList").getAsJsonArray().get(rowIndex);
				
				cboSalesType.addItem(jRowData.get("commonCodeId").getAsString());
			}
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}	
	
	void viewSalesCategoryList() throws Exception{
		JsonObject jReply = CommonCode.getInstance().viewCommonCodeItemList(
				this.rpcClient, 
				this.connectionInfo, 
				BrunnerClientApi.SYSTEM_CODE_DEFAULT, 
				"SALES_CATEGORY");

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			while(cboSalesCategory.getItemCount() > 0)
				cboSalesCategory.removeItemAt(0);

			cboSalesCategory.addItem("");
			for(int rowIndex = 0; rowIndex < jReply.get("commonCodeItemList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("commonCodeItemList").getAsJsonArray().get(rowIndex);
				
				cboSalesCategory.addItem(jRowData.get("commonCodeId").getAsString());
			}

			cboSalesCategory.setSelectedItem(this.loginUserInfo.get("SALES_CATEGORY").getAsString());
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}		
	
	void clearForm() {
		cboVendorId.setSelectedIndex(-1);
		cboSalesType.setSelectedIndex(-1);
		cboSalesCategory.setSelectedIndex(-1);
		txtRegisterName.setText("");
		txtRegisterNo.setText("");
		txtAddress.setText("");
		txtPhoneNumber.setText("");;
	}
}
