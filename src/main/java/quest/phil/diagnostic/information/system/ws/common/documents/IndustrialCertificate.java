package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratorySerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionChemistry;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionClinicalMicroscopy;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionHematology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionSerology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionToxicology;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.toxi.QisTransactionLabToxicology;

public class IndustrialCertificate {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;

	public IndustrialCertificate(String applicationHeader, String applicationFooter, AppUtility appUtility) {
		super();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionLaboratoryIndustrialCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionItemLaboratories qisTxnItmLab, boolean withHeaderFooter)
			throws DocumentException, IOException {

		QisUserPersonel qc = null;
		if (qisTxnItmLab.getQcId() != null && !qisTxnItmLab.getQcId().equals(qisTxnItmLab.getLabPersonelId())) {
			qc = qisTxnItmLab.getQualityControl();
		} else {
			qc = qisTxnItmLab.getClassifiedBy();
		}

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, qisTxnItmLab.getLabPersonel(), qc, qisTxnItmLab.getMedicalDoctor(), "Pathologist",
				qisTxnItmLab.getLabPersonelDate(), withHeaderFooter, false, "", null);
		pdfWriter.setPageEvent(event);
		document.open();

		Certificate certificate = new Certificate(appUtility);

		formatIndustrial(document, qisTransaction, qisTxnItmLab, withHeaderFooter, certificate);

