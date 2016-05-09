package com.qrp.agent.simulator.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.qrp.agent.simulator.model.Policy;
import com.qrp.agent.simulator.model.PolicyStatisticGeneral;

public class FilelUtils {
	private final static Logger LOGGER = Logger.getLogger(FilelUtils.class.getName());

	public Set<Policy> getPolicySetFromExcelFile(String filePath) throws IOException {
		int lastRowNum = 0;
		int autoReneEnabledCount = 0;
		int duplicateOmmitedCount = 0;
		FileInputStream fis = new FileInputStream(filePath);
		// Find workbook XLSX file instance
		XSSFWorkbook  workbook = new XSSFWorkbook(fis);
		// Get first sheet from XLSX workbook
		XSSFSheet sheet = workbook.getSheetAt(0);
		// Get current sheet iterator row
		Iterator<Row> rows = sheet.rowIterator();
		lastRowNum = sheet.getLastRowNum();
		Set<Policy> policySet = new LinkedHashSet<Policy>();
		while (rows.hasNext()) {
			Row row = rows.next();
			// Skip first row which contains only column names.
			if(row.getRowNum()==0 ) continue;
			Iterator<Cell> cells = row.cellIterator();
			Policy policy = new Policy();
			int cellNumber = 0;
			while (cells.hasNext()){
				XSSFCell cell=(XSSFCell) cells.next();
				cellNumber = cell.getRowIndex();
					 if (cell.getColumnIndex() == 0) {
						 policy.setAgentBreed(cell.getStringCellValue());
					 } else if (cell.getColumnIndex() == 1) {
						long policyId = Long.parseLong(cell.getRawValue());
						policy.setPolicyId(policyId);
					 } else if (cell.getColumnIndex() == 2) {
						int age = Integer.parseInt(cell.getRawValue());
						policy.setAge(age);
					 } else if (cell.getColumnIndex() == 3) {
						int socialGrade = Integer.parseInt(cell.getRawValue());
						policy.setSocialGrade(socialGrade); 
					 } else if (cell.getColumnIndex() == 4) {
						BigDecimal paymentAtPurchase = new BigDecimal(cell.getRawValue());
						policy.setPaymentAtPurchase(paymentAtPurchase);
					 } else if (cell.getColumnIndex() == 5) {
						BigDecimal atributeBrand = new BigDecimal(cell.getRawValue());
						policy.setAtributeBrand(atributeBrand); 
					 } else if (cell.getColumnIndex() == 6) {
						BigDecimal atributePrice = new BigDecimal(cell.getRawValue());
						policy.setAtributePrice(atributePrice );
					 } else if (cell.getColumnIndex() == 7) {
						BigDecimal atributePromotion = new BigDecimal(cell.getRawValue());
						policy.setAtributePromotion(atributePromotion );
					 } else if (cell.getColumnIndex() == 8) {
						byte autoRenew = Byte.parseByte(cell.getRawValue());
						policy.setAutoRenew(autoRenew ); 
					 } else if (cell.getColumnIndex() == 9) {
						 int inertiaForSwitch = Integer.parseInt(cell.getRawValue());
						policy.setInertiaForSwitch(inertiaForSwitch);
					 }
			}
			if (policy.getAutoRenew()==1) {
				autoReneEnabledCount++;
				continue;
			}
			boolean addResult = policySet.add(policy);
			if (!addResult) {
				duplicateOmmitedCount++;
				LOGGER.warning("duplicate policyId detected, record ommited: " +cellNumber+ "  policyId: " +policy.getPolicyId());
			};
		}
		LOGGER.info("last row num " +lastRowNum);
		LOGGER.info("duplicate ommited: " +duplicateOmmitedCount);
		LOGGER.info("auto renew ommited: " +autoReneEnabledCount);
		LOGGER.info("policy set size: " +policySet.size());
		return policySet;
	}

	public static void writePolicyStatisticGeneralExcel(List<PolicyStatisticGeneral> policyStatisticGeneralList, String writeFilePath) throws IOException {
		//String excelFileName = "C:/Users/qrp/Documents/excel/PolicySummary.xlsx";
		String sheetName = "Policy Summary Iteration";// name of sheet
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);
		// generate header
		int counter = 1;
		XSSFRow header = sheet.createRow(0);
		for (int c = 0; c < 4; c++) {
			XSSFCell cell = header.createCell(c);
			if (c == 0) {
				cell.setCellValue("Year");
			} else if (c == 1) {
				cell.setCellValue("Breed Lost");
			} else if (c == 2) {
				cell.setCellValue("Breed Gained");
			} else if (c == 3) {
				cell.setCellValue("Breed Regained");
			}
		}
		// generate rows after header
		for (PolicyStatisticGeneral policyStatisticGeneral : policyStatisticGeneralList) {
			XSSFRow row = sheet.createRow(counter);
			// iterating c number of columns
			for (int c = 0; c < 4; c++) {
				XSSFCell cell = row.createCell(c);

				if (c == 0) {
					cell.setCellValue(policyStatisticGeneral.getYear());
				} else if (c == 1) {
					cell.setCellValue(policyStatisticGeneral.getLostCount());
				} else if (c == 2) {
					cell.setCellValue(policyStatisticGeneral.getGainedCount());
				} else if (c == 3) {
					cell.setCellValue(policyStatisticGeneral.getReGainedCount());
				}
			}
			counter++;
		}
		FileOutputStream fileOut = new FileOutputStream(writeFilePath);
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();		
	}
}