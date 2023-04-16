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
import brunner.client.sales.api.Material;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class FrmRegisterMaterial extends FrmBrunnerInternalFrame {

	private static final long serialVersionUID = 1L;

	JLabel lblMaterialList;
	JBrunnerTable tblMaterialList;
	JScrollPane scMaterialList;
	JButton btnViewMaterialList;
	JLabel lblSalesCategory;
	JComboBox<String> cboSalesCategory;
	JLabel lblMaterialName;
	JComboBox<String> cboMaterialName;
	JLabel lblUnitName;
	JComboBox<String> cboUnitName;
	JLabel lblUnitPrice;
	JTextField txtUnitPrice;
	JLabel lblMaterialDesc;
	JTextArea txtMaterialDesc;
	JScrollPane scMaterialDesc;
	
	JButton btnRegisterMaterial;
	JButton btnUnregisterMaterial;
	JButton btnCancel;

	public FrmRegisterMaterial(RPCClient client,
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

		lblMaterialList = new JLabel("자재 목록");
		LayoutUtil.layoutComponent(this, 
								   LayoutUtil.x_ControlStart_1, 
								   y_controlPosition, 
								   LayoutUtil.w_NormalLabel, 
								   LayoutUtil.h_NormalPanelControl,	
								   lblMaterialList);

		btnViewMaterialList = new JButton("조회");
		btnViewMaterialList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnViewMaterialList_Click();
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
								   btnViewMaterialList);

		String groupHeaders[] = { "자재명", "자재 설명", "단위", "단가" };
		String groupContents[][] = {};

		tblMaterialList = JBrunnerTable.getNewInstance(lblMaterialList.getText());
		tblMaterialList.setModel(new BrunnerTableModel(groupContents, groupHeaders, false));
		tblMaterialList.setShowHorizontalLines(true);
		tblMaterialList.setGridColor(Color.GRAY);
		tblMaterialList.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					int row = tblMaterialList.getSelectedRow();

					try {
						tblMaterialList_rowSelected(row);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}

		});
		tblMaterialList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				int row = tblMaterialList.getSelectedRow();

				try {
					tblMaterialList_rowSelected(row);
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

		for (int colIndex = 0; colIndex < tblMaterialList.getColumnCount(); colIndex++)
			tblMaterialList.getColumnModel().getColumn(colIndex).setCellRenderer(JBrunnerTable.getDefaultTableCellRenderer());

		((DefaultTableCellRenderer) tblMaterialList.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

		scMaterialList = new JScrollPane(tblMaterialList);
		y_controlPosition += btnViewMaterialList.getHeight();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_fullWidthControl,
				LayoutUtil.h_NormalTable,
				scMaterialList);

		lblSalesCategory = new JLabel("업종");
		y_controlPosition += scMaterialList.getHeight();
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

		lblMaterialName = new JLabel("자재명");
		y_controlPosition += cboSalesCategory.getHeight();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl,
				lblMaterialName);

		cboMaterialName = new JComboBox<String>();
		cboMaterialName.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		cboMaterialName.setEditable(true);
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_2,
				y_controlPosition,
				LayoutUtil.w_NormalPanelControl,
				LayoutUtil.h_NormalPanelControl,
				cboMaterialName);

		lblUnitName = new JLabel("단위");
		y_controlPosition += cboMaterialName.getHeight();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl,
				lblUnitName);

		cboUnitName = new JComboBox<String>();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_2,
				y_controlPosition,
				LayoutUtil.w_NormalPanelControl,
				LayoutUtil.h_NormalPanelControl,
				cboUnitName);

		lblUnitPrice = new JLabel("단가");
		y_controlPosition += cboUnitName.getHeight();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl,
				lblUnitPrice);

		txtUnitPrice = new JTextField("");
		txtUnitPrice.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_2,
				y_controlPosition,
				LayoutUtil.w_NormalPanelControl,
				LayoutUtil.h_NormalPanelControl,
				txtUnitPrice);

		lblMaterialDesc = new JLabel("자재 설명");
		y_controlPosition = lblSalesCategory.getY();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.right_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl,
				lblMaterialDesc);

		txtMaterialDesc = new JTextArea("");
		txtMaterialDesc.setLineWrap(true);
		scMaterialDesc = new JScrollPane(txtMaterialDesc);
		LayoutUtil.layoutComponent(this,
				LayoutUtil.right_ControlStart_2,
				y_controlPosition,
				LayoutUtil.w_NormalPanelControl,
				LayoutUtil.h_TallestPanelControl,
				txtMaterialDesc);

		btnRegisterMaterial = new JButton("등록");
		btnRegisterMaterial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnRegisterMaterial_click();
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
								   btnRegisterMaterial);

		btnUnregisterMaterial = new JButton("삭제");
		btnUnregisterMaterial.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnUnregisterMaterial_click();
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
								   btnUnregisterMaterial);
		try {
			viewSalesCategoryList();
			viewUnitList();
		} catch (Exception e) {
			MessageBoxUtil.showExceptionMessageDialog(this, e);
		}
	}

	void btnViewMaterialList_Click() throws Exception {
		JsonObject jReply = Material.getInstance().viewMaterialList(this.rpcClient,
																	this.connectionInfo,
																	this.loginUserInfo.get("SYSTEM_CODE").getAsString(),
																	this.loginUserInfo.get("USER_ID").getAsString());

		if (jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			DefaultTableModel modelCodeGroupList = (DefaultTableModel) tblMaterialList.getModel();
			tblMaterialList.clearSelection();

			while (modelCodeGroupList.getRowCount() > 0)
				modelCodeGroupList.removeRow(0);

			for (int rowIndex = 0; rowIndex < jReply.get("materialList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("materialList").getAsJsonArray().get(rowIndex);

				modelCodeGroupList.addRow(new Object[] {
						jRowData.get("materialName").getAsString(),
						jRowData.get("materialDesc").getAsString(),
						jRowData.get("materialUnit").getAsString(),
						jRowData.get("unitPrice").getAsString()
				});
			}
			clearForm();
		} else {
			MessageBoxUtil.showExceptionMessageDialog(this, jReply.get("resultMessage").getAsString());
		}
	}

	void tblMaterialList_rowSelected(int row) throws InterruptedException, TimeoutException, JsonSyntaxException, IOException, URISyntaxException {
		String materialName = (String) tblMaterialList.getModel().getValueAt(row, 0);
		String materialDesc = (String) tblMaterialList.getModel().getValueAt(row, 1);
		String unitName = (String) tblMaterialList.getModel().getValueAt(row, 2);
		int unitPrice = Integer.parseInt(tblMaterialList.getModel().getValueAt(row, 3).toString());

		cboMaterialName.setSelectedItem(materialName);
		cboUnitName.setSelectedItem(unitName);
		txtUnitPrice.setText(String.format("%d", unitPrice));
		txtMaterialDesc.setText(materialDesc);
	}

	void btnRegisterMaterial_click() throws NumberFormatException, Exception {

		if (cboMaterialName.getSelectedItem() == null
				|| cboMaterialName.getSelectedItem().toString().trim().length() == 0) {
			MessageBoxUtil.showErrorMessageDialog(this, "자재 이름를 입력하세요.");
			cboMaterialName.requestFocus();
			return;
		}

		if (cboUnitName.getSelectedItem() == null || cboUnitName.getSelectedItem().toString().trim().length() == 0) {
			MessageBoxUtil.showErrorMessageDialog(this, "자재 단위를 입력하세요.");
			cboUnitName.requestFocus();
			return;
		}

		if (txtUnitPrice.getText().toString().trim().length() == 0) {
			MessageBoxUtil.showErrorMessageDialog(this, "자재 단가를 입력하세요.");
			txtUnitPrice.requestFocus();
			return;
		}

		if (StringUtil.isNumeric(txtUnitPrice.getText()) == false) {
			MessageBoxUtil.showErrorMessageDialog(this, "자재 단가를 숫자로 입력하세요.");
			txtUnitPrice.requestFocus();
			return;
		}

		if (MessageBoxUtil.showConfirmMessageDialog(this, "등록 하시겠습니끼?") == JOptionPane.YES_OPTION) {
			JsonObject jReply = Material.getInstance().registerMaterial(this.rpcClient,
																		this.connectionInfo,
																		this.loginUserInfo.get("USER_ID").getAsString(),
																		this.loginUserInfo.get("SYSTEM_CODE").getAsString(),
																		cboMaterialName.getSelectedItem().toString(),
																		cboUnitName.getSelectedItem().toString(),
																		txtMaterialDesc.getText(),
																		Integer.parseInt(txtUnitPrice.getText()));

			if (jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString().equals(BrunnerClientApi.resultCode_success)) {
				btnViewMaterialList_Click();
			} else
				MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}

	void btnUnregisterMaterial_click() throws Exception {

		if (cboMaterialName.getSelectedItem().toString().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "자재 이름를 입력하세요.");
			cboMaterialName.requestFocus();
			return;
		}

		if (cboUnitName.getSelectedItem().toString().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "자재 단위를 입력하세요.");
			cboUnitName.requestFocus();
			return;
		}

		if (MessageBoxUtil.showConfirmMessageDialog(this, "삭제 하시겠습니끼?") == JOptionPane.YES_OPTION) {
			JsonObject jReply = Material.getInstance().unregisterMaterial(this.rpcClient,
																		  this.connectionInfo,
																		  this.loginUserInfo.get("USER_ID").getAsString(),
																		  this.loginUserInfo.get("SYSTEM_CODE").getAsString(),
																		  cboMaterialName.getSelectedItem().toString(),
																		  cboUnitName.getSelectedItem().toString());

			if (jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString().equals(BrunnerClientApi.resultCode_success)) {
				btnViewMaterialList_Click();
			} else
				MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}

	void viewSalesCategoryList() throws Exception {
		JsonObject jReply = CommonCode.getInstance().viewCommonCodeItemList(this.rpcClient,
																			this.connectionInfo,
																			this.loginUserInfo.get("SYSTEM_CODE").getAsString(),
																			"SALES_CATEGORY");

		if (jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			while (cboSalesCategory.getItemCount() > 0)
				cboSalesCategory.removeItemAt(0);

			cboSalesCategory.addItem("");
			for (int rowIndex = 0; rowIndex < jReply.get("commonCodeItemList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("commonCodeItemList").getAsJsonArray().get(rowIndex);

				cboSalesCategory.addItem(jRowData.get("commonCodeId").getAsString());
			}

			if (this.loginUserInfo.get("SALES_CATEGORY") == null) {
				MessageBoxUtil.showExceptionMessageDialog(this, "현재 사용자의 업종 정보가 없습니다. 업종 정보를 설정하세요.");
			} else {
				cboSalesCategory.setSelectedItem(this.loginUserInfo.get("SALES_CATEGORY").getAsString());
				cboSalesCategory.setEnabled(false);
			}
		} else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}
	}

	void viewUnitList() throws Exception {
		JsonObject jReply = CommonCode.getInstance().viewCommonCodeItemListByKey(this.rpcClient,
																				 this.connectionInfo,
																				 this.loginUserInfo.get("SYSTEM_CODE").getAsString(),
																				 "UNIT_BY_SALES_CATEGORY",
																				 cboSalesCategory.getSelectedItem().toString(),
																				 "*");

		if (jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			while (cboUnitName.getItemCount() > 0)
				cboUnitName.removeItemAt(0);

			cboUnitName.addItem("");
			for (int rowIndex = 0; rowIndex < jReply.get("commonCodeItemList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("commonCodeItemList").getAsJsonArray().get(rowIndex);

				cboUnitName.addItem(jRowData.get("commonCodeId").getAsString());
			}
		} else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}
	}

	void clearForm() {
		cboMaterialName.setSelectedIndex(-1);
		cboUnitName.setSelectedIndex(-1);
		txtUnitPrice.setText("");
		txtMaterialDesc.setText("");
	}
}
