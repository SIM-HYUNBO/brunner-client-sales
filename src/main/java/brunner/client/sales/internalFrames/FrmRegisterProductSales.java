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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.jdatepicker.JDatePicker;

import com.google.gson.JsonObject;

import brunner.client.BrunnerTableModel;
import brunner.client.FrmBrunnerInternalFrame;
import brunner.client.JBrunnerTable;
import brunner.client.LayoutUtil;
import brunner.client.MessageBoxUtil;
import brunner.client.api.BrunnerClientApi;
import brunner.client.frames.MainFrame;
import brunner.client.sales.api.Customer;
import brunner.client.sales.api.Product;
import brunner.client.sales.api.ProductSales;
import mw.launchers.RPCClient;
import mw.utility.StringUtil;

public class FrmRegisterProductSales extends FrmBrunnerInternalFrame {

	private static final long serialVersionUID = 1L;
	
	JLabel lblProductSalesList;
	JBrunnerTable tblProductSalesList;
	JScrollPane scProductSalesList;
	JButton btnViewProductSalesList;
	JLabel lblProductSalesPeriod;
	JDatePicker dtFromDate;
	JDatePicker dtToDate;
	JLabel lblCustomerId_View;
	JComboBox<String> cboCustomerId_View;
	JLabel lblProductSalesDate;
	JDatePicker dtProductSalesDate;
	JLabel lblProductSalesSerialNo;
	JTextField txtProductSalesSerialNo;
	JLabel lblCustomerId;
	JComboBox<String> cboCustomerId;
	JLabel lblRegisterNamey;
	JTextField txtRegisterName;
	JLabel lblProductName;
	JComboBox<String> cboProductName;
	JLabel lblUnitPrice;
	JTextField txtUnitPrice;
	JLabel lblProductSaleCount;
	JTextField txtProductSalesCount;
	JLabel lblProductSalesAmount;
	JTextField txtProductSalesAmount;
	JLabel lblReceivedAmount;
	JTextField txtReceivedAmount;
	JLabel lblProductSalesComment;
	JTextArea txtProductSalesComment;
	JScrollPane scProductSalesComment;

	JButton btnViewReceit;
	JButton btnRegisterProductSales;
	JButton btnUnregisterProductSales;

	HashMap<String, JsonObject> tmpUserCustomerList;
	HashMap<String, JsonObject> tmpProductList;
	
	public FrmRegisterProductSales(
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
		
		tmpUserCustomerList = new HashMap<String, JsonObject>();
		tmpProductList = new HashMap<String, JsonObject>();
		
		setLayout(null);
		
		int y_controlPosition = LayoutUtil.y_ControlStart;

		lblProductSalesPeriod = new JLabel("조회 기간");
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblProductSalesPeriod);
		
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

