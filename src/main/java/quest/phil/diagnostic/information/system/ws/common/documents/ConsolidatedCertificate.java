package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionToxicology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionXRay;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.toxi.QisTransactionLabToxicology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.xray.QisTransactionLabXRay;

public class ConsolidatedCertificate {
	private String applicationName;
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;

	public ConsolidatedCertificate(String applicationName, String applicationHeader, String applicationFooter,
			AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionConsolidatedCertificate(Document document, PdfWriter pdfWriter,
			QisQualityTransaction qisQualityTransaction, boolean withHeaderFooter)
			throws DocumentException, IOException {

		Certificate certificate = new Certificate(appUtility);
		QisTransaction qisTransaction = appUtility.getQisTransaction(qisQualityTransaction.getTransactionid());

		// PACKAGES ONLY
		Set<QisQualityTransactionItem> packageItems = qisQualityTransaction.getTransactionItems().stream()
				.filter(l -> "PCK".equals(l.getItemType())).collect(Collectors.toSet());
		for (QisQualityTransactionItem pck : packageItems) {
			if ("IndustrialPackage".equals(pck.getQisPackage().getTypeTestPackage())) {
				printPackageResults(document, pdfWriter, qisTransaction, pck, certificate, withHeaderFooter);
			} else {
				printPackageResultsTest(document, pdfWriter, qisTransaction, pck, certificate, withHeaderFooter);
			}
		}

		// ITEMS ONLY
		if (packageItems.size() <= 0) {
			Set<QisQualityTransactionItem> itemList = qisQualityTransaction.getTransactionItems().stream()
					.filter(l -> "ITM".equals(l.getItemType())).collect(Collectors.toSet());
			if (itemList.size() > 0) {
				printItemResults(document, pdfWriter, qisTransaction, itemList, certificate, withHeaderFooter);
			}
		}
		return document;
	}

	public void printPackageResults(Document document, PdfWriter pdfWriter, QisTransaction qisTransaction,
			QisQualityTransactionItem pckItem, Certificate certificate, boolean withHeaderFooter)
			throws BadElementException, IOException {

		QisHeaderFooterImageEvent event = new QisHeaderFooterImageEvent(applicationHeader, applicationFooter,
				certificate, withHeaderFooter);
		pdfWriter.setPageEvent(event);

		document.setMargins(10, 10, 10, 10);
		document.open();

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell cellBorder = new PdfPCell();
		cellBorder.setBorder(Rectangle.BOTTOM);
		cellBorder.setBorderColor(Color.BLUE);
		cellBorder.setVerticalAlignment(Element.ALIGN_BOTTOM);

		boolean hasPE = false;
		// PHYSICAL EXAMINATION
		Set<QisQualityTransactionLaboratory> peLabRequests = pckItem.getItemLaboratories().stream()
				.filter(l -> "PE".equals(l.getItemDetails().getItemLaboratory())).collect(Collectors.toSet());
		if (peLabRequests.size() > 0) {
			PhysicalExamCertificate peCertificate = new PhysicalExamCertificate(applicationName, applicationHeader,
					applicationFooter, appUtility);

			for (QisQualityTransactionLaboratory peLab : peLabRequests) {
				if (peLab.getPhysicalExamination() != null
						&& (peLab.getPhysicalExamination().getMedicalHistory() != null
								|| peLab.getPhysicalExamination().getVitalSign() != null
								|| peLab.getPhysicalExamination().getPhysicalExam() != null)) {

					QisTransactionPhysicalExamination qisTransactionPhysicalExamination = new QisTransactionPhysicalExamination();
					QisLaboratoryPhysicalExamination labPhysicalExamination = peLab.getPhysicalExamination();
					BeanUtils.copyProperties(labPhysicalExamination, qisTransactionPhysicalExamination);

					qisTransactionPhysicalExamination.setItemDetails(peLab.getItemDetails());
					peCertificate.formatPhysicalExamination(document, qisTransaction, qisTransactionPhysicalExamination,
							true, certificate);

					hasPE = true;
				}
			}
		}

		if (hasPE) {
			document.newPage();
		}

		// MEDICAL CERTIFICATE
		QisTransactionItemLaboratories qisTxnItemLab = appUtility
				.getQisTransactionItemLaboratories(pckItem.getTransactionid(), pckItem.getId());
		MedicalCertificate medCertificate = new MedicalCertificate(applicationName, applicationHeader,
				applicationFooter, appUtility);
		medCertificate.formatMedicalCertificateClassification(document, qisTransaction, qisTxnItemLab, certificate,
				cell, cellBorder);

		IndustrialCertificate indCertificate = new IndustrialCertificate(applicationHeader, applicationFooter,
				appUtility);
		indCertificate.formatIndustrialClassification(document, pdfWriter, qisTransaction, qisTxnItemLab, true,
				certificate);

		// XRAY
		Set<QisTransactionLaboratoryInfo> xrLabRequests = qisTxnItemLab.getTransactionLabRequests().stream()
				.filter(l -> "XR".equals(l.getItemDetails().getItemLaboratory())).collect(Collectors.toSet());
		if (xrLabRequests.size() > 0) {
			XRayCertificate xrCertificate = new XRayCertificate(applicationHeader, applicationFooter, appUtility);

			for (QisTransactionLaboratoryInfo xrLab : xrLabRequests) {
				if (xrLab.getXray() != null) {
					QisTransactionXRay qisTransactionXRay = new QisTransactionXRay();
					QisTransactionLabXRay labXRay = xrLab.getXray();
					BeanUtils.copyProperties(labXRay, qisTransactionXRay);

					qisTransactionXRay.setItemDetails(xrLab.getItemDetails());
					qisTransactionXRay.setXray(labXRay);
					qisTransactionXRay.setLabPersonel(labXRay.getTechnician());
					xrCertificate.formatXRayClassification(document, qisTransaction, qisTransactionXRay,
							withHeaderFooter, certificate);
				}
			}
		}

		document.close();
	}

	@SuppressWarnings("unused")
	private void printPackageResultsTest(Document document, PdfWriter pdfWriter, QisTransaction qisTransaction,
			QisQualityTransactionItem pckItem, Certificate certificate, boolean withHeaderFooter) {

		QisTransactionItemLaboratories qisTxnItemLab = appUtility
				.getQisTransactionItemLaboratories(pckItem.getTransactionid(), pckItem.getId());

		QisUserPersonel labPersonel1 = qisTxnItemLab.getLabPersonel();
		QisDoctor doctor1 = qisTxnItemLab.getMedicalDoctor();
		QisUserPersonel qualityControl1 = qisTxnItemLab.getClassifiedBy();
		Calendar dateReported = null;

		if (dateReported == null) {
			dateReported = pckItem.getClassifiedDate();
		} else {
			if (dateReported.after(pckItem.getClassifiedDate())) {
				dateReported = pckItem.getClassifiedDate();
			}
		}

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, labPersonel1, qualityControl1, doctor1, "Pathologist", dateReported, withHeaderFooter,
				true, "", null);
		pdfWriter.setPageEvent(event);

		document.setMargins(10, 10, 10, 150);
		document.open();

		Font fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(11);
		fontTitle.setColor(Color.BLACK);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		PdfPCell cellBorder = new PdfPCell();
		cellBorder.setBorder(Rectangle.BOTTOM);
		cellBorder.setBorderColor(Color.BLUE);
		cellBorder.setVerticalAlignment(Element.ALIGN_BOTTOM);

		boolean isMale = "M".equals(qisTransaction.getPatient().getGender());

		PdfPTable tableResults = new PdfPTable(9);
		tableResults.setSpacingBefore(5);
		tableResults.setWidthPercentage(90f);
		tableResults.setHorizontalAlignment(Element.ALIGN_CENTER);

		Set<QisTransactionLaboratoryInfo> cmLabRequests = qisTxnItemLab.getTransactionLabRequests().stream()
				.filter(l -> "CM".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (!cmLabRequests.isEmpty()) {
			ClinicalMicroscopyCertificate cmCertificate = new ClinicalMicroscopyCertificate(applicationHeader,
					applicationFooter, appUtility);
			
			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "CLINICAL MICROSCOPY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);
			
			Set<QisTransactionLaboratoryInfo> cmUCHEMList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> cmFECAList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> cmPREGTList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> cmOBTList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> cmAFBList = new HashSet<>();
			
			
			for (QisTransactionLaboratoryInfo cmLab : cmLabRequests) {
				Set<QisLaboratoryProcedureService> services = cmLab.getItemDetails().getServiceRequest();
				
				boolean isAdd = false;
				QisLaboratoryProcedureService uchem = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("UCHEM")).findAny().orElse(null);
				if (!isAdd && uchem != null) {
					cmUCHEMList.add(cmLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService fecalysis = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("FECA")).findAny().orElse(null);
				if (!isAdd && fecalysis != null) {
					cmFECAList.add(cmLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService pregnancyTest = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("PREGT")).findAny().orElse(null);
				if (!isAdd && pregnancyTest != null) {
					cmPREGTList.add(cmLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService occultBloodTest = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("OBT")).findAny().orElse(null);
				if (!isAdd && occultBloodTest != null) {
					cmOBTList.add(cmLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService acidFastBacilli = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("AFB")).findAny().orElse(null);
				if (!isAdd && acidFastBacilli != null) {
					cmAFBList.add(cmLab);
					isAdd = true;
				}
			}
			
			formatClinicalMicroscopyDataPackage(cmCertificate, tableResults, certificate, isMale, cmUCHEMList);
			formatClinicalMicroscopyDataPackage(cmCertificate, tableResults, certificate, isMale, cmFECAList);
			formatClinicalMicroscopyDataPackage(cmCertificate, tableResults, certificate, isMale, cmPREGTList);
			formatClinicalMicroscopyDataPackage(cmCertificate, tableResults, certificate, isMale, cmOBTList);
			formatClinicalMicroscopyDataPackage(cmCertificate, tableResults, certificate, isMale, cmAFBList);
		}
		
		Set<QisTransactionLaboratoryInfo> heLabRequests = qisTxnItemLab.getTransactionLabRequests().stream()
				.filter(l -> "HE".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());

		if (!heLabRequests.isEmpty()) { 
			HematologyCertificate heCertificate = new HematologyCertificate(applicationHeader, applicationFooter,
					appUtility);
			
			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "HEMATOLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);
			
			Set<QisTransactionLaboratoryInfo> heCBCList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> heBTYPList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> heCTMList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> heBTMList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> hePR131List = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> heMASMList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> heESRList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> hePTMList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> heAPTTList = new HashSet<>();
			
			for (QisTransactionLaboratoryInfo heQualityItem : heLabRequests) {
				Set<QisLaboratoryProcedureService> services = heQualityItem.getItemDetails().getServiceRequest();
				
				boolean isAdd = false;
				QisLaboratoryProcedureService completeBloodCount = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("CBC")).findAny().orElse(null);
				if (!isAdd && completeBloodCount != null) {
					heCBCList.add(heQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService bloodTyping = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("BTYP")).findAny().orElse(null);
				if (!isAdd && bloodTyping != null) {
					heBTYPList.add(heQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService clottingTime = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("CTM")).findAny().orElse(null);
				if (!isAdd && clottingTime != null) {
					heCTMList.add(heQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService bleedingTime = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("BTM")).findAny().orElse(null);
				if (!isAdd && bleedingTime != null) {
					heBTMList.add(heQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService pr131 = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("PR131")).findAny().orElse(null);
				if (!isAdd && pr131 != null) {
					hePR131List.add(heQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService masm = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("MASM")).findAny().orElse(null);
				if (!isAdd && masm != null) {
					heMASMList.add(heQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService esr = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("ESR")).findAny().orElse(null);
				if (!isAdd && esr != null) {
					heESRList.add(heQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService prothrombinTime = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("PTM")).findAny().orElse(null);
				if (!isAdd && prothrombinTime != null) {
					hePTMList.add(heQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService apttData = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("APTT")).findAny().orElse(null);
				if (!isAdd && apttData != null) {
					heAPTTList.add(heQualityItem);
					isAdd = true;
				}
				
			}
			
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, heCBCList);
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, heBTYPList);
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, heCTMList);
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, heBTMList);
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, hePR131List);
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, heMASMList);
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, heESRList);
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, hePTMList);
			formatHematologyDataPackage(heCertificate, tableResults, certificate, isMale, heAPTTList);
		}

		Set<QisTransactionLaboratoryInfo> chLabRequests = qisTxnItemLab.getTransactionLabRequests().stream()
				.filter(l -> "CH".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (!chLabRequests.isEmpty()) {	
			ChemistryCertificate chCertificate = new ChemistryCertificate(applicationHeader, applicationFooter, appUtility);
			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "CHEMISTRY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);
			Set<QisTransactionLaboratoryInfo> chFBSList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chRBSList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chPPRBSList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chURACList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chBUNList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chCREAList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chLIPPList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chOGTTList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chOGCTList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chELECList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chENZYList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chCPKList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chBILIList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chPROTList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> chHBA1CList = new HashSet<>();
			
			for (QisTransactionLaboratoryInfo chQualityItem : chLabRequests) {
				Set<QisLaboratoryProcedureService> services = chQualityItem.getItemDetails().getServiceRequest();
				
				boolean isAdd = false;
				QisLaboratoryProcedureService fastingBloodSugar = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("FBS")).findAny().orElse(null);
				if (!isAdd && fastingBloodSugar != null) {
					chFBSList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService randomBloodSugar = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("RBS")).findAny().orElse(null);
				if (!isAdd && randomBloodSugar != null) {
					chRBSList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService ppRandomBloodSugar = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("PPRBS")).findAny().orElse(null);
				if (!isAdd && ppRandomBloodSugar != null) {
					chPPRBSList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService uricAcid = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("URAC")).findAny().orElse(null);
				if (!isAdd && uricAcid != null) {
					chURACList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService bloodUreaNitrogen = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("BUN")).findAny().orElse(null);
				if (!isAdd && bloodUreaNitrogen != null) {
					chBUNList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService creatinine = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("CREA")).findAny().orElse(null);
				if (!isAdd && creatinine != null) {
					chCREAList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService lipidProfile = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("LIPP")).findAny().orElse(null);
				if (!isAdd && lipidProfile != null) {
					chLIPPList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService ogtTest = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("OGTT")).findAny().orElse(null);
				if (!isAdd && ogtTest != null) {
					chOGTTList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService ogcTest = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("OGCT")).findAny().orElse(null);
				if (!isAdd && ogcTest != null) {
					chOGCTList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService serumElectrolytes = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("ELEC")).findAny().orElse(null);
				if (!isAdd && serumElectrolytes != null) {
					chELECList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService serumEnzymes = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("ENZY")).findAny().orElse(null);
				if (!isAdd && serumEnzymes != null) {
					chENZYList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService creatinePhosphokinase = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("CPK")).findAny().orElse(null);
				if (!isAdd && creatinePhosphokinase != null) {
					chCPKList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService bilirubin = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("BILI")).findAny().orElse(null);
				if (!isAdd && bilirubin != null) {
					chBILIList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService protein = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("PROT")).findAny().orElse(null);
				if (!isAdd && protein != null) {
					chPROTList.add(chQualityItem);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService hemoglobin = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("HBA1C")).findAny().orElse(null);
				if (!isAdd && hemoglobin != null) {
					chHBA1CList.add(chQualityItem);
					isAdd = true;
				}
				
			}
			
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chFBSList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chRBSList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chPPRBSList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chURACList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chBUNList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chCREAList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chLIPPList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chOGTTList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chOGCTList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chELECList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chENZYList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chCPKList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chBILIList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chPROTList);
			formatChemistryDataPackage(chCertificate, tableResults, certificate, isMale, chHBA1CList);
		}

		Set<QisTransactionLaboratoryInfo> seLabRequests = qisTxnItemLab.getTransactionLabRequests().stream()
				.filter(l -> "SE".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (!seLabRequests.isEmpty()) {
			SerologyCertificate seCertificate = new SerologyCertificate(applicationHeader, applicationFooter, appUtility);
			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "SEROLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);
			
			Set<QisTransactionLaboratoryInfo> seSERList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> seTHYRList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> seTYPHList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> seCRPList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> seHIVList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> seAGENList = new HashSet<>();
			Set<QisTransactionLaboratoryInfo> seCOVIDList = new HashSet<>();
			
			for (QisTransactionLaboratoryInfo seLab : seLabRequests) {
				Set<QisLaboratoryProcedureService> services = seLab.getItemDetails().getServiceRequest();
				
				boolean isAdd = false;
				QisLaboratoryProcedureService serology = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("SER")).findAny().orElse(null);
				if (!isAdd && serology != null) {
					seSERList.add(seLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService thyroid = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("THYR")).findAny().orElse(null);
				if (!isAdd && thyroid != null) {
					seTHYRList.add(seLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService typhidot = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("TYPH")).findAny().orElse(null);
				if (!isAdd && typhidot != null) {
					seTYPHList.add(seLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService cProtein = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("CRP")).findAny().orElse(null);
				if (!isAdd && cProtein != null) {
					seCRPList.add(seLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService hiv = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("HIV")).findAny().orElse(null);
				if (!isAdd && hiv != null) {
					seHIVList.add(seLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService antigen = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("AGEN")).findAny().orElse(null);
				if (!isAdd && antigen != null) {
					seAGENList.add(seLab);
					isAdd = true;
				}
				
				QisLaboratoryProcedureService covid = services.stream()
						.filter(s -> s.getLaboratoryRequest().toString().equals("COVID")).findAny().orElse(null);
				if (!isAdd && covid != null) {
					seCOVIDList.add(seLab);
					isAdd = true;
				}
			}
			
			formatSerologyDataPackage(seCertificate, tableResults, certificate, seSERList);
			formatSerologyDataPackage(seCertificate, tableResults, certificate, seTHYRList);
			formatSerologyDataPackage(seCertificate, tableResults, certificate, seTYPHList);
			formatSerologyDataPackage(seCertificate, tableResults, certificate, seCRPList);
			formatSerologyDataPackage(seCertificate, tableResults, certificate, seHIVList);
			formatSerologyDataPackage(seCertificate, tableResults, certificate, seAGENList);
			formatSerologyDataPackage(seCertificate, tableResults, certificate, seCOVIDList);
		}

		Set<QisTransactionLaboratoryInfo> toLabRequests = qisTxnItemLab.getTransactionLabRequests().stream()
				.filter(l -> "TO".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());

		if (!toLabRequests.isEmpty()) {
			ToxicologyCertificate toCertificate = new ToxicologyCertificate(applicationHeader, applicationFooter,
					appUtility);

			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "TOXICOLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);

			for (QisTransactionLaboratoryInfo toLab : toLabRequests) {
				QisTransactionToxicology qisTransactionToxicology = new QisTransactionToxicology();
				QisTransactionLabToxicology labToxicology = toLab.getToxicology();
				qisTransactionToxicology.setToxicology(labToxicology);
				qisTransactionToxicology.setItemDetails(toLab.getItemDetails());

				toCertificate.formatToxicologyConsolidated(qisTransactionToxicology, tableResults, certificate);
			}
		}
		
			
		document.add(tableResults);
		document.close();
	}

	

	public void printItemResults(Document document, PdfWriter pdfWriter, QisTransaction qisTransaction,
			Set<QisQualityTransactionItem> itemList, Certificate certificate, boolean withHeaderFooter)
			throws BadElementException, IOException {

		QisUserPersonel labPersonel = null;
		QisUserPersonel qualityControl = null;
		QisDoctor doctor = null;
		Calendar dateReported = null;

		Set<QisQualityTransactionItem> labItemList = itemList.stream()
				.filter(i -> "LAB".equals(((QisItem) i.getItemDetails()).getItemCategory()))
				.collect(Collectors.toSet());

		if (labItemList.size() > 0) {
			for (QisQualityTransactionItem labItem : labItemList) {
				for (QisQualityTransactionLaboratory lab : labItem.getItemLaboratories()) {
					if (dateReported == null) {
						dateReported = lab.getVerifiedDate();
					} else {
						if (dateReported.after(lab.getVerifiedDate())) {
							dateReported = lab.getVerifiedDate();
						}
					}

					if (labPersonel == null && lab.getLabPersonel() != null) {
						labPersonel = lab.getLabPersonel();
					}

					if (qualityControl == null && lab.getQualityControl() != null) {
						qualityControl = lab.getQualityControl();
					}

					if (doctor == null && lab.getMedicalDoctor() != null) {
						doctor = lab.getMedicalDoctor();
					}
				}
			}
		}

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, labPersonel, qualityControl, doctor, "Pathologist", dateReported, withHeaderFooter,
				true, "", null);
		pdfWriter.setPageEvent(event);

		document.setMargins(10, 10, 10, 150);
		document.open();

		Font fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(11);
		fontTitle.setColor(Color.BLACK);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		PdfPCell cellBorder = new PdfPCell();
		cellBorder.setBorder(Rectangle.BOTTOM);
		cellBorder.setBorderColor(Color.BLUE);
		cellBorder.setVerticalAlignment(Element.ALIGN_BOTTOM);

		boolean isMale = "M".equals(qisTransaction.getPatient().getGender());

		PdfPTable tableResults = new PdfPTable(9);
		tableResults.setSpacingBefore(5);
		tableResults.setWidthPercentage(90f);
		tableResults.setHorizontalAlignment(Element.ALIGN_CENTER);

		// CHEMISTRY
		Set<QisQualityTransactionItem> chItemList = itemList.stream()
				.filter(i -> "CH".equals(((QisItem) i.getItemDetails()).getItemLaboratoryProcedure()))
				.collect(Collectors.toSet());
		if (chItemList.size() > 0) {
			ChemistryCertificate chCertificate = new ChemistryCertificate(applicationHeader, applicationFooter,
					appUtility);

			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "CHEMISTRY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);

			Set<QisQualityTransactionLaboratory> chFBSList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chRBSList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chPPRBSList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chURACList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chBUNList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chCREAList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chLIPPList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chOGTTList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chOGCTList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chELECList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chENZYList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chCPKList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chBILIList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chPROTList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chHBA1CList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> chTIBCList = new HashSet<>();

			for (QisQualityTransactionItem chQualityItem : chItemList) {
				for (QisQualityTransactionLaboratory chLab : chQualityItem.getItemLaboratories()) {
					Set<QisLaboratoryProcedureService> services = chLab.getItemDetails().getServiceRequest();

					boolean isAdd = false;
					QisLaboratoryProcedureService fastingBloodSugar = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("FBS")).findAny().orElse(null);
					if (!isAdd && fastingBloodSugar != null) {
						chFBSList.add(chLab);
						isAdd = true;
					}
					
					QisLaboratoryProcedureService totalIronBindingCapacity = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("TIBC")).findAny().orElse(null);
					if (!isAdd && totalIronBindingCapacity != null) {
						chTIBCList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService randomBloodSugar = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("RBS")).findAny().orElse(null);
					if (!isAdd && randomBloodSugar != null) {
						chRBSList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService ppRandomBloodSugar = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("PPRBS")).findAny().orElse(null);
					if (!isAdd && ppRandomBloodSugar != null) {
						chPPRBSList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService uricAcid = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("URAC")).findAny().orElse(null);
					if (!isAdd && uricAcid != null) {
						chURACList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService bloodUreaNitrogen = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("BUN")).findAny().orElse(null);
					if (!isAdd && bloodUreaNitrogen != null) {
						chBUNList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService creatinine = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("CREA")).findAny().orElse(null);
					if (!isAdd && creatinine != null) {
						chCREAList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService lipidProfile = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("LIPP")).findAny().orElse(null);
					if (!isAdd && lipidProfile != null) {
						chLIPPList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService ogtTest = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("OGTT")).findAny().orElse(null);
					if (!isAdd && ogtTest != null) {
						chOGTTList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService ogcTest = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("OGCT")).findAny().orElse(null);
					if (!isAdd && ogcTest != null) {
						chOGCTList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService serumElectrolytes = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("ELEC")).findAny().orElse(null);
					if (!isAdd && serumElectrolytes != null) {
						chELECList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService serumEnzymes = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("ENZY")).findAny().orElse(null);
					if (!isAdd && serumEnzymes != null) {
						chENZYList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService creatinePhosphokinase = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("CPK")).findAny().orElse(null);
					if (!isAdd && creatinePhosphokinase != null) {
						chCPKList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService bilirubin = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("BILI")).findAny().orElse(null);
					if (!isAdd && bilirubin != null) {
						chBILIList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService protein = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("PROT")).findAny().orElse(null);
					if (!isAdd && protein != null) {
						chPROTList.add(chLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService hemoglobin = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("HBA1C")).findAny().orElse(null);
					if (!isAdd && hemoglobin != null) {
						chHBA1CList.add(chLab);
						isAdd = true;
					}
				}
			}
			
			

			formatChemistryData(chCertificate, tableResults, certificate, isMale, chFBSList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chRBSList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chPPRBSList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chURACList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chBUNList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chCREAList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chLIPPList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chOGTTList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chOGCTList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chELECList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chENZYList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chCPKList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chBILIList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chPROTList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chHBA1CList);
			formatChemistryData(chCertificate, tableResults, certificate, isMale, chTIBCList);

		}

		// HEMATOLOGY
		Set<QisQualityTransactionItem> heItemList = itemList.stream()
				.filter(i -> "HE".equals(((QisItem) i.getItemDetails()).getItemLaboratoryProcedure()))
				.collect(Collectors.toSet());
		if (heItemList.size() > 0) {
			HematologyCertificate heCertificate = new HematologyCertificate(applicationHeader, applicationFooter,
					appUtility);

			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "HEMATOLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);

			Set<QisQualityTransactionLaboratory> heCBCList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> heBTYPList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> heCTMList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> heBTMList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> hePR131List = new HashSet<>();
			Set<QisQualityTransactionLaboratory> heMASMList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> heESRList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> hePTMList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> heAPTTList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> heFerrList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> heRCTList = new HashSet<>();

			for (QisQualityTransactionItem heQualityItem : heItemList) {
				for (QisQualityTransactionLaboratory heLab : heQualityItem.getItemLaboratories()) {
					Set<QisLaboratoryProcedureService> services = heLab.getItemDetails().getServiceRequest();

					boolean isAdd = false;
					QisLaboratoryProcedureService completeBloodCount = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("CBC")).findAny().orElse(null);
					if (!isAdd && completeBloodCount != null) {
						heCBCList.add(heLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService bloodTyping = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("BTYP")).findAny().orElse(null);
					if (!isAdd && bloodTyping != null) {
						heBTYPList.add(heLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService clottingTime = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("CTM")).findAny().orElse(null);
					if (!isAdd && clottingTime != null) {
						heCTMList.add(heLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService bleedingTime = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("BTM")).findAny().orElse(null);
					if (!isAdd && bleedingTime != null) {
						heBTMList.add(heLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService pr131 = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("PR131")).findAny().orElse(null);
					if (!isAdd && pr131 != null) {
						hePR131List.add(heLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService masm = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("MASM")).findAny().orElse(null);
					if (!isAdd && masm != null) {
						heMASMList.add(heLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService esr = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("ESR")).findAny().orElse(null);
					if (!isAdd && esr != null) {
						heESRList.add(heLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService prothrombinTime = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("PTM")).findAny().orElse(null);
					if (!isAdd && prothrombinTime != null) {
						hePTMList.add(heLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService apttData = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("APTT")).findAny().orElse(null);
					if (!isAdd && apttData != null) {
						heAPTTList.add(heLab);
						isAdd = true;
					}
					
					QisLaboratoryProcedureService ferritinData = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("FERR")).findAny().orElse(null);
					if (!isAdd && ferritinData != null) {
						heFerrList.add(heLab);
						isAdd = true;
					}
					
					QisLaboratoryProcedureService RctData = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("RCT")).findAny().orElse(null);
					if (!isAdd && RctData != null) {
						heRCTList.add(heLab);
						isAdd = true;
					}
					
					
					
					
				}
			}

			formatHematologyData(heCertificate, tableResults, certificate, isMale, heCBCList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, heBTYPList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, heCTMList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, heBTMList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, hePR131List);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, heMASMList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, heESRList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, hePTMList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, heAPTTList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, heFerrList);
			formatHematologyData(heCertificate, tableResults, certificate, isMale, heRCTList);
		}

		// CLINICAL MICROSCOPY
		Set<QisQualityTransactionItem> cmItemList = itemList.stream()
				.filter(i -> "CM".equals(((QisItem) i.getItemDetails()).getItemLaboratoryProcedure()))
				.collect(Collectors.toSet());
		if (cmItemList.size() > 0) {
			ClinicalMicroscopyCertificate cmCertificate = new ClinicalMicroscopyCertificate(applicationHeader,
					applicationFooter, appUtility);

			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "CLINICAL MICROSCOPY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);

			Set<QisQualityTransactionLaboratory> cmUCHEMList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> cmFECAList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> cmPREGTList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> cmOBTList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> cmAFBList = new HashSet<>();

			for (QisQualityTransactionItem cmQualityItem : cmItemList) {
				for (QisQualityTransactionLaboratory cmLab : cmQualityItem.getItemLaboratories()) {
					Set<QisLaboratoryProcedureService> services = cmLab.getItemDetails().getServiceRequest();

					boolean isAdd = false;
					QisLaboratoryProcedureService uchem = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("UCHEM")).findAny().orElse(null);
					if (!isAdd && uchem != null) {
						cmUCHEMList.add(cmLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService fecalysis = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("FECA")).findAny().orElse(null);
					if (!isAdd && fecalysis != null) {
						cmFECAList.add(cmLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService pregnancyTest = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("PREGT")).findAny().orElse(null);
					if (!isAdd && pregnancyTest != null) {
						cmPREGTList.add(cmLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService occultBloodTest = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("OBT")).findAny().orElse(null);
					if (!isAdd && occultBloodTest != null) {
						cmOBTList.add(cmLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService acidFastBacilli = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("AFB")).findAny().orElse(null);
					if (!isAdd && acidFastBacilli != null) {
						cmAFBList.add(cmLab);
						isAdd = true;
					}
				}
			}

			formatClinicalMicroscopyData(cmCertificate, tableResults, certificate, isMale, cmUCHEMList);
			formatClinicalMicroscopyData(cmCertificate, tableResults, certificate, isMale, cmFECAList);
			formatClinicalMicroscopyData(cmCertificate, tableResults, certificate, isMale, cmPREGTList);
			formatClinicalMicroscopyData(cmCertificate, tableResults, certificate, isMale, cmOBTList);
			formatClinicalMicroscopyData(cmCertificate, tableResults, certificate, isMale, cmAFBList);

		}

		// SEROLOGY
		Set<QisQualityTransactionItem> seItemList = itemList.stream()
				.filter(i -> "SE".equals(((QisItem) i.getItemDetails()).getItemLaboratoryProcedure()))
				.collect(Collectors.toSet());
		if (seItemList.size() > 0) {
			SerologyCertificate seCertificate = new SerologyCertificate(applicationHeader, applicationFooter,
					appUtility);
			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "SEROLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);

			Set<QisQualityTransactionLaboratory> seSERList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seTHYRList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seTYPHList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seCRPList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seHIVList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seAGENList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seCOVIDList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seRFTList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> setphawtList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seAsoList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seDengueList = new HashSet<>();
			Set<QisQualityTransactionLaboratory> seTpnList = new HashSet<>();

			for (QisQualityTransactionItem seQualityItem : seItemList) {
				for (QisQualityTransactionLaboratory seLab : seQualityItem.getItemLaboratories()) {
					Set<QisLaboratoryProcedureService> services = seLab.getItemDetails().getServiceRequest();

					boolean isAdd = false;
					QisLaboratoryProcedureService serology = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("SER")).findAny().orElse(null);
					if (!isAdd && serology != null) {
						seSERList.add(seLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService thyroid = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("THYR")).findAny().orElse(null);
					if (!isAdd && thyroid != null) {
						seTHYRList.add(seLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService typhidot = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("TYPH")).findAny().orElse(null);
					if (!isAdd && typhidot != null) {
						seTYPHList.add(seLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService cProtein = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("CRP")).findAny().orElse(null);
					if (!isAdd && cProtein != null) {
						seCRPList.add(seLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService hiv = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("HIV")).findAny().orElse(null);
					if (!isAdd && hiv != null) {
						seHIVList.add(seLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService antigen = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("AGEN")).findAny().orElse(null);
					if (!isAdd && antigen != null) {
						seAGENList.add(seLab);
						isAdd = true;
					}

					QisLaboratoryProcedureService covid = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("COVID")).findAny().orElse(null);
					if (!isAdd && covid != null) {
						seCOVIDList.add(seLab);
						isAdd = true;
					}
					QisLaboratoryProcedureService rft = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("RFT")).findAny().orElse(null);
					if (!isAdd && rft != null) {
						seRFTList.add(seLab);
						isAdd = true;
					}
					
					QisLaboratoryProcedureService tpha = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("TPHA")).findAny().orElse(null);
					if (!isAdd && tpha != null) {
						setphawtList.add(seLab);
						isAdd = true;
					}
					
					QisLaboratoryProcedureService aso = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("ASO")).findAny().orElse(null);
					if (!isAdd && aso != null) {
						seAsoList.add(seLab);
						isAdd = true;
					}
					
					QisLaboratoryProcedureService dengue = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("DGE")).findAny().orElse(null);
					if (!isAdd && dengue != null) {
						seDengueList.add(seLab);
						isAdd = true;
					}
					
					QisLaboratoryProcedureService tpn = services.stream()
							.filter(s -> s.getLaboratoryRequest().toString().equals("TPN")).findAny().orElse(null);
					if (!isAdd && tpn != null) {
						seTpnList.add(seLab);
						isAdd = true;
					}
			
				}
			}

			formatSerologyData(seCertificate, tableResults, certificate, seSERList);
			formatSerologyData(seCertificate, tableResults, certificate, seTHYRList);
			formatSerologyData(seCertificate, tableResults, certificate, seTYPHList);
			formatSerologyData(seCertificate, tableResults, certificate, seCRPList);
			formatSerologyData(seCertificate, tableResults, certificate, seHIVList);
			formatSerologyData(seCertificate, tableResults, certificate, seAGENList);
			formatSerologyData(seCertificate, tableResults, certificate, seCOVIDList);
			formatSerologyData(seCertificate, tableResults, certificate, seRFTList);
			formatSerologyData(seCertificate, tableResults, certificate, setphawtList);
			formatSerologyData(seCertificate, tableResults, certificate, seAsoList);
			formatSerologyData(seCertificate, tableResults, certificate, seDengueList);
			formatSerologyData(seCertificate, tableResults, certificate, seTpnList);
		}

		// TOXICOLOGY
		Set<QisQualityTransactionItem> toItemList = itemList.stream()
				.filter(i -> "TO".equals(((QisItem) i.getItemDetails()).getItemLaboratoryProcedure()))
				.collect(Collectors.toSet());
		if (toItemList.size() > 0) {
			ToxicologyCertificate toCertificate = new ToxicologyCertificate(applicationHeader, applicationFooter,
					appUtility);

			cell.setColspan(5);
			certificate.addToTable(tableResults, cell, "TOXICOLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setColspan(1);

			for (QisQualityTransactionItem toQualityItem : toItemList) {
				for (QisQualityTransactionLaboratory toLab : toQualityItem.getItemLaboratories()) {
					QisTransactionToxicology qisTransactionToxicology = new QisTransactionToxicology();
					QisTransactionLabToxicology labToxicology = toLab.getToxicology();
					qisTransactionToxicology.setToxicology(labToxicology);
					qisTransactionToxicology.setItemDetails(toLab.getItemDetails());

					toCertificate.formatToxicologyConsolidated(qisTransactionToxicology, tableResults, certificate);
				}
			}
		}

		document.add(tableResults);

		document.close();
	}

	private void formatChemistryData(ChemistryCertificate chCertificate, PdfPTable tableResults,
			Certificate certificate, boolean isMale, Set<QisQualityTransactionLaboratory> chLabList) {
		for (QisQualityTransactionLaboratory chLab : chLabList) {
			QisTransactionChemistry qisChemistry = appUtility.getQisTransactionChemistry(chLab.getTransactionid(),
					chLab.getId());
			chCertificate.formatChemistryConsolidated(qisChemistry, tableResults, certificate, isMale);
		}
	}

	private void formatHematologyData(HematologyCertificate heCertificate, PdfPTable tableResults,
			Certificate certificate, boolean isMale, Set<QisQualityTransactionLaboratory> heLabList) {
		for (QisQualityTransactionLaboratory heLab : heLabList) {
			QisTransactionHematology qisHematology = appUtility.getQisTransactionHematology(heLab.getTransactionid(),
					heLab.getId());
			heCertificate.formatHematologyConsolidated(qisHematology, tableResults, certificate, isMale);
		}
	}

	private void formatClinicalMicroscopyData(ClinicalMicroscopyCertificate cmCertificate, PdfPTable tableResults,
			Certificate certificate, boolean isMale, Set<QisQualityTransactionLaboratory> cmLabList) {
		for (QisQualityTransactionLaboratory cmLab : cmLabList) {
			QisTransactionClinicalMicroscopy qisClinicalMicroscopy = appUtility
					.getQisTransactionClinicalMicroscopy(cmLab.getTransactionid(), cmLab.getId());
			cmCertificate.formatClinicalMicroscopyConsolodated(qisClinicalMicroscopy, tableResults, certificate,
					isMale);
		}
	}

	private void formatSerologyData(SerologyCertificate seCertificate, PdfPTable tableResults, Certificate certificate,
			Set<QisQualityTransactionLaboratory> seLabList) {
		for (QisQualityTransactionLaboratory seLab : seLabList) {
			QisTransactionSerology qisSerology = appUtility.getQisTransactionSerology(seLab.getTransactionid(),
					seLab.getId());
			seCertificate.formatSerologyConsolidated(qisSerology, tableResults, certificate);
		}
	}

	private void formatHematologyDataPackage(HematologyCertificate heCertificate, PdfPTable tableResults,
			Certificate certificate, boolean isMale, Set<QisTransactionLaboratoryInfo> heCTMList) {
		for (QisTransactionLaboratoryInfo heLab : heCTMList) {
			QisTransactionHematology qisHematology = appUtility.getQisTransactionHematology(heLab.getTransactionid(),
					heLab.getId());
			heCertificate.formatHematologyConsolidated(qisHematology, tableResults, certificate, isMale);
		}
	}

	private void formatChemistryDataPackage(ChemistryCertificate chCertificate, PdfPTable tableResults,
			Certificate certificate, boolean isMale, Set<QisTransactionLaboratoryInfo> chFBSList) {
		for (QisTransactionLaboratoryInfo chLab : chFBSList) {
			QisTransactionChemistry qisChemistry = appUtility.getQisTransactionChemistry(chLab.getTransactionid(),
					chLab.getId());
			chCertificate.formatChemistryConsolidated(qisChemistry, tableResults, certificate, isMale);
		}
	}

	private void formatSerologyDataPackage(SerologyCertificate seCertificate, PdfPTable tableResults,
			Certificate certificate, Set<QisTransactionLaboratoryInfo> seSERList) {
		for (QisTransactionLaboratoryInfo seLab : seSERList) {
			QisTransactionSerology qisSerology = appUtility.getQisTransactionSerology(seLab.getTransactionid(),
					seLab.getId());
			seCertificate.formatSerologyConsolidated(qisSerology, tableResults, certificate);
		}
	}
	
	private void formatClinicalMicroscopyDataPackage(ClinicalMicroscopyCertificate cmCertificate,
			PdfPTable tableResults, Certificate certificate, boolean isMale,
			Set<QisTransactionLaboratoryInfo> cmUCHEMList) {
		for (QisTransactionLaboratoryInfo cmLab : cmUCHEMList) {
			QisTransactionClinicalMicroscopy qisClinicalMicroscopy = appUtility
					.getQisTransactionClinicalMicroscopy(cmLab.getTransactionid(), cmLab.getId());
			cmCertificate.formatClinicalMicroscopyConsolodated(qisClinicalMicroscopy, tableResults, certificate,
					isMale);
		}
		
	}

}
