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
import brunner.client.sales.api.Product;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class FrmRegisterProduct extends FrmBrunnerInternalFrame {

	private static final long serialVersionUID = 1L;

	JLabel lblProductList;
	JBrunnerTable tblProductList;
	JScrollPane scProductList;
	JButton btnViewProductList;
	JLabel lblSalesCategory;
	JComboBox<String> cboSalesCategory;
	JLabel lblProductName;
	JComboBox<String> cboProductName;
	JLabel lblUnitName;
	JComboBox<String> cboUnitName;
	JLabel lblUnitPrice;
	JTextField txtUnitPrice;
	JLabel lblProductDesc;
	JTextArea txtProductDesc;
	JScrollPane scProductDesc;
	
	JButton btnRegisterProduct;
	JButton btnUnregisterProduct;
	JButton btnCancel;

	public FrmRegisterProduct(RPCClient client,
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

		lblProductList = new JLabel("제품 목록");
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl,
				lblProductList);

		btnViewProductList = new JButton("조회");
		btnViewProductList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnViewProductList_Click();
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
				btnViewProductList);

		String groupHeaders[] = { "제품(품목)명", "제품(품목) 설명", "단위", "단가" };
		String groupContents[][] = {};

		tblProductList = JBrunnerTable.getNewInstance(lblProductList.getText());
		tblProductList.setModel(new BrunnerTableModel(groupContents, groupHeaders, false));
		tblProductList.setShowHorizontalLines(true);
		tblProductList.setGridColor(Color.GRAY);
		tblProductList.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					int row = tblProductList.getSelectedRow();

					try {
						tblProductList_rowSelected(row);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}

		});
		tblProductList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				int row = tblProductList.getSelectedRow();

				try {
					tblProductList_rowSelected(row);
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

		for (int colIndex = 0; colIndex < tblProductList.getColumnCount(); colIndex++)
			tblProductList.getColumnModel().getColumn(colIndex)
					.setCellRenderer(JBrunnerTable.getDefaultTableCellRenderer());

		((DefaultTableCellRenderer) tblProductList.getTableHeader().getDefaultRenderer())
				.setHorizontalAlignment(JLabel.CENTER);

		scProductList = new JScrollPane(tblProductList);
		y_controlPosition += btnViewProductList.getHeight();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_fullWidthControl,
				LayoutUtil.h_NormalTable,
				scProductList);

		lblSalesCategory = new JLabel("업종");
		y_controlPosition += scProductList.getHeight();
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

		lblProductName = new JLabel("제품(품목)명");
		y_controlPosition += cboSalesCategory.getHeight();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl,
				lblProductName);

		cboProductName = new JComboBox<String>();
		cboProductName.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		cboProductName.setEditable(true);
		LayoutUtil.layoutComponent(this,
				LayoutUtil.x_ControlStart_2,
				y_controlPosition,
				LayoutUtil.w_NormalPanelControl,
				LayoutUtil.h_NormalPanelControl,
				cboProductName);

		lblUnitName = new JLabel("단위");
		y_controlPosition += cboProductName.getHeight();
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

		lblProductDesc = new JLabel("제품(품목) 설명");
		y_controlPosition = lblSalesCategory.getY();
		LayoutUtil.layoutComponent(this,
				LayoutUtil.right_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl,
				lblProductDesc);

		txtProductDesc = new JTextArea("");
		txtProductDesc.setLineWrap(true);
		scProductDesc = new JScrollPane(txtProductDesc);
		LayoutUtil.layoutComponent(this,
				LayoutUtil.right_ControlStart_2,
				y_controlPosition,
				LayoutUtil.w_NormalPanelControl,
				LayoutUtil.h_TallestPanelControl,
				txtProductDesc);

		btnRegisterProduct = new JButton("등록");
		btnRegisterProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnRegisterProduct_click();
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
				btnRegisterProduct);

		btnUnregisterProduct = new JButton("삭제");
		btnUnregisterProduct.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnUnregisterProduct_click();
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
				btnUnregisterProduct);

		try {
			viewSalesCategoryList();
			viewUnitList();
		} catch (Exception e) {
			MessageBoxUtil.showExceptionMessageDialog(this, e);
		}
	}

	void btnViewProductList_Click() throws Exception {
		JsonObject jReply = Product.getInstance().viewProductList(
				this.rpcClient,
				this.connectionInfo,
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(),
				this.loginUserInfo.get("USER_ID").getAsString());

		if (jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			DefaultTableModel modelCodeGroupList = (DefaultTableModel) tblProductList.getModel();
			tblProductList.clearSelection();

			while (modelCodeGroupList.getRowCount() > 0)
				modelCodeGroupList.removeRow(0);

			for (int rowIndex = 0; rowIndex < jReply.get("productList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("productList").getAsJsonArray().get(rowIndex);

				modelCodeGroupList.addRow(new Object[] {
						jRowData.get("productName").getAsString(),
						jRowData.get("productDesc").getAsString(),
						jRowData.get("productUnit").getAsString(),
						jRowData.get("unitPrice").getAsString()
				});
			}
			clearForm();
		} else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}
	}

	void tblProductList_rowSelected(int row)
			throws InterruptedException, TimeoutException, JsonSyntaxException, IOException, URISyntaxException {
		String productName = (String) tblProductList.getModel().getValueAt(row, 0);
		String productDesc = (String) tblProductList.getModel().getValueAt(row, 1);
		String unitName = (String) tblProductList.getModel().getValueAt(row, 2);
		int unitPrice = Integer.parseInt(tblProductList.getModel().getValueAt(row, 3).toString());

		cboProductName.setSelectedItem(productName);
		cboUnitName.setSelectedItem(unitName);
		txtUnitPrice.setText(String.format("%d", unitPrice));
		txtProductDesc.setText(productDesc);
	}

	void btnRegisterProduct_click() throws NumberFormatException, Exception {

		if (cboProductName.getSelectedItem() == null
				|| cboProductName.getSelectedItem().toString().trim().length() == 0) {
			MessageBoxUtil.showErrorMessageDialog(this, "제품(품목)이름를 입력하세요.");
			cboProductName.requestFocus();
			return;
		}

		if (cboUnitName.getSelectedItem() == null || cboUnitName.getSelectedItem().toString().trim().length() == 0) {
			MessageBoxUtil.showErrorMessageDialog(this, "제품 단위를 입력하세요.");
			cboUnitName.requestFocus();
			return;
		}

		if (txtUnitPrice.getText().toString().trim().length() == 0) {
			MessageBoxUtil.showErrorMessageDialog(this, "제품 단가를 입력하세요.");
			txtUnitPrice.requestFocus();
			return;
		}

		if (StringUtil.isNumeric(txtUnitPrice.getText()) == false) {
			MessageBoxUtil.showErrorMessageDialog(this, "제품 단가를 숫자로 입력하세요.");
			txtUnitPrice.requestFocus();
			return;
		}

		if (MessageBoxUtil.showConfirmMessageDialog(this, "등록 하시겠습니끼?") == JOptionPane.YES_OPTION) {
			JsonObject jReply = Product.getInstance().registerProduct(
					this.rpcClient,
					this.connectionInfo,
					this.loginUserInfo.get("USER_ID").getAsString(),
					this.loginUserInfo.get("SYSTEM_CODE").getAsString(),
					cboProductName.getSelectedItem().toString(),
					cboUnitName.getSelectedItem().toString(),
					txtProductDesc.getText(),
					Integer.parseInt(txtUnitPrice.getText()));

			if (jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString()
					.equals(BrunnerClientApi.resultCode_success)) {
				btnViewProductList_Click();
			} else
				MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}

	void btnUnregisterProduct_click() throws Exception {

		if (cboProductName.getSelectedItem().toString().trim().length() == 0) {
			MessageBoxUtil.showErrorMessageDialog(this, "제품(품목)이름를 입력하세요.");
			cboProductName.requestFocus();
			return;
		}

		if (cboUnitName.getSelectedItem().toString().trim().length() == 0) {
			MessageBoxUtil.showErrorMessageDialog(this, "제품 단위를 입력하세요.");
			cboUnitName.requestFocus();
			return;
		}

		if (MessageBoxUtil.showConfirmMessageDialog(this, "삭제 하시겠습니끼?") == JOptionPane.YES_OPTION) {
			JsonObject jReply = Product.getInstance().unregisterProduct(
					this.rpcClient,
					this.connectionInfo,
					this.loginUserInfo.get("USER_ID").getAsString(),
					this.loginUserInfo.get("SYSTEM_CODE").getAsString(),
					cboProductName.getSelectedItem().toString(),
					cboUnitName.getSelectedItem().toString());

			if (jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString()
					.equals(BrunnerClientApi.resultCode_success)) {
				btnViewProductList_Click();
			} else
				MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(),
						jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}

	void viewSalesCategoryList() throws Exception {
		JsonObject jReply = CommonCode.getInstance().viewCommonCodeItemList(
				this.rpcClient,
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
			MessageBoxUtil.showExceptionMessageDialog(this, jReply.get("resultMessage").getAsString());
		}
	}

	void viewUnitList() throws Exception {
		JsonObject jReply = CommonCode.getInstance().viewCommonCodeItemListByKey(
				this.rpcClient,
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
			MessageBoxUtil.showExceptionMessageDialog(this, jReply.get("resultMessage").getAsString());
		}
	}

	void clearForm() {
		cboProductName.setSelectedIndex(-1);
		cboUnitName.setSelectedIndex(-1);
		txtUnitPrice.setText("");
		txtProductDesc.setText("");
	}
}