		lblCustomerId_View = new JLabel("고객아이디");
		y_controlPosition += dtToDate.getHeight(); 
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblCustomerId_View);
		
		cboCustomerId_View = new JComboBox<String>();
		cboCustomerId_View.setAlignmentX(SwingConstants.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboCustomerId_View);		
		
		btnViewProductSalesList = new JButton("조회");
		btnViewProductSalesList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnViewProductSalesList_click();
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
				btnViewProductSalesList);
		
		lblProductSalesList = new JLabel("매출 목록");
		y_controlPosition = cboCustomerId_View.getY() + cboCustomerId_View.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblProductSalesList);	
		
		String groupHeaders[] = {"매출일", "일련번호", "고객아이디", "상호명", "제품명", "단위", "단가", "수량", "매출액", "영수금액", "비고"};
		String groupContents[][] = {};
	
		tblProductSalesList = JBrunnerTable.getNewInstance(lblProductSalesList.getText());
	    
		tblProductSalesList.setModel(new BrunnerTableModel(groupContents, groupHeaders, false));
		tblProductSalesList.setShowHorizontalLines(true);
		tblProductSalesList.setGridColor(Color.GRAY);
		tblProductSalesList.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					int row = tblProductSalesList.getSelectedRow();
					
					try {
						tblProductSalesList_rowSelected(row);
					} catch (Exception ex) {
						ex.printStackTrace();
					} 
				}
			}			
		});
		tblProductSalesList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int row = tblProductSalesList.getSelectedRow();
				
				try {
					tblProductSalesList_rowSelected(row);
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
		
		for(int colIndex = 0; colIndex < tblProductSalesList.getColumnCount(); colIndex ++)
			tblProductSalesList.getColumnModel().getColumn(colIndex).setCellRenderer(JBrunnerTable.getDefaultTableCellRenderer());

		((DefaultTableCellRenderer)tblProductSalesList.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		scProductSalesList = new JScrollPane(tblProductSalesList);
		y_controlPosition += lblProductSalesList.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_fullWidthControl,
				LayoutUtil.h_NormalTable, 
				scProductSalesList);	

		lblProductSalesDate = new JLabel("매출일");
		y_controlPosition += scProductSalesList.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1,
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblProductSalesDate);
		
		dtProductSalesDate = new JDatePicker();
		dtProductSalesDate.getModel().setDate(
				Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DATE)
				);
		
		dtProductSalesDate.getModel().setSelected(true);
		dtProductSalesDate.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_datePicker, 
				dtProductSalesDate);

		lblProductSalesSerialNo = new JLabel("일련번호");
		y_controlPosition += dtProductSalesDate.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel,
				LayoutUtil.h_NormalPanelControl, 
				lblProductSalesSerialNo);		

		txtProductSalesSerialNo = new JTextField("");
		txtProductSalesSerialNo.setHorizontalAlignment(JTextField.CENTER);
		txtProductSalesSerialNo.setEditable(false);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtProductSalesSerialNo);			
		
		lblCustomerId = new JLabel("고객아이디");
		y_controlPosition += txtProductSalesSerialNo.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1,
				y_controlPosition,
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblCustomerId);
		
		cboCustomerId = new JComboBox<String>();
		cboCustomerId.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				cboCustomerId_selectionChanged();
			}
			
		});

		cboCustomerId.setAlignmentX(SwingConstants.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboCustomerId);

		lblRegisterNamey = new JLabel("상호명");
		y_controlPosition += cboCustomerId.getHeight();
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
		
		lblProductName = new JLabel("제품명");
		y_controlPosition += txtRegisterName.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblProductName);

		cboProductName = new JComboBox<String>();
		cboProductName.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				cboProductName_selectionChanged();
			}
			
		});

		LayoutUtil.layoutComponent(this, 
				LayoutUtil.x_ControlStart_2,
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				cboProductName);

		lblUnitPrice = new JLabel("단가");
		y_controlPosition += cboProductName.getHeight();
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

		lblProductSaleCount = new JLabel("수량");
		y_controlPosition = lblProductSalesDate.getY();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblProductSaleCount);
		
		txtProductSalesCount = new JTextField();
		txtProductSalesCount.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				float f = 0;
				
				if(txtProductSalesCount.getText().trim().length() == 0) {
					txtProductSalesAmount.setText("");
					txtReceivedAmount.setText("");
					return;
				}
				
				try	{
					f = Float.parseFloat(txtProductSalesCount.getText());
				}
				catch(Exception ex) {
					MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), "숫자를 입력하세요");
					txtProductSalesAmount.setText("");
					txtReceivedAmount.setText("");
					txtProductSalesCount.setText("");
					return;
				}
				
				if(f < 0) {
					MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), "0보다 큰 숫자를 입력하세요");
					txtProductSalesAmount.setText("");
					txtReceivedAmount.setText("");
					txtProductSalesCount.setText("");
					return;
				}
				
				if(txtUnitPrice.getText().trim().length() > 0 && txtProductSalesCount.getText().trim().length() > 0)
				{
					float unitPrice = Float.parseFloat(txtUnitPrice.getText());
					float productSalesCount = Float.parseFloat(txtProductSalesCount.getText());
					txtProductSalesAmount.setText(String.format("%.0f", unitPrice * productSalesCount));
					txtReceivedAmount.setText(String.format("%.0f", unitPrice * productSalesCount));
				} else {
					txtProductSalesAmount.setText("");
					txtReceivedAmount.setText("");
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}
			
		});
		txtProductSalesCount.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl, 
				txtProductSalesCount);
		
		lblProductSalesAmount = new JLabel("매출금액");
		y_controlPosition += txtProductSalesCount.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblProductSalesAmount);
		
		txtProductSalesAmount = new JTextField("");
		txtProductSalesAmount.setEditable(false);
		txtProductSalesAmount.setHorizontalAlignment(JTextField.CENTER);
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_2, 
				y_controlPosition, 
				LayoutUtil.w_NormalPanelControl, 
				LayoutUtil.h_NormalPanelControl,
				txtProductSalesAmount);		

		lblReceivedAmount = new JLabel("영수금액");
		y_controlPosition += txtProductSalesAmount.getHeight();
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
				
				if(txtProductSalesCount.getText().trim().length() == 0) {
					txtReceivedAmount.setText("");
					return;
				}
				
				try	{
					f = Float.parseFloat(txtReceivedAmount.getText());
				}
				catch(Exception ex) {
					MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), "숫자를 입력하세요");
					txtReceivedAmount.setText("");
					return;
				}
				
				if(f < 0) {
					MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), "0보다 큰 숫자를 입력하세요");
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

		lblProductSalesComment = new JLabel("비고");
		y_controlPosition += txtReceivedAmount.getHeight();
		LayoutUtil.layoutComponent(this, 
				LayoutUtil.right_ControlStart_1, 
				y_controlPosition, 
				LayoutUtil.w_NormalLabel, 
				LayoutUtil.h_NormalPanelControl, 
				lblProductSalesComment);
		
		txtProductSalesComment = new JTextArea("");
		txtProductSalesComment.setLineWrap(true);
		scProductSalesComment = new JScrollPane(txtProductSalesComment); 
		scProductSalesComment.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		LayoutUtil.layoutComponent(this, LayoutUtil.right_ControlStart_2, y_controlPosition, LayoutUtil.w_NormalPanelControl, LayoutUtil.h_TallestPanelControl, txtProductSalesComment);		

		btnViewReceit = new JButton("영수증 보기");
		btnViewReceit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnViewReceit_click();
				} catch (Exception ex) {
					MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), ex);
				}
			}
		});
		
		y_controlPosition = LayoutUtil.y_BottomButton;
		LayoutUtil.layoutComponent(this, LayoutUtil.x_RightButton_3, y_controlPosition, LayoutUtil.w_NormalButton,LayoutUtil.h_NormalPanelControl, btnViewReceit);
		

		btnRegisterProductSales = new JButton("등록");
		btnRegisterProductSales.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnRegisterProductSales_click();
				} catch (Exception ex) {
					MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), ex);
				}
			}
			
		});
		
		y_controlPosition = LayoutUtil.y_BottomButton;
		LayoutUtil.layoutComponent(this, LayoutUtil.x_RightButton_2, y_controlPosition, LayoutUtil.w_NormalButton,LayoutUtil.h_NormalPanelControl, btnRegisterProductSales);

		btnUnregisterProductSales = new JButton("삭제");
		btnUnregisterProductSales.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnUnregisterProductSales_click();
				} catch (Exception ex) {
					MessageBoxUtil.showExceptionMessageDialog(MainFrame.getInstance(), ex);
				}
			}
 		});
		
		LayoutUtil.layoutComponent(this, LayoutUtil.x_RightButton_1, y_controlPosition, LayoutUtil.w_NormalButton, LayoutUtil.h_NormalPanelControl, btnUnregisterProductSales);
		
		try {
			viewUserCustomerList();
			viewProductList();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	void btnViewProductSalesList_click() throws Exception {
		JsonObject jReply = ProductSales.getInstance().viewProductSalesList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				this.loginUserInfo.get("USER_ID").getAsString(),
				new SimpleDateFormat("yyyyMMdd").format(((GregorianCalendar)dtFromDate.getModel().getValue()).getTime()),
				new SimpleDateFormat("yyyyMMdd").format(((GregorianCalendar)dtToDate.getModel().getValue()).getTime()),
				cboCustomerId_View.getSelectedItem() != null ? cboCustomerId_View.getSelectedItem().toString(): "");

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			DefaultTableModel modelCodeGroupList = (DefaultTableModel)tblProductSalesList.getModel();
			tblProductSalesList.clearSelection();
			
			while(modelCodeGroupList.getRowCount() > 0)
				modelCodeGroupList.removeRow(0);
			
			for(int rowIndex = 0; rowIndex < jReply.get("productSalesList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("productSalesList").getAsJsonArray().get(rowIndex);
				
				modelCodeGroupList.addRow(new Object[]{ 
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
			clearForm();
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}	
	
	void tblProductSalesList_rowSelected(int row) throws Exception{
		if(row < 0)
			return;
		
		clearForm();
		
		String productSalesDate = (String) tblProductSalesList.getModel().getValueAt(row, 0);
		String productSalesSerialNo = (String) tblProductSalesList.getModel().getValueAt(row, 1);
		String customerId = (String) tblProductSalesList.getModel().getValueAt(row, 2);
		String registerName = (String) tblProductSalesList.getModel().getValueAt(row, 3);
		String productName = (String) tblProductSalesList.getModel().getValueAt(row, 4);
		String productUnit = (String) tblProductSalesList.getModel().getValueAt(row, 5);
		String unitPrice = (String) tblProductSalesList.getModel().getValueAt(row, 6);
		String productSalesCount = (String) tblProductSalesList.getModel().getValueAt(row, 7);
		String productSalesAmount = (String) tblProductSalesList.getModel().getValueAt(row, 8);
		String receivedAmount = (String) tblProductSalesList.getModel().getValueAt(row, 9);
		String productSalesCommnt = (String) tblProductSalesList.getModel().getValueAt(row, 10);

		dtProductSalesDate.getModel().setDate(Integer.parseInt(productSalesDate.substring(0, 4)), Integer.parseInt(productSalesDate.substring(4, 6)) - 1, Integer.parseInt(productSalesDate.substring(6, 8)));
		txtProductSalesSerialNo.setText(productSalesSerialNo);
		cboCustomerId.setSelectedItem(customerId);
		txtRegisterName.setText(registerName);
		cboProductName.setSelectedItem(String.format("{%s}:{%s}", productName, productUnit));
		txtUnitPrice.setText(unitPrice);
		txtProductSalesCount.setText(productSalesCount);
		txtProductSalesAmount.setText(productSalesAmount);
		txtReceivedAmount.setText(receivedAmount);
		txtProductSalesComment.setText(productSalesCommnt);
	}	
	
	void btnRegisterProductSales_click() throws NumberFormatException, Exception {
		JsonObject jReply;
		
		if(cboCustomerId.getSelectedItem() == null || cboCustomerId.getSelectedItem().toString().trim().length() == 0 ) {
			MessageBoxUtil.showErrorMessageDialog(this, "고객(판매처)아이디를 선택하세요.");
			cboCustomerId.requestFocus();
			return;
		}
		
		if(cboProductName.getSelectedItem() == null) {
			MessageBoxUtil.showErrorMessageDialog(this, "제품을 선택하세요.");
			cboProductName.requestFocus();
			return;
		}		

		if(txtProductSalesCount.getText().trim().length() == 0 || StringUtil.isNumeric(txtProductSalesCount.getText()) == false) {
			MessageBoxUtil.showErrorMessageDialog(this, "판매 수량을 숫자로 입력하세요.");
			txtProductSalesCount.requestFocus();
			return;
		}		
		
		if(txtReceivedAmount.getText().trim().length() == 0 || StringUtil.isNumeric(txtReceivedAmount.getText()) == false) {
			MessageBoxUtil.showErrorMessageDialog(this, "영수 금액을 숫자로 입력하세요.");
			txtReceivedAmount.requestFocus();
			return;
		}			
		
		if(MessageBoxUtil.showConfirmMessageDialog(this, "등록 하시겠습니끼?") == JOptionPane.YES_OPTION) {		
			jReply = ProductSales.getInstance().registerProductSales(
					this.rpcClient, 
					this.connectionInfo, 
					this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
					this.loginUserInfo.get("USER_ID").getAsString(),
					txtProductSalesSerialNo.getText(),
					new SimpleDateFormat("yyyyMMdd").format(((GregorianCalendar)dtProductSalesDate.getModel().getValue()).getTime()),
					cboCustomerId.getSelectedItem().toString(),
					cboProductName.getSelectedItem().toString().split(":")[0].replace("{", "").replace("}", ""),
					cboProductName.getSelectedItem().toString().split(":")[1].replace("{", "").replace("}", ""),
					Integer.parseInt(txtUnitPrice.getText()),
					Float.parseFloat(txtProductSalesCount.getText()),
					Float.parseFloat(txtProductSalesAmount.getText()),
					Float.parseFloat(txtReceivedAmount.getText()),
					txtProductSalesComment.getText()
					);
				
			if(jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString().equals(BrunnerClientApi.resultCode_success)) {
				btnViewProductSalesList_click();
			}
			else
				MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}
	
	void btnUnregisterProductSales_click() throws Exception {
		if(this.tblProductSalesList.getSelectedRowCount() == 0 ) {
			JOptionPane.showMessageDialog(this, "매출목록에서 삭제할 항목을 선택하세요.");
			tblProductSalesList.requestFocus();
			return;
		}
		
		if(MessageBoxUtil.showConfirmMessageDialog(this, "삭제 하시겠습니끼?") == JOptionPane.YES_OPTION) {
			JsonObject jReply = ProductSales.getInstance().unregisterProductSales(
					this.rpcClient, 
					this.connectionInfo, 
					this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
					this.loginUserInfo.get("USER_ID").getAsString(),
					txtProductSalesSerialNo.getText()
					);
				
			if(jReply.get(BrunnerClientApi.msgFieldName_resultCode).getAsString().equals(BrunnerClientApi.resultCode_success)) {
				btnViewProductSalesList_click();
			}
			else
				MessageBoxUtil.showErrorMessageDialog(MainFrame.getInstance(), jReply.get(BrunnerClientApi.msgFieldName_resultMessage).getAsString());
		}
	}

	void clearForm() {
		dtProductSalesDate.getModel().setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));		
		txtProductSalesSerialNo.setText("");
		cboCustomerId.setSelectedIndex(-1);
		txtRegisterName.setText("");
		cboProductName.setSelectedIndex(-1);
		txtUnitPrice.setText("");
		txtProductSalesCount.setText("");
		txtProductSalesAmount.setText("");
		txtReceivedAmount.setText("");
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
			
			cboCustomerId_View.removeAllItems();
			cboCustomerId_View.addItem("");
			
			for(int rowIndex = 0; rowIndex < jReply.get("customerList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("customerList").getAsJsonArray().get(rowIndex);
				
				cboCustomerId.addItem(jRowData.get("customerId").getAsString());
				cboCustomerId_View.addItem(jRowData.get("customerId").getAsString());
				tmpUserCustomerList.put(jRowData.get("customerId").getAsString(), jRowData);
			}
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}
	
	void cboCustomerId_selectionChanged() {
		if(cboCustomerId.getSelectedItem() == null || StringUtil.isNullOrEmpty(cboCustomerId.getSelectedItem().toString()) == true)
			txtRegisterName.setText("");
		else
			txtRegisterName.setText(tmpUserCustomerList.get(cboCustomerId.getSelectedItem().toString()).get("registerName").getAsString());
	}
	
	void viewProductList() throws Exception {
		JsonObject jReply = Product.getInstance().viewProductList(
				this.rpcClient, 
				this.connectionInfo, 
				this.loginUserInfo.get("SYSTEM_CODE").getAsString(), 
				this.loginUserInfo.get("USER_ID").getAsString());

		if(jReply.get("resultCode").getAsString().equals(BrunnerClientApi.resultCode_success)) {
			cboProductName.removeAllItems();
			cboProductName.addItem("");
			for(int rowIndex = 0; rowIndex < jReply.get("productList").getAsJsonArray().size(); rowIndex++) {
				JsonObject jRowData = (JsonObject) jReply.get("productList").getAsJsonArray().get(rowIndex);
				
				cboProductName.addItem(String.format("{%s}:{%s}", jRowData.get("productName").getAsString(), jRowData.get("productUnit").getAsString()));
				tmpProductList.put(String.format("{%s}:{%s}", jRowData.get("productName").getAsString(), jRowData.get("productUnit").getAsString()), jRowData);
			}
		}
		else {
			MessageBoxUtil.showErrorMessageDialog(this, jReply.get("resultMessage").getAsString());
		}		
	}	
	
	void cboProductName_selectionChanged() {
		if(cboProductName.getSelectedItem() == null || cboProductName.getSelectedItem().equals(""))
			txtUnitPrice.setText("");
		else
			txtUnitPrice.setText(tmpProductList.get(cboProductName.getSelectedItem().toString()).get("unitPrice").getAsString());
			
		txtProductSalesCount.setText("");
		txtProductSalesAmount.setText("");
		txtReceivedAmount.setText("");
		txtProductSalesComment.setText("");
	}

	/***
	Class.getResourceAsStream(filePath) 함수 사용법

	1) 경로에 '/' 붙지 않은경우 예)"test.txt": 클래스가 위치한 패키지 내에 파읽 읽음.
	  InputStream in = this.getClass().getResourceAsStream("test.txt"); 
	2) 경로에 '/' 붙은 경우, ClassLoader의 모든 경로에서 파일 읽음. 보통 src/main/resources 폴더의 파일 읽을때 사용.
	  InputStream in = this.getClass().getResourceAsStream("/test.txt"); 
	3) 경로에 '/' 붙은 경우, ClassLoader의 모든 경로에서 파일 읽음.
 	  InputStream in = this.getClass().getResourceAsStream("/com/test/controller/test.txt"); 

	ClassLoader().getResourceAaStream() 함수 사용법

 	1) 경로에 '/' 붙지 않은 경우, ClassLoader의 모든 경로에서 파일 읽음. 보통 src/main/resources 폴더의 파일 읽을때 사용.
	  InputStream in = this.getClass().getClassLoader().getResourceAsStream("test.txt"); 
	2) 경로에 '/' 붙지 않은 경우. ClassLoader의 모든 경로에서 파일 읽음.
	  InputStream in = this.getClass().getClassLoader().getResourceAsStream("com/test/controller/test.txt"); 		
	 */
	void btnViewReceit_click() throws InvalidFormatException, IOException, URISyntaxException{
		if(this.tblProductSalesList.getSelectedRows().length == 0){
			MessageBoxUtil.showConfirmMessageDialog(this,"항목을 선택하세요");
			return;
		}

		// jar 파일 내에 있는 영수증 템플릿 파일을 현재 실행 디렉토리에 복사
		String outputDirName = Paths.get("").toAbsolutePath().getParent().toString();
		String receitTemplateFilePath = "/receitTemplate.xls";
        InputStream fis = this.getClass().getResourceAsStream(receitTemplateFilePath); // 현재 클래스 기준 리소스 폴더에서 템플릿 파일 획득
		String receitOutputFileName = "receit_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xls";
		OutputStream fos = new FileOutputStream(outputDirName + "/" + receitOutputFileName);

        byte[] buf = new byte[1024];
        int readData;
        while ((readData = fis.read(buf)) > 0) {
            fos.write(buf, 0, readData);
        }
        fis.close();
        fos.close();
		
		// 복사한 템플릿 엑셀 파일 불러오기
		FileInputStream inputStream = new FileInputStream(outputDirName + "/" + receitOutputFileName);
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);//엑셀읽기
		HSSFSheet sheet = workbook.getSheetAt(0);//시트가져오기 0은 첫번째 시트

		String remarkValue = ""; // 비고

		// Left Receit
		setValue(sheet, "F", 4, this.txtProductSalesSerialNo.getText()); // F4 No
		setValue(sheet, "H", 4, this.txtRegisterName.getText()); // H4 영수자이름

		// 공급자 정보
		setValue(sheet, "H", 5, this.loginUserInfo.get("REGISTER_NO").getAsString()); // H5 사업자등록번호
		setValue(sheet, "H", 6, this.loginUserInfo.get("REGISTER_NAME").getAsString()); // H6 상호
		setValue(sheet, "M", 6, this.loginUserInfo.get("USER_NAME").getAsString()); // M6 성명
		setValue(sheet, "H", 7, this.loginUserInfo.get("ADDRESS").getAsString()); // H7 사업장소재지
		setValue(sheet, "H", 8, this.loginUserInfo.get("SALES_TYPE").getAsString()); // H8 업태
		setValue(sheet, "L", 8, this.loginUserInfo.get("SALES_CATEGORY").getAsString()); // L8 종목 
	
		// 작성년월, 금액, 비고	
		setValue(sheet, "E", 10, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))); // E10 작성년월일

		Float amountTotal = 0.0F;
		// 항목별 (최대 15개)
		int itemCount = Math.min(tblProductSalesList.getSelectedRowCount(), 15);

		for(int itemIndex = 0; itemIndex < itemCount; itemIndex++){
			int rowIndex = tblProductSalesList.getSelectedRows()[itemIndex];
			amountTotal += Float.parseFloat(tblProductSalesList.getModel().getValueAt(rowIndex, 9).toString());
		}		

		setValue(sheet, "H", 10, amountTotal.toString()); // H10  금액
		setValue(sheet, "M", 10, remarkValue); // M10  비고

		int itemRowIndex = -1;
		String itemDateValue = ""; //월일
		String itemProductName = ""; // 품목
		String itemCountString = ""; // 항목별 수량
		String itemUnitPriceString = ""; // 항목별 단가
		String itemPriceString = ""; // 항목별 금액

		for(int itemIndex = 0; itemIndex < itemCount; itemIndex++){
			int rowIndex = tblProductSalesList.getSelectedRows()[itemIndex];
			itemRowIndex = itemIndex + 14;

			itemDateValue = tblProductSalesList.getModel().getValueAt(rowIndex, 0).toString().substring(tblProductSalesList.getModel().getValueAt(rowIndex, 0).toString().length() - 4); //월일
			itemProductName = tblProductSalesList.getModel().getValueAt(rowIndex, 4).toString(); // 품목
			itemCountString = tblProductSalesList.getModel().getValueAt(rowIndex, 7).toString(); // 수량
			itemUnitPriceString = tblProductSalesList.getModel().getValueAt(rowIndex, 6).toString(); // 단가
			itemPriceString = tblProductSalesList.getModel().getValueAt(rowIndex, 9).toString(); // 금액

			setValue(sheet, "E",  itemRowIndex, itemDateValue ); // E14 부터 월일
			setValue(sheet, "F",  itemRowIndex, itemProductName); // F14 부터 품목
			setValue(sheet, "K",  itemRowIndex, itemCountString); // K14 부터 수량
			setValue(sheet, "L",  itemRowIndex, itemUnitPriceString); // L14 부터 단가
			setValue(sheet, "M",  itemRowIndex, itemPriceString); // M14 부터 금액
		}

		// Right Receit
		setValue(sheet, "Z", 4, this.txtProductSalesSerialNo.getText()); // Z4 No
		setValue(sheet, "AB", 4, this.txtRegisterName.getText()); // AB4 영수자이름

		// 공급자 정보
		setValue(sheet, "AB", 5, this.loginUserInfo.get("REGISTER_NO").getAsString()); // AB5 사업자등록번호
		setValue(sheet, "AB", 6, this.loginUserInfo.get("REGISTER_NAME").getAsString()); // AB6 상호
		setValue(sheet, "AG", 6, this.loginUserInfo.get("USER_NAME").getAsString()); // AG6 성명
		setValue(sheet, "AB", 7, this.loginUserInfo.get("ADDRESS").getAsString()); // AB7 사업장소재지
		setValue(sheet, "AB", 8, this.loginUserInfo.get("SALES_TYPE").getAsString()); // AB8 업태
		setValue(sheet, "AF", 8, this.loginUserInfo.get("SALES_CATEGORY").getAsString()); // AF8 종목 
	
		// 작성년월, 금액, 비고	
		setValue(sheet, "Y", 10, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))); // Y 작성년월일
		setValue(sheet, "AB", 10, amountTotal.toString()); // AB10  금액
		setValue(sheet, "AG", 10, remarkValue); // AG10  비고

		for(int itemIndex = 0; itemIndex < itemCount; itemIndex++){
			int rowIndex = tblProductSalesList.getSelectedRows()[itemIndex];
			itemRowIndex = itemIndex + 14;

			itemDateValue = tblProductSalesList.getModel().getValueAt(rowIndex, 0).toString().substring(tblProductSalesList.getModel().getValueAt(rowIndex, 0).toString().length() - 4); //월일
			itemProductName = tblProductSalesList.getModel().getValueAt(rowIndex, 4).toString(); // 품목
			itemCountString = tblProductSalesList.getModel().getValueAt(rowIndex, 7).toString(); // 수량
			itemUnitPriceString = tblProductSalesList.getModel().getValueAt(rowIndex, 6).toString(); // 단가
			itemPriceString = tblProductSalesList.getModel().getValueAt(rowIndex, 9).toString(); // 금액

			setValue(sheet, "Y",  itemRowIndex, itemDateValue ); // Y14 부터 월일
			setValue(sheet, "Z",  itemRowIndex, itemProductName); // Z14 부터 품목
			setValue(sheet, "AE",  itemRowIndex, itemCountString); // AE14 부터 수량
			setValue(sheet, "AF",  itemRowIndex, itemUnitPriceString); // AF14 부터 단가
			setValue(sheet, "AG",  itemRowIndex, itemPriceString); // AG14 부터 금액
		}		

		inputStream.close();
		fos = new FileOutputStream(new File(outputDirName + "/" + receitOutputFileName));
		workbook.write(fos);
		workbook.close();
		fos.close();
		 
		MessageBoxUtil.showInformationMessageDialog(this, String.format("영수증을 %s에 저장했습니다.", outputDirName + "/" + receitOutputFileName));
		
		JBrunnerTable.runExcel(outputDirName + "/" + receitOutputFileName);
	}

	// 특정 셀에 특정 값 넣기
	private void setValue(HSSFSheet sheet, String columnId ,int rowIndex, String value) {
		String position = String.format("%s%d", columnId, rowIndex);
		CellReference ref = new CellReference(position);
		Row r = sheet.getRow(ref.getRow());
		if (r != null) {
			Cell c = r.getCell(ref.getCol());
			c.setCellValue(value);
		}
	}	

}