		document.close();
		return document;
	}

	public void formatIndustrial(Document document, QisTransaction qisTransaction,
			QisTransactionItemLaboratories qisTxnItmLab, boolean withHeaderFooter, Certificate certificate)
			throws BadElementException, IOException {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell cellBorder = new PdfPCell();
		cellBorder.setBorder(Rectangle.BOTTOM);
		cellBorder.setBorderColor(Color.BLUE);
		cellBorder.setVerticalAlignment(Element.ALIGN_BOTTOM);

		boolean isMale = "M".equals(qisTransaction.getPatient().getGender());

		// RESULTS
		PdfPTable tableReport = new PdfPTable(2);
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		cell.setColspan(2);
		certificate.addToTable(tableReport, cell, "LABORATORY RESULT", certificate.getFontTitle(),
				Element.ALIGN_CENTER);

		Font fontTitle;
		fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(10);
		fontTitle.setColor(Color.WHITE);

		// CLINICAL MICROSCOPY
		Set<QisTransactionLaboratoryInfo> cmLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "CM".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (cmLabRequests.size() > 0) {
			ClinicalMicroscopyCertificate cmCertificate = new ClinicalMicroscopyCertificate(applicationHeader,
					applicationFooter, appUtility);

			cell.setColspan(2);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
			cell.setBackgroundColor(new Color(66, 103, 178));
			certificate.addToTable(tableReport, cell, "CLINICAL MICROSCOPY", fontTitle, Element.ALIGN_LEFT);
			cell.setBackgroundColor(null);

			for (QisTransactionLaboratoryInfo cmLab : cmLabRequests) {
				QisTransactionClinicalMicroscopy qisTransactionClinicalMicroscopy = new QisTransactionClinicalMicroscopy();
				QisLaboratoryClinicalMicroscopy labClinicalMicroscopy = cmLab.getClinicalMicroscopy();

				BeanUtils.copyProperties(labClinicalMicroscopy, qisTransactionClinicalMicroscopy);

				qisTransactionClinicalMicroscopy.setOtherNotes(cmLab.getOtherNotes());
				qisTransactionClinicalMicroscopy.setItemDetails(cmLab.getItemDetails());
				cmCertificate.formatClinicalMicroscopyCertificate(qisTransactionClinicalMicroscopy, tableReport,
						certificate, cell, cellBorder, isMale);
			}
		}

		// HEMATOLOGY
		Set<QisTransactionLaboratoryInfo> heLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "HE".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (heLabRequests.size() > 0) {
			HematologyCertificate heCertificate = new HematologyCertificate(applicationHeader, applicationFooter,
					appUtility);

			cell.setColspan(2);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
			cell.setBackgroundColor(new Color(66, 103, 178));
			certificate.addToTable(tableReport, cell, "HEMATOLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setBackgroundColor(null);

			for (QisTransactionLaboratoryInfo heLab : heLabRequests) {
				QisTransactionHematology qisTransactionHematology = new QisTransactionHematology();
				QisLaboratoryHematology labHematology = heLab.getHematology();

				BeanUtils.copyProperties(labHematology, qisTransactionHematology);

				qisTransactionHematology.setOtherNotes(heLab.getOtherNotes());
				qisTransactionHematology.setItemDetails(heLab.getItemDetails());
				heCertificate.formatHematologyCertificate(qisTransactionHematology, tableReport, certificate, cell,
						cellBorder, isMale);

			}
		}

		// CHEMISTRY
		Set<QisTransactionLaboratoryInfo> chLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "CH".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (chLabRequests.size() > 0) {
			ChemistryCertificate chCertificate = new ChemistryCertificate(applicationHeader, applicationFooter,
					appUtility);

			cell.setColspan(2);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
			cell.setBackgroundColor(new Color(66, 103, 178));
			certificate.addToTable(tableReport, cell, "CHEMISTRY", fontTitle, Element.ALIGN_LEFT);
			cell.setBackgroundColor(null);

			for (QisTransactionLaboratoryInfo chLab : chLabRequests) {
				QisTransactionChemistry qisTransactionChemistry = new QisTransactionChemistry();
				QisLaboratoryChemistry labChemistry = chLab.getChemistry();

				BeanUtils.copyProperties(labChemistry, qisTransactionChemistry);

				qisTransactionChemistry.setOtherNotes(chLab.getOtherNotes());
				qisTransactionChemistry.setItemDetails(chLab.getItemDetails());
				chCertificate.formatChemistryCertificate(qisTransactionChemistry, tableReport, certificate, cell,
						cellBorder, isMale);
			}
		}

		// SEROLOGY
		Set<QisTransactionLaboratoryInfo> seLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "SE".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (seLabRequests.size() > 0) {
			SerologyCertificate seCertificate = new SerologyCertificate(applicationHeader, applicationFooter,
					appUtility);

			cell.setColspan(2);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
			cell.setBackgroundColor(new Color(66, 103, 178));
			certificate.addToTable(tableReport, cell, "SEROLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setBackgroundColor(null);

			for (QisTransactionLaboratoryInfo seLab : seLabRequests) {
				QisTransactionSerology qisTransactionSerology = new QisTransactionSerology();
////				QisTransactionSerology qisTransactionSerology = appUtility.getQisTransactionSerology(seLab.getTransactionid(),
//						seLab.getId());
				QisLaboratorySerology labSerology = seLab.getSerology();

				BeanUtils.copyProperties(labSerology, qisTransactionSerology);

				qisTransactionSerology.setOtherNotes(seLab.getOtherNotes());
				qisTransactionSerology.setItemDetails(seLab.getItemDetails());
				seCertificate.formatSerologyCertificate(qisTransactionSerology, tableReport, certificate, cell,
						cellBorder);
			}
		}

		// TOXICOLOGY
		Set<QisTransactionLaboratoryInfo> toLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "TO".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (toLabRequests.size() > 0) {
			ToxicologyCertificate toCertificate = new ToxicologyCertificate(applicationHeader, applicationFooter,
					appUtility);

			cell.setColspan(2);
			cell.setFixedHeight(10);
			certificate.addToTable(tableReport, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
			cell.setFixedHeight(0);
			cell.setBackgroundColor(new Color(66, 103, 178));
			certificate.addToTable(tableReport, cell, "TOXICOLOGY", fontTitle, Element.ALIGN_LEFT);
			cell.setBackgroundColor(null);

			certificate.addToTable(tableReport, cell, "Drug Test", certificate.getFontValue(), Element.ALIGN_LEFT);

			for (QisTransactionLaboratoryInfo toLab : toLabRequests) {
				QisTransactionToxicology qisTransactionToxicology = new QisTransactionToxicology();
				QisTransactionLabToxicology labToxicology = toLab.getToxicology();

				qisTransactionToxicology.setToxicology(labToxicology);

				qisTransactionToxicology.setItemDetails(toLab.getItemDetails());
				toCertificate.formatToxicologyCertificate(qisTransactionToxicology, tableReport, certificate, cell,
						cellBorder);
			}
		}

		document.add(tableReport);

	}

	public void formatIndustrialClassification(Document document, PdfWriter pdfWriter, QisTransaction qisTransaction,
			QisTransactionItemLaboratories qisTxnItmLab, boolean withHeaderFooter, Certificate certificate)
			throws BadElementException, IOException {

		Font fontTitle;
		fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(10);
		fontTitle.setColor(Color.WHITE);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		PdfPCell cellBorder = new PdfPCell();
		cellBorder.setBorder(Rectangle.BOTTOM);
		cellBorder.setBorderColor(Color.BLUE);
		cellBorder.setUseAscender(true);
		cellBorder.setUseDescender(true);

		// RESULTS
		PdfPTable tableReport = new PdfPTable(2);
		tableReport.setSpacingBefore(10);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(5);

		cell.setColspan(2);
		cell.setBackgroundColor(new Color(66, 103, 178));
		certificate.addToTable(tableReport, cell, "LABORATORY SCIENCES RESULT", fontTitle, Element.ALIGN_CENTER);
		cell.setBackgroundColor(null);

		boolean isMale = "M".equals(qisTransaction.getPatient().getGender());

		PdfPTable indLeft = new PdfPTable(new float[] { 7, 3, 3, 5 });
		indLeft.setWidthPercentage(100f);
		indLeft.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable indRight = new PdfPTable(new float[] { 6, 5, 3, 3 });
		indRight.setWidthPercentage(100f);
		indRight.setHorizontalAlignment(Element.ALIGN_LEFT);

		// HEMATOLOGY
		Set<QisTransactionLaboratoryInfo> heLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "HE".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (heLabRequests.size() > 0) {
			HematologyCertificate heCertificate = new HematologyCertificate(applicationHeader, applicationFooter,
					appUtility);
			for (QisTransactionLaboratoryInfo heLab : heLabRequests) {
				QisTransactionHematology qisTransactionHematology = new QisTransactionHematology();
				QisLaboratoryHematology labHematology = heLab.getHematology();

				BeanUtils.copyProperties(labHematology, qisTransactionHematology);

				qisTransactionHematology.setOtherNotes(heLab.getOtherNotes());
				qisTransactionHematology.setItemDetails(heLab.getItemDetails());
				heCertificate.formatHematologyClassification(qisTransactionHematology, indLeft, certificate, cell,
						isMale);
			}
		}

		// CHEMISTRY
		Set<QisTransactionLaboratoryInfo> chLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "CH".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (chLabRequests.size() > 0) {
			ChemistryCertificate chCertificate = new ChemistryCertificate(applicationHeader, applicationFooter,
					appUtility);

			for (QisTransactionLaboratoryInfo chLab : chLabRequests) {
				QisTransactionChemistry qisTransactionChemistry = new QisTransactionChemistry();
				QisLaboratoryChemistry labChemistry = chLab.getChemistry();

				BeanUtils.copyProperties(labChemistry, qisTransactionChemistry);

				qisTransactionChemistry.setOtherNotes(chLab.getOtherNotes());
				qisTransactionChemistry.setItemDetails(chLab.getItemDetails());
				chCertificate.formatChemistryClassification(qisTransactionChemistry, indLeft, certificate, cell,
						isMale);
			}
		}

		// SEROLOGY
		Set<QisTransactionLaboratoryInfo> seLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "SE".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (seLabRequests.size() > 0) {
			SerologyCertificate seCertificate = new SerologyCertificate(applicationHeader, applicationFooter,
					appUtility);
			for (QisTransactionLaboratoryInfo seLab : seLabRequests) {
//				QisTransactionSerology qisTransactionSerology = new QisTransactionSerology();
				QisTransactionSerology qisTransactionSerology = appUtility
						.getQisTransactionSerology(seLab.getTransactionid(), seLab.getId());
				QisLaboratorySerology labSerology = seLab.getSerology();

				BeanUtils.copyProperties(labSerology, qisTransactionSerology);

				qisTransactionSerology.setOtherNotes(seLab.getOtherNotes());
				qisTransactionSerology.setItemDetails(seLab.getItemDetails());
				seCertificate.formatSerologyClassification(qisTransactionSerology, indLeft, certificate, cell);
			}
		}

		// TOXICOLOGY
		Set<QisTransactionLaboratoryInfo> toLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "TO".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (toLabRequests.size() > 0) {
			ToxicologyCertificate toCertificate = new ToxicologyCertificate(applicationHeader, applicationFooter,
					appUtility);

			for (QisTransactionLaboratoryInfo toLab : toLabRequests) {
				QisTransactionToxicology qisTransactionToxicology = new QisTransactionToxicology();
				QisTransactionLabToxicology labToxicology = toLab.getToxicology();

				qisTransactionToxicology.setToxicology(labToxicology);

				qisTransactionToxicology.setItemDetails(toLab.getItemDetails());
				toCertificate.formatToxicologyClassification(qisTransactionToxicology, indLeft, certificate, cell);
			}
		}

		// CLINICAL MICROSCOPY
		Set<QisTransactionLaboratoryInfo> cmLabRequests = qisTxnItmLab.getTransactionLabRequests().stream()
				.filter(l -> "CM".equals(l.getItemDetails().getItemLaboratoryProcedure())).collect(Collectors.toSet());
		if (cmLabRequests.size() > 0) {
			ClinicalMicroscopyCertificate cmCertificate = new ClinicalMicroscopyCertificate(applicationHeader,
					applicationFooter, appUtility);

			for (QisTransactionLaboratoryInfo cmLab : cmLabRequests) {
				QisTransactionClinicalMicroscopy qisTransactionClinicalMicroscopy = new QisTransactionClinicalMicroscopy();
				QisLaboratoryClinicalMicroscopy labClinicalMicroscopy = cmLab.getClinicalMicroscopy();
				BeanUtils.copyProperties(labClinicalMicroscopy, qisTransactionClinicalMicroscopy);

				qisTransactionClinicalMicroscopy.setOtherNotes(cmLab.getOtherNotes());
				qisTransactionClinicalMicroscopy.setItemDetails(cmLab.getItemDetails());
				cmCertificate.formatClinicalMicroscopyClassification(qisTransactionClinicalMicroscopy, indLeft,
						indRight, certificate, cell, isMale);
			}
		}

		tableReport.addCell(indLeft);
		tableReport.addCell(indRight);

		document.add(tableReport);
		int addY = 15;
		if ("B".equals(qisTxnItmLab.getClassification())) {
			addY = 0;
		}
		System.out.println(tableReport.getTotalHeight());
		int positionSignature = 0;
			if (tableReport.getTotalHeight() >= 315) {				
				positionSignature = 185;
			} else if (tableReport.getTotalHeight() >= 265 && tableReport.getTotalHeight() <= 314) {				
				positionSignature = 230;
			} else if (tableReport.getTotalHeight() >= 189 && tableReport.getTotalHeight() <= 264) {
				positionSignature = 310;
			} else if (tableReport.getTotalHeight() >= 115 && tableReport.getTotalHeight() <= 185) {
				positionSignature = 350;
			}else {
				positionSignature = 400;
			}

		QisUserPersonel labPersonel = qisTxnItmLab.getLabPersonel();
		if (labPersonel != null && labPersonel.getProfile() != null
				&& labPersonel.getProfile().getSignature() != null) {
			try {
				Image labSignature = Image.getInstance(labPersonel.getProfile().getSignature());
				labSignature.setAlignment(Element.ALIGN_CENTER);
				labSignature.setAbsolutePosition(35, positionSignature + addY);
				labSignature.scalePercent(13f);
				pdfWriter.getDirectContentUnder().addImage(labSignature, true);
			} catch (BadElementException | IOException e) {
				e.printStackTrace();
			}
		}

		QisUserPersonel qualityControl = qisTxnItmLab.getClassifiedBy();
		if (qualityControl != null && qualityControl.getProfile() != null
				&& qualityControl.getProfile().getSignature() != null) {
			try {
				Image qcSignature = Image.getInstance(qualityControl.getProfile().getSignature());
				qcSignature.setAlignment(Element.ALIGN_CENTER);
				qcSignature.setAbsolutePosition(225, positionSignature + addY);
				qcSignature.scalePercent(13f);
				pdfWriter.getDirectContentUnder().addImage(qcSignature, true);
			} catch (BadElementException | IOException e) {
				e.printStackTrace();
			}
		}

		QisDoctor doctor = qisTxnItmLab.getMedicalDoctor();
		if (doctor != null && doctor.getSignature() != null) {
			try {
				Image doctorSignature = Image.getInstance(doctor.getSignature());
				doctorSignature.setAlignment(Element.ALIGN_CENTER);
				doctorSignature.setAbsolutePosition(410, positionSignature + addY);
				doctorSignature.scalePercent(13f);
				pdfWriter.getDirectContentUnder().addImage(doctorSignature, true);
			} catch (BadElementException | IOException e) {
				e.printStackTrace();
			}
		}

		QisDoctor classifyDoctor = qisTxnItmLab.getClassifyDoctor();
		if (classifyDoctor != null && classifyDoctor.getSignature() != null) {
			try {
				Image classifyDoctorSignature = Image.getInstance(classifyDoctor.getSignature());
				classifyDoctorSignature.setAlignment(Element.ALIGN_CENTER);
				classifyDoctorSignature.setAbsolutePosition(410, 575 + addY);
				classifyDoctorSignature.scalePercent(13f);
				pdfWriter.getDirectContentUnder().addImage(classifyDoctorSignature, true);
			} catch (BadElementException | IOException e) {
				e.printStackTrace();
			}
		}

		// SIGNATURES
		document.add(certificate.certificateSinature(true, labPersonel, qualityControl, doctor, classifyDoctor,
				"Pathologist", 5));
	}
}
