package brunner.client.sales.internalFrames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
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
import brunner.client.frames.MainFrame;
import brunner.client.sales.api.Vendor;
import brunner.client.sales.api.Material;
import brunner.client.sales.api.MaterialPurchase;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class FrmRegisterMaterialPurchase extends FrmBrunnerInternalFrame {

	private static final long serialVersionUID = 1L;
	
	JLabel lblMaterialPurchaseList;
	JBrunnerTable tblMaterialPurchaseList;
	JScrollPane scMaterialPurchaseList;
	JButton btnViewMaterialPurchaseList;
	JLabel lblMaterialPurchasePeriod;
	JDatePicker dtFromDate;
	JDatePicker dtToDate;
	JLabel lblVendorId_View;
	JComboBox<String> cboVendorId_View;
	JLabel lblMaterialPurchaseDate;
	JDatePicker dtMaterialPurchaseDate;
	JLabel lblMaterialPurchaseSerialNo;
	JTextField txtMaterialPurchaseSerialNo;
	JLabel lblVendorId;
	JComboBox<String> cboVendorId;
	JLabel lblRegisterNamey;
	JTextField txtRegisterName;
	JLabel lblMaterialName;
	JComboBox<String> cboMaterialName;
	JLabel lblUnitPrice;
	JTextField txtUnitPrice;
	JLabel lblMaterialSaleCount;
	JTextField txtMaterialPurchaseCount;
	JLabel lblMaterialPurchaseAmount;
	JTextField txtMaterialPurchaseAmount;
	JLabel lblReceivedAmount;
	JTextField txtReceivedAmount;
	JLabel lblMaterialPurchaseComment;
	JTextArea txtMaterialPurchaseComment;
	JScrollPane scMaterialPurchaseComment;

	JButton btnRegisterMaterialPurchase;
	JButton btnUnregisterMaterialPurchase;

	HashMap<String, JsonObject> tmpUserVendorList;
	HashMap<String, JsonObject> tmpMaterialList;
	
	public FrmRegisterMaterialPurchase(
			RPCClient rpcClient, 
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
		
		tmpUserVendorList = new HashMap<String, JsonObject>();
		tmpMaterialList = new HashMap<String, JsonObject>();
		
		setLayout(null);
		
		int y_controlPosition = LayoutUtil.y_ControlStart;

		lblMaterialPurchasePeriod = new JLabel("조회 기간");
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblMaterialPurchasePeriod);
		
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
				LayoutUtil.y_ControlStart, 
				LayoutUtil.w_datePicker, 
				LayoutUtil.h_datePicker, 
				dtToDate);

		lblVendorId_View = new JLabel("구매처아이디");
		y_controlPosition += dtToDate.getHeight(); 
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblVendorId_View);
		
		cboVendorId_View = new JComboBox<String>();
		cboVendorId_View.setAlignmentX(SwingConstants.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboVendorId_View);		
		
		btnViewMaterialPurchaseList = new JButton("조회");
		btnViewMaterialPurchaseList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnViewMaterialPurchaseList_click();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		y_controlPosition = LayoutUtil.y_ControlStart;
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_RightButton_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalButton, 
				LayoutUtil.h_NormalPanelControl, 
				btnViewMaterialPurchaseList);
		
		lblMaterialPurchaseList = new JLabel("구매 목록");
		y_controlPosition = cboVendorId_View.getY() + cboVendorId_View.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblMaterialPurchaseList);	
		
		String groupHeaders[] = {"구매일", "일련번호", "구매처아이디", "상호명", "자재명", "단위", "단가", "수량", "구매금액", "지불금액", "비고"};
		String groupContents[][] = {};
	
		tblMaterialPurchaseList = JBrunnerTable.getNewInstance(lblMaterialPurchaseList.getText());
	    
		tblMaterialPurchaseList.setModel(new BrunnerTableModel(groupContents, groupHeaders, false));
		tblMaterialPurchaseList.setShowHorizontalLines(true);
		tblMaterialPurchaseList.setGridColor(Color.GRAY);
		tblMaterialPurchaseList.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					int row = tblMaterialPurchaseList.getSelectedRow();
					
					try {
						tblMaterialPurchaseList_rowSelected(row);
					} catch (Exception ex) {
						ex.printStackTrace();
					} 
				}
			}			
		});
		tblMaterialPurchaseList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int row = tblMaterialPurchaseList.getSelectedRow();
				
				try {
					tblMaterialPurchaseList_rowSelected(row);
				} catch (Exception ex) {
					ex.printStackTrace();
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
		
		for(int colIndex = 0; colIndex < tblMaterialPurchaseList.getColumnCount(); colIndex ++)
			tblMaterialPurchaseList.getColumnModel().getColumn(colIndex).setCellRenderer(JBrunnerTable.getDefaultTableCellRenderer());

		((DefaultTableCellRenderer)tblMaterialPurchaseList.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		scMaterialPurchaseList = new JScrollPane(tblMaterialPurchaseList);
		y_controlPosition += lblMaterialPurchaseList.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_fullWidthControl,
				LayoutUtil.h_NormalTable, 
				scMaterialPurchaseList);	

		lblMaterialPurchaseDate = new JLabel("구매일");
		y_controlPosition += scMaterialPurchaseList.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1,
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblMaterialPurchaseDate);
		
		dtMaterialPurchaseDate = new JDatePicker();
		dtMaterialPurchaseDate.getModel().setDate(
				Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DATE)
				);
		
		dtMaterialPurchaseDate.getModel().setSelected(true);
		dtMaterialPurchaseDate.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_datePicker, 
				dtMaterialPurchaseDate);

		lblMaterialPurchaseSerialNo = new JLabel("일련번호");
		y_controlPosition += dtMaterialPurchaseDate.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl, 
				lblMaterialPurchaseSerialNo);		

		txtMaterialPurchaseSerialNo = new JTextField("");
		txtMaterialPurchaseSerialNo.setHorizontalAlignment(JTextField.CENTER);
		txtMaterialPurchaseSerialNo.setEditable(false);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtMaterialPurchaseSerialNo);			
		
		lblVendorId = new JLabel("구매처아이디");
		y_controlPosition += txtMaterialPurchaseSerialNo.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblVendorId);
		
		cboVendorId = new JComboBox<String>();
		cboVendorId.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				cboVendorId_selectionChanged();
			}
			
		});

		cboVendorId.setAlignmentX(SwingConstants.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboVendorId);

		lblRegisterNamey = new JLabel("상호명");
		y_controlPosition += cboVendorId.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblRegisterNamey);
		
		txtRegisterName = new JTextField();
		txtRegisterName.setEditable(false);
		txtRegisterName.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtRegisterName);
		
		lblMaterialName = new JLabel("자재명");
		y_controlPosition += txtRegisterName.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblMaterialName);

		cboMaterialName = new JComboBox<String>();
		cboMaterialName.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				cboMaterialName_selectionChanged();
			}
			
		});

		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2,
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboMaterialName);

		lblUnitPrice = new JLabel("단가");
		y_controlPosition += cboMaterialName.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblUnitPrice);

		txtUnitPrice = new JTextField("");
		txtUnitPrice.setEditable(false);
		txtUnitPrice.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl,
				LayoutUtil.h_NormalPanelControl, 
				txtUnitPrice);

		lblMaterialSaleCount = new JLabel("수량");
		y_controlPosition = lblMaterialPurchaseDate.getY();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblMaterialSaleCount);
		
		txtMaterialPurchaseCount = new JTextField();
		txtMaterialPurchaseCount.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				float f = 0;
				
				if(txtMaterialPurchaseCount.getText().trim().length() == 0) {
					txtMaterialPurchaseAmount.setText("");
					txtReceivedAmount.setText("");
					return;
				}
				
				try	{
					f = Float.parseFloat(txtMaterialPurchaseCount.getText());
				}
				catch(Exception ex) {
					MessageBoxUtil.showInformationMessageDialog(MainFrame.getInstance(), "숫자를 입력하세요");
					txtMaterialPurchaseAmount.setText("");
					txtReceivedAmount.setText("");
					txtMaterialPurchaseCount.setText("");
					return;
				}
				
				if(f < 0) {
					MessageBoxUtil.showInformationMessageDialog(MainFrame.getInstance(), "0보다 큰 숫자를 입력하세요");
					txtMaterialPurchaseAmount.setText("");
					txtReceivedAmount.setText("");
					txtMaterialPurchaseCount.setText("");
					return;
				}
				
				if(txtUnitPrice.getText().trim().length() > 0 && txtMaterialPurchaseCount.getText().trim().length() > 0)
				{
					float unitPrice = Float.parseFloat(txtUnitPrice.getText());
					float materialPurchaseCount = Float.parseFloat(txtMaterialPurchaseCount.getText());
					txtMaterialPurchaseAmount.setText(String.format("%.0f", unitPrice * materialPurchaseCount));
					txtReceivedAmount.setText(String.format("%.0f", unitPrice * materialPurchaseCount));
				} else {
					txtMaterialPurchaseAmount.setText("");
					txtReceivedAmount.setText("");
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}
			
		});
		txtMaterialPurchaseCount.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtMaterialPurchaseCount);
		
		lblMaterialPurchaseAmount = new JLabel("구매금액");
		y_controlPosition += txtMaterialPurchaseCount.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblMaterialPurchaseAmount);
		
		txtMaterialPurchaseAmount = new JTextField("");
		txtMaterialPurchaseAmount.setEditable(false);
		txtMaterialPurchaseAmount.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl,
				txtMaterialPurchaseAmount);		

		lblReceivedAmount = new JLabel("지불금액");
		y_controlPosition += txtMaterialPurchaseAmount.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblReceivedAmount);
		
		txtReceivedAmount = new JTextField("");
		txtReceivedAmount.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				float f = 0;
				
				if(txtMaterialPurchaseCount.getText().trim().length() == 0) {
					txtReceivedAmount.setText("");
					return;
				}
				
				try	{
					f = Float.parseFloat(txtReceivedAmount.getText());
				}
				catch(Exception ex) {
					MessageBoxUtil.showInformationMessageDialog(MainFrame.getInstance(), "숫자를 입력하세요");
					txtReceivedAmount.setText("");
					return;
				}
				
				if(f < 0) {
					MessageBoxUtil.showInformationMessageDialog(MainFrame.getInstance(), "0보다 큰 숫자를 입력하세요");
					txtReceivedAmount.setText("");
					return;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		txtReceivedAmount.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtReceivedAmount);		

		lblMaterialPurchaseComment = new JLabel("비고");
		y_controlPosition += txtReceivedAmount.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblMaterialPurchaseComment);
		
		txtMaterialPurchaseComment = new JTextArea("");
		txtMaterialPurchaseComment.setLineWrap(true);
		scMaterialPurchaseComment = new JScrollPane(txtMaterialPurchaseComment); 
		scMaterialPurchaseComment.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		LayoutUtil.layoutComponent(this, LayoutUtil.right_ControlStart_2, y_controlPosition, LayoutUtil.w_NormalPanelControl, LayoutUtil.h_TallestPanelControl, txtMaterialPurchaseComment);

		btnRegisterMaterialPurchase = new JButton("등록");
		btnRegisterMaterialPurchase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnRegisterMaterialPurchase_click();
				} catch (Exception ex) {
					MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), ex);
				}
			}
			
		});
		
		y_controlPosition = LayoutUtil.y_BottomButton;
		LayoutUtil.layoutComponent(this, LayoutUtil.x_RightButton_2, y_controlPosition, LayoutUtil.w_NormalButton,LayoutUtil.h_NormalPanelControl, btnRegisterMaterialPurchase);

		btnUnregisterMaterialPurchase = new JButton("삭제");
		btnUnregisterMaterialPurchase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnUnregisterMaterialPurchase_click();
				} catch (Exception ex) {
					MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), ex);
				}
			}
 		});
		
		LayoutUtil.layoutComponent(this, LayoutUtil.x_RightButton_1, y_controlPosition, LayoutUtil.w_NormalButton, LayoutUtil.h_NormalPanelControl, btnUnregisterMaterialPurchase);
		
		try {
			viewUserVendorList();
			viewMaterialList();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	void btnViewMaterialPurchaseList_click() throws Exception {
		JsonObject jReply = MaterialPurchase.getInstance().viewMaterialPurchaseList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				this.loginUserInfo.get("USER_ID").getAsString(),
				new SimpleDateFormat("yyyyMMdd").format(((GregorianCalendar)dtFromDate.getModel().getValue()).getTime()),
				new SimpleDateFormat("yyyyMMdd").format(((GregorianCalendar)dtToDate.getModel().getValue()).getTime()),
				cboVendorId_View.getSelectedItem() != null ? cboVendorId_View.getSelectedItem().toString(): "");

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			DefaultTableModel modelCodeGroupList = (DefaultTableModel)tblMaterialPurchaseList.getModel();
			tblMaterialPurchaseList.clearSelection();
			
			while(modelCodeGroupList.getRowCount() > 0)
				modelCodeGroupList.removeRow(0);
			
			for(int rowIndex = 0; rowIndex < jReply.get("materialPurchaseList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("materialPurchaseList").getAsJsonArray().get(rowIndex);
				
				modelCodeGroupList.addRow(new Object[]{ 
						jRowData.get("purchaseDate").getAsString(), 
						jRowData.get("purchaseSerialNo").getAsString(),
						jRowData.get("vendorId").getAsString(),
						jRowData.get("registerName").getAsString(),
						jRowData.get("materialName").getAsString(),
						jRowData.get("materialUnit").getAsString(),
						jRowData.get("unitPrice").getAsString(),
						jRowData.get("purchaseCount").getAsString(),
						jRowData.get("purchaseAmount").getAsString(),
						jRowData.get("paidAmount").getAsString(),
						jRowData.get("purchaseComment").getAsString()
						});
			}
			clearForm();
		}
		else {
			MessageBoxUtil.showExceptionMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}	
	
	void tblMaterialPurchaseList_rowSelected(int row) throws Exception{
		if(row < 0)
			return;
		
		clearForm();
		
		String materialPurchaseDate = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 0);
		String materialPurchaseSerialNo = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 1);
		String vendorId = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 2);
		String registerName = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 3);
		String materialName = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 4);
		String materialUnit = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 5);
		String unitPrice = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 6);
		String materialPurchaseCount = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 7);
		String materialPurchaseAmount = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 8);
		String paidAmount = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 9);
		String materialPurchaseCommnt = (String) tblMaterialPurchaseList.getModel().getValueAt(row, 10);

		dtMaterialPurchaseDate.getModel().setDate(Integer.parseInt(materialPurchaseDate.substring(0, 4)), Integer.parseInt(materialPurchaseDate.substring(4, 6)) - 1, Integer.parseInt(materialPurchaseDate.substring(6, 8)));
		txtMaterialPurchaseSerialNo.setText(materialPurchaseSerialNo);
		cboVendorId.setSelectedItem(vendorId);
		txtRegisterName.setText(registerName);
		cboMaterialName.setSelectedItem(String.format("{%s}:{%s}", materialName, materialUnit));
		txtUnitPrice.setText(unitPrice);
		txtMaterialPurchaseCount.setText(materialPurchaseCount);
		txtMaterialPurchaseAmount.setText(materialPurchaseAmount);
		txtReceivedAmount.setText(paidAmount);
		txtMaterialPurchaseComment.setText(materialPurchaseCommnt);
	}	
	
	void btnRegisterMaterialPurchase_click() throws NumberFormatException, Exception {
		JsonObject jReply;
		
		if(cboVendorId.getSelectedItem() == null || cboVendorId.getSelectedItem().toString().trim().length() == 0 ) {
			MessageBoxUtil.showErrorMessageDialog(this, "구매처아이디를 선택하세요.");
			cboVendorId.requestFocus();
			return;
		}
		
		if(cboMaterialName.getSelectedItem() == null) {
			MessageBoxUtil.showErrorMessageDialog(this, "자재를 선택하세요.");
			cboMaterialName.requestFocus();
			return;
		}		

		if(txtMaterialPurchaseCount.getText().trim().length() == 0 || StringUtil.isNumeric(txtMaterialPurchaseCount.getText()) == false) {
			MessageBoxUtil.showErrorMessageDialog(this, "구매 수량을 숫자로 입력하세요.");
			txtMaterialPurchaseCount.requestFocus();
			return;
		}		
		
		if(txtReceivedAmount.getText().trim().length() == 0 || StringUtil.isNumeric(txtReceivedAmount.getText()) == false) {
			MessageBoxUtil.showErrorMessageDialog(this, "지불 금액을 숫자로 입력하세요.");
			txtReceivedAmount.requestFocus();
			return;
		}			
		
		if(MessageBoxUtil.showConfirmMessageDialog(this, "등록 하시겠습니끼?") == JOptionPane.YES_OPTION) {		
			jReply = MaterialPurchase.getInstance().registerMaterialPurchase(
					this.rpcClient, 
					this.connectionInfo, 
					this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
					this.loginUserInfo.get("USER_ID").getAsString(),
					txtMaterialPurchaseSerialNo.getText(),
					new SimpleDateFormat("yyyyMMdd").format(((GregorianCalendar)dtMaterialPurchaseDate.getModel().getValue()).getTime()),
					cboVendorId.getSelectedItem().toString(),
					cboMaterialName.getSelectedItem().toString().split(":")[0].replace("{", "").replace("}", ""),
					cboMaterialName.getSelectedItem().toString().split(":")[1].replace("{", "").replace("}", ""),
					Integer.parseInt(txtUnitPrice.getText()),
					Float.parseFloat(txtMaterialPurchaseCount.getText()),
					Float.parseFloat(txtMaterialPurchaseAmount.getText()),
					Float.parseFloat(txtReceivedAmount.getText()),
					txtMaterialPurchaseComment.getText()
					);
				
			if(jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString().equals(BrunnerClientApi.resultCode_success)) {
				btnViewMaterialPurchaseList_click();
			}
			else
				MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}
	
	void btnUnregisterMaterialPurchase_click() throws Exception {
		if(this.tblMaterialPurchaseList.getSelectedRowCount() == 0 ) {
			JOptionPane.showMessageDialog(this, "구매목록에서 삭제할 항목을 선택하세요.");
			tblMaterialPurchaseList.requestFocus();
			return;
		}
		
		if(MessageBoxUtil.showConfirmMessageDialog(this, "삭제 하시겠습니끼?") == JOptionPane.YES_OPTION) {
			JsonObject jReply = MaterialPurchase.getInstance().unregisterMaterialPurchase(
					this.rpcClient, 
					this.connectionInfo, 
					this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
					this.loginUserInfo.get("USER_ID").getAsString(),
					txtMaterialPurchaseSerialNo.getText()
					);
				
			if(jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString().equals(BrunnerClientApi.resultCode_success)) {
				btnViewMaterialPurchaseList_click();
			}
			else
				MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}

	void clearForm() {
		dtMaterialPurchaseDate.getModel().setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));		
		txtMaterialPurchaseSerialNo.setText("");
		cboVendorId.setSelectedIndex(-1);
		txtRegisterName.setText("");
		cboMaterialName.setSelectedIndex(-1);
		txtUnitPrice.setText("");
		txtMaterialPurchaseCount.setText("");
		txtMaterialPurchaseAmount.setText("");
		txtReceivedAmount.setText("");
	}	

	void viewUserVendorList() throws Exception {
		JsonObject jReply = Vendor.getInstance().viewUserVendorList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				this.loginUserInfo.get("USER_ID").getAsString());

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			cboVendorId.removeAllItems();
			cboVendorId.addItem("");
			
			cboVendorId_View.removeAllItems();
			cboVendorId_View.addItem("");
			
			for(int rowIndex = 0; rowIndex < jReply.get("vendorList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("vendorList").getAsJsonArray().get(rowIndex);
				
				cboVendorId.addItem(jRowData.get("vendorId").getAsString());
				cboVendorId_View.addItem(jRowData.get("vendorId").getAsString());
				tmpUserVendorList.put(jRowData.get("vendorId").getAsString(), jRowData);
			}
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}
	
	void cboVendorId_selectionChanged() {
		if(cboVendorId.getSelectedItem() == null || StringUtil.isNullOrEmpty(cboVendorId.getSelectedItem().toString()) == true)
			txtRegisterName.setText("");
		else
			txtRegisterName.setText(tmpUserVendorList.get(cboVendorId.getSelectedItem().toString()).get("registerName").getAsString());
	}
	
	void viewMaterialList() throws Exception {
		JsonObject jReply = Material.getInstance().viewMaterialList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				this.loginUserInfo.get("USER_ID").getAsString());

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			cboMaterialName.removeAllItems();
			cboMaterialName.addItem("");
			for(int rowIndex = 0; rowIndex < jReply.get("materialList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("materialList").getAsJsonArray().get(rowIndex);
				
				cboMaterialName.addItem(String.format("{%s}:{%s}", jRowData.get("materialName").getAsString(), jRowData.get("materialUnit").getAsString()));
				tmpMaterialList.put(String.format("{%s}:{%s}", jRowData.get("materialName").getAsString(), jRowData.get("materialUnit").getAsString()), jRowData);
			}
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}	
	
	void cboMaterialName_selectionChanged() {
		if(cboMaterialName.getSelectedItem() == null || cboMaterialName.getSelectedItem().equals(""))
			txtUnitPrice.setText("");
		else
			txtUnitPrice.setText(tmpMaterialList.get(cboMaterialName.getSelectedItem().toString()).get("unitPrice").getAsString());
			
		txtMaterialPurchaseCount.setText("");
		txtMaterialPurchaseAmount.setText("");
		txtReceivedAmount.setText("");
		txtMaterialPurchaseComment.setText("");
	}
}
