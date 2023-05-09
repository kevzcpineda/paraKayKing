package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisLaboratoryPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionPhysicalExamination;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionXRay;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.xray.QisTransactionLabXRay;

public class ClassificationCertificate {
	private String applicationName;
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;

	public ClassificationCertificate(String applicationName, String applicationHeader, String applicationFooter,
			AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getTransactionClassificationCertificate(Document document, PdfWriter pdfWriter,
			QisTransaction qisTransaction, QisTransactionItemLaboratories qisTxnItemLab, boolean withHeaderFooter)
			throws DocumentException, IOException {
		document.open();

		Certificate certificate = new Certificate(appUtility);

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
		Set<QisTransactionLaboratoryInfo> peLabRequests = qisTxnItemLab.getTransactionLabRequests().stream()
				.filter(l -> "PE".equals(l.getItemDetails().getItemLaboratory())).collect(Collectors.toSet());
		if (peLabRequests.size() > 0) {
			PhysicalExamCertificate peCertificate = new PhysicalExamCertificate(applicationName, applicationHeader,
					applicationFooter, appUtility);

			for (QisTransactionLaboratoryInfo peLab : peLabRequests) {
				if (peLab.getPhysicalExamination() != null
						&& (peLab.getPhysicalExamination().getMedicalHistory() != null
								|| peLab.getPhysicalExamination().getVitalSign() != null
								|| peLab.getPhysicalExamination().getPhysicalExam() != null)) {

					// HEADER IMAGE
					Image imgHeader = Image.getInstance("src/main/resources/images/" + applicationHeader);
					if (!withHeaderFooter) {
						imgHeader = null;
					}
					document.add(certificate.certificateHeader(imgHeader));

					QisTransactionPhysicalExamination qisTransactionPhysicalExamination = new QisTransactionPhysicalExamination();
					QisLaboratoryPhysicalExamination labPhysicalExamination = peLab.getPhysicalExamination();
					BeanUtils.copyProperties(labPhysicalExamination, qisTransactionPhysicalExamination);

					qisTransactionPhysicalExamination.setItemDetails(peLab.getItemDetails());
					peCertificate.formatPhysicalExamination(document, qisTransaction, qisTransactionPhysicalExamination,
							withHeaderFooter, certificate);

					Image imgFooter = Image.getInstance("src/main/resources/images/" + applicationFooter);
					if (!withHeaderFooter) {
						imgFooter = null;
					}
					document.add(certificate.certificateFooter(imgFooter, 100));

					hasPE = true;
				}
			}
		}

		if (hasPE) {
			document.newPage();
		}

		// MEDICAL CERTIFICATE
		// HEADER IMAGE
		Image imgHeader = Image.getInstance("src/main/resources/images/" + applicationHeader);
		if (!withHeaderFooter) {
			imgHeader = null;
		}
		document.add(certificate.certificateHeader(imgHeader));

		MedicalCertificate medCertificate = new MedicalCertificate(applicationName, applicationHeader,
				applicationFooter, appUtility);
		medCertificate.formatMedicalCertificateClassification(document, qisTransaction, qisTxnItemLab, certificate,
				cell, cellBorder);

		IndustrialCertificate indCertificate = new IndustrialCertificate(applicationHeader, applicationFooter,
				appUtility);
		indCertificate.formatIndustrialClassification(document, pdfWriter, qisTransaction, qisTxnItemLab,
				withHeaderFooter, certificate);

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

		Image imgFooter = Image.getInstance("src/main/resources/images/" + applicationFooter);
		if (!withHeaderFooter) {
			imgFooter = null;
		}
		document.add(certificate.certificateFooter(imgFooter, 10));

		document.close();
		return document;

	}
}
