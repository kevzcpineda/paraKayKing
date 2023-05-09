package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.ss.util.CellAddress;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.entity.QisDoctor;
import quest.phil.diagnostic.information.system.ws.model.entity.QisPatient;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisSOA;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;

public class QisHeaderFooterEvent extends PdfPageEventHelper {
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;
	private Certificate certificate;
	private QisTransaction qisTransaction;
	private QisUserPersonel labPersonel = null;
	private QisUserPersonel qualityControl = null;
	private QisDoctor doctor = null;
	private String doctorType;
	private Calendar dateReported;
	private boolean withHeaderFooter;
	private boolean withTableHeader;
	private String labRequest;
	private List<QisQualityTransaction> mainList;
	private ArrayList<QisQualityTransaction> txnListNoDuplicate;

	public QisHeaderFooterEvent(String applicationHeader, String applicationFooter, AppUtility appUtility,
			QisTransaction qisTransaction, QisUserPersonel labPersonel, QisUserPersonel qualityControl,
			QisDoctor doctor, String doctorType, Calendar dateReported, boolean withHeaderFooter,
			boolean withTableHeader, String labRequest, List<QisQualityTransaction> mainList) {
		super();
		this.txnListNoDuplicate = new ArrayList<>();
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
		this.qisTransaction = qisTransaction;
		this.labPersonel = labPersonel;
		this.qualityControl = qualityControl;
		this.doctor = doctor;
		this.doctorType = doctorType;
		this.dateReported = dateReported;
		this.withHeaderFooter = withHeaderFooter;
		this.withTableHeader = withTableHeader;
		this.certificate = new Certificate(appUtility);
		this.labRequest = labRequest;
		this.mainList = mainList;
	}

	public void onStartPage(PdfWriter writer, Document document) {
		try {
			Image imgHeader = null;
			if ("COVID".equals(labRequest) || "ANTIGEN".equals(labRequest)) {
				imgHeader = Image.getInstance("src/main/resources/images/HeaderDocFroi.png");
			} else {
				if (withHeaderFooter) {
					imgHeader = Image.getInstance("src/main/resources/images/" + applicationHeader);
				}
			}
			document.add(certificate.certificateHeader(imgHeader));
			// PATIENT DETAILS
			if ("certAntigen".equals(labRequest) || "certAntibody".equals(labRequest)) {
			} else {
				document.add(certificatePatientInformation(qisTransaction, dateReported, document));
			}

			if (withTableHeader) {
				// TABLE RESULTS
				if ("cumulative".equals(labRequest)) {
					document.add(resultsTableHeaderCumulative());
				} else {
					document.add(resultsTableHeader());
				}
			}

		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
	}

	public void onEndPage(PdfWriter writer, Document document) {
		try {
			Image imgFooter = null;
			if (withHeaderFooter && !"ANTIGEN".equals(labRequest)) {
				imgFooter = Image.getInstance("src/main/resources/images/" + applicationFooter);
			}
			if (doctor.getId() != 1) {
				if (labPersonel != null && labPersonel.getProfile() != null
						&& labPersonel.getProfile().getSignature() != null) {
					try {
						Image labSignature = Image.getInstance(labPersonel.getProfile().getSignature());
						labSignature.setAlignment(Element.ALIGN_CENTER);
						labSignature.setAbsolutePosition(35, 60);
						labSignature.scalePercent(13f);
						writer.getDirectContent().addImage(labSignature, true);
					} catch (BadElementException | IOException e) {
						e.printStackTrace();
					}
				}

				if (qualityControl != null && qualityControl.getProfile() != null
						&& qualityControl.getProfile().getSignature() != null) {
					try {
						Image qcSignature = Image.getInstance(qualityControl.getProfile().getSignature());
						qcSignature.setAlignment(Element.ALIGN_CENTER);
						qcSignature.setAbsolutePosition(225, 60);
						qcSignature.scalePercent(13f);
						writer.getDirectContent().addImage(qcSignature, true);
					} catch (BadElementException | IOException e) {
						e.printStackTrace();
					}
				}
			}

			if (doctor != null && doctor.getSignature() != null) {
				try {
					Image doctorSignature = Image.getInstance(doctor.getSignature());
					doctorSignature.setAlignment(Element.ALIGN_CENTER);
					doctorSignature.setAbsolutePosition(410, 60);
					doctorSignature.scalePercent(13f);
					writer.getDirectContentUnder().addImage(doctorSignature, true);
				} catch (BadElementException | IOException e) {
					e.printStackTrace();
				}
			}

			PdfPTable tableFooter = certificateSinature(true, labPersonel, qualityControl, doctor, doctorType);
			PdfPCell cellFooter = new PdfPCell();
			cellFooter.setColspan(7);
			cellFooter.setBorder(Rectangle.NO_BORDER);
			cellFooter.addElement(imgFooter);
			cellFooter.setFixedHeight(50);
			cellFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableFooter.addCell(cellFooter);
			tableFooter.setTotalWidth(600);
			tableFooter.writeSelectedRows(0, -1, 7, 135, writer.getDirectContent());
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
	}

	public PdfPTable certificatePatientInformation(QisTransaction qisTransaction, Calendar verifiedDate,
			Document document) {
		PdfPTable tablePatient = new PdfPTable(new float[] { 1, 2, 1, 2, 1, 2 });
		tablePatient.setSpacingBefore(0);
		tablePatient.setWidthPercentage(90f);
		tablePatient.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(Color.BLACK);

		QisPatient patient = qisTransaction.getPatient();
		certificate.addToTable(tablePatient, cell, "Name:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tablePatient, cell, appUtility.getPatientFullname(patient), certificate.getFontValue(),
				Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tablePatient, cell, "SR#:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePatient, cell, appUtility.numberFormat(qisTransaction.getId(), "000000"),
				certificate.getFontValue(), Element.ALIGN_LEFT);

		String gender = "MALE";
		if ("F".equals(patient.getGender())) {
			gender = "FEMALE";
		}
		String age = String
				.valueOf(appUtility.calculateAgeInYear(patient.getDateOfBirth(), qisTransaction.getTransactionDate()));

		certificate.addToTable(tablePatient, cell, "Age/Gender:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);
		certificate.addToTable(tablePatient, cell, age + "/" + gender, certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tablePatient, cell, "QuestID:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePatient, cell, appUtility.numberFormat(patient.getId(), "0000"),
				certificate.getFontValue(), Element.ALIGN_LEFT);

		certificate.addToTable(tablePatient, cell, "Transaction:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		cell.setColspan(3);

		String company = "";
		if (qisTransaction.getBiller() != null) {
			company = " (" + qisTransaction.getBiller() + ")";
		} else {
			if (qisTransaction.getPatient().getCorporate() != null) {
				company = " (" + qisTransaction.getPatient().getCorporate().getCompanyName() + ")";
			}
		}
		certificate.addToTable(tablePatient, cell,
				appUtility.getTransactionType(qisTransaction.getTransactionType()) + company,
				certificate.getFontValue(), Element.ALIGN_LEFT);
		cell.setColspan(1);
		certificate.addToTable(tablePatient, cell, "Dispatch:", certificate.getFontLabel(), Element.ALIGN_LEFT);
		certificate.addToTable(tablePatient, cell, appUtility.getDispatchType(qisTransaction.getDispatch()),
				certificate.getFontValue(), Element.ALIGN_LEFT);

		if (qisTransaction.getReferral() != null) {
			certificate.addToTable(tablePatient, cell, "Clinician:", certificate.getFontLabel(), Element.ALIGN_LEFT);
			cell.setColspan(3);
			certificate.addToTable(tablePatient, cell, qisTransaction.getReferral().getReferral(),
					certificate.getFontValue(), Element.ALIGN_LEFT);
			cell.setColspan(1);
			certificate.addToTable(tablePatient, cell, "", certificate.getFontLabel(), Element.ALIGN_LEFT);
			certificate.addToTable(tablePatient, cell, "", certificate.getFontValue(), Element.ALIGN_LEFT);
//			certificate.addToTable(tablePatient, cell, "Page:", certificate.getFontLabel(), Element.ALIGN_LEFT);
//			certificate.addToTable(tablePatient, cell, document.getPageNumber() + "", certificate.getFontValue(),
//					Element.ALIGN_LEFT);			
		}

		certificate.addToTable(tablePatient, cell, "Received:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tablePatient, cell,
				appUtility.calendarToFormatedDate(qisTransaction.getTransactionDate(), "MMM-dd-yyyy hh:mm:ss"),
				certificate.getFontDocValue(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tablePatient, cell, "Reported:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		String reportedDate = "";
		if (verifiedDate != null) {
			reportedDate = appUtility.calendarToFormatedDate(verifiedDate, "MMM-dd-yyyy hh:mm:ss");
		}
		certificate.addToTable(tablePatient, cell, reportedDate, certificate.getFontDocValue(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		certificate.addToTable(tablePatient, cell, "Printed:", certificate.getFontDocLabel(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tablePatient, cell,
				appUtility.calendarToFormatedDate(Calendar.getInstance(), "MMM-dd-yyyy hh:mm:ss"),
				certificate.getFontDocValue(), Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);

		return tablePatient;
	}

	public PdfPTable resultsTableHeaderCumulative() {
		PdfPTable tableResults = new PdfPTable(9);
		tableResults.setSpacingBefore(5);
		tableResults.setWidthPercentage(90f);
		tableResults.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell cell = new PdfPCell();

		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(3);
		cell.setRowspan(2);
		certificate.addToTable(tableResults, cell, "TEST", certificate.getFontSubTitle(), Element.ALIGN_CENTER);

		cell.setRowspan(2);
		cell.setColspan(1);
		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		certificate.addToTable(tableResults, cell, "SI UNIT", certificate.getFontSubTitle(), Element.ALIGN_RIGHT);

		for (QisQualityTransaction i : mainList) {
			if (!txnListNoDuplicate.contains(i)) {
				txnListNoDuplicate.add(i);
			}
		}

		int num = 4;
		for (QisQualityTransaction list : txnListNoDuplicate) {
			num++;
			if (num <= 9) {
				cell.setCellEvent(null);
				cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
				certificate.addToTable(tableResults, cell,
						appUtility.calendarToFormatedDate(list.getTransactionDate(), "MM-DD-yyyy hh:mm a"),
						certificate.getFontSubsubTitleXray(), Element.ALIGN_RIGHT);
			}
		}
		for (int i = num; i <= 9; i++ ) {
			cell.setCellEvent(null);
			cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
			certificate.addToTable(tableResults, cell, "", certificate.getFontSubTitle(), Element.ALIGN_RIGHT);
		}

		
		
		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));

		return tableResults;
	}

	public PdfPTable resultsTableHeader() {
		PdfPTable tableResults = new PdfPTable(9);
		tableResults.setSpacingBefore(5);
		tableResults.setWidthPercentage(90f);
		tableResults.setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell cell = new PdfPCell();

		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		cell.setBorderColor(Color.BLACK);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(3);
		cell.setRowspan(2);
		certificate.addToTable(tableResults, cell, "TEST", certificate.getFontSubTitle(), Element.ALIGN_CENTER);

		cell.setRowspan(2);
		cell.setColspan(1);
		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		certificate.addToTable(tableResults, cell, "Result", certificate.getFontSubTitle(), Element.ALIGN_RIGHT);

		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		certificate.addToTable(tableResults, cell, "(SI Unit)", certificate.getFontValue(), Element.ALIGN_RIGHT);

		cell.setRowspan(2);
		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		certificate.addToTable(tableResults, cell, "Reference Range", certificate.getFontSubTitle(),
				Element.ALIGN_RIGHT);

		cell.setRowspan(2);
		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		certificate.addToTable(tableResults, cell, "Result", certificate.getFontSubTitle(), Element.ALIGN_RIGHT);

		cell.setRowspan(2);
		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		certificate.addToTable(tableResults, cell, "(Conv. Unit)", certificate.getFontValue(), Element.ALIGN_RIGHT);

		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));
		certificate.addToTable(tableResults, cell, "Reference Range", certificate.getFontSubTitle(),
				Element.ALIGN_RIGHT);

		cell.setCellEvent(null);
		cell.setCellEvent(new DottedCell(PdfPCell.TOP | PdfPCell.BOTTOM));

		return tableResults;
	}

	public PdfPTable certificateSinature(boolean withNotication, QisUserPersonel labPersonel,
			QisUserPersonel qualityControl, QisDoctor doctor, String doctorType) {
		PdfPTable tableSignature = new PdfPTable(new float[] { 1, 6, 1, 6, 1, 6, 1 });
		tableSignature.setSpacingBefore(0);
		tableSignature.setWidthPercentage(95f);
		tableSignature.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_TOP);
		cell.setBorderColor(Color.BLACK);

		boolean notification = withNotication;
		if (withTableHeader) {
			cell.setFixedHeight(20);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			cell.setCellEvent(new DottedCell(PdfPCell.BOTTOM));
			cell.setColspan(3);
			if ("ultrasound".equals(labRequest)) {
				certificate.addToTable(tableSignature, cell, "", certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(tableSignature, cell, "Note: Specimen rechecked, result/s verified.",
						certificate.getFontDocNormal(), Element.ALIGN_LEFT);
			}

			cell.setColspan(2);
			String up = certificate.getUPARROW() + " - High";
			String down = certificate.getDOWNARROW() + " - Low";
			certificate.addToTable(tableSignature, cell, "LEGEND : " + up + "       " + down,
					certificate.getFontResult(), Element.ALIGN_RIGHT);
			notification = false;
			cell.setCellEvent(null);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			cell.setFixedHeight(0);
		}

		if (notification) {
			cell.setColspan(7);
			if ("ultrasound".equals(labRequest)) {
				certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
			} else {
				certificate.addToTable(tableSignature, cell, "                Note: Specimen rechecked, result/s verified.",
						certificate.getFontNotesNormal(), Element.ALIGN_LEFT);
			}
		}

		cell.setColspan(1);
		cell.setFixedHeight(40);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

		if (doctor.getId() != 1) {
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, appUtility.getMedicalTechnologyDisplayName(labPersonel),
					certificate.getFontDocValue(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			if ("xray".equals(labRequest) || "ultrasound".equals(labRequest)) {
				certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(tableSignature, cell, appUtility.getMedicalTechnologyDisplayName(qualityControl),
						certificate.getFontDocValue(), Element.ALIGN_CENTER);
			}

		} else {
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
		}

		certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
		certificate.addToTable(tableSignature, cell, appUtility.getDoctorsDisplayName(doctor),
				certificate.getFontDocValue(), Element.ALIGN_CENTER);

		if (doctor.getId() != 1) {
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			cell.setFixedHeight(0);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);

			if ("ultrasound".equals(labRequest)) {
				cell.setBorder(Rectangle.NO_BORDER);
			} else {
				cell.setBorder(Rectangle.BOTTOM);
			}
			certificate.addToTable(tableSignature, cell, appUtility.getMedicalTechnologyLicenseNo(labPersonel),
					certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			if ("xray".equals(labRequest) || "ultrasound".equals(labRequest)) {
				cell.setBorder(Rectangle.NO_BORDER);
			} else {
				cell.setBorder(Rectangle.BOTTOM);
			}
			certificate.addToTable(tableSignature, cell, appUtility.getMedicalTechnologyLicenseNo(qualityControl),
					certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
		} else {
			certificate.addToTable(tableSignature, cell, "", certificate.getFontDocValue(), Element.ALIGN_CENTER);
			cell.setFixedHeight(0);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
		}
		certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		certificate.addToTable(tableSignature, cell,
				doctor != null && doctor.getLicenseNumber() != null ? "LIC NO. " + doctor.getLicenseNumber() : "",
				certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		if (doctor.getId() != 1) {
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			if ("xray".equals(labRequest)) {
				certificate.addToTable(tableSignature, cell, "Quality Assurance", certificate.getFontNotes(),
						Element.ALIGN_CENTER);
			} else if ("ultrasound".equals(labRequest)) {
				certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(tableSignature, cell, "Registered Medical Technologist",
						certificate.getFontNotes(), Element.ALIGN_CENTER);
			}
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			if ("xray".equals(labRequest)) {
				certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			} else if ("ultrasound".equals(labRequest)) {
				certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			} else {
				certificate.addToTable(tableSignature, cell, "Quality Control", certificate.getFontNotes(),
						Element.ALIGN_CENTER);
			}
		} else {
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotesNormal(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
			certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
		}

		certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);
		certificate.addToTable(tableSignature, cell, doctorType, certificate.getFontNotes(), Element.ALIGN_CENTER);
		certificate.addToTable(tableSignature, cell, "", certificate.getFontNotes(), Element.ALIGN_CENTER);

		return tableSignature;
	}
}

class DottedCell implements PdfPCellEvent {
	private int border = 0;

	public DottedCell(int border) {
		this.border = border;
	}

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
		PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];

		canvas.saveState();
		canvas.setLineDash(3f, 3f);
		if ((border & PdfPCell.TOP) == PdfPCell.TOP) {
			canvas.moveTo(position.getRight(), position.getTop());
			canvas.lineTo(position.getLeft(), position.getTop());
		}
		if ((border & PdfPCell.BOTTOM) == PdfPCell.BOTTOM) {
			canvas.moveTo(position.getRight(), position.getBottom());
			canvas.lineTo(position.getLeft(), position.getBottom());
		}
		if ((border & PdfPCell.RIGHT) == PdfPCell.RIGHT) {
			canvas.moveTo(position.getRight(), position.getTop());
			canvas.lineTo(position.getRight(), position.getBottom());
		}
		if ((border & PdfPCell.LEFT) == PdfPCell.LEFT) {
			canvas.moveTo(position.getLeft(), position.getTop());
			canvas.lineTo(position.getLeft(), position.getBottom());
		}
		canvas.stroke();
		canvas.restoreState();
	}
}

// DOCUMENTATION AND EXAMPLES

//public void onStartPage(PdfWriter writer, Document document) {
//imgHeader.setAlignment(Element.ALIGN_RIGHT);
//imgHeader.setAbsolutePosition(30, 720);
//imgHeader.scalePercent(35f);
//imgHeader.setBackgroundColor(Color.WHITE);		
//writer.getDirectContent().addImage(imgHeader, true);

//ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Left"), 30, 770, 0);
//ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Right"), 550, 770,
//		0);

//   Rectangle rect = pdfWriter.getBoxSize("rectangle");
//   // TOP LEFT
//  ColumnText.showTextAligned(pdfWriter.getDirectContent(),
//           Element.ALIGN_CENTER, new Phrase("TOP LEFT"), rect.getLeft(),
//           rect.getTop(), 0);
//
//  // TOP MEDIUM
//  ColumnText.showTextAligned(pdfWriter.getDirectContent(),
//           Element.ALIGN_CENTER, new Phrase("TOP MEDIUM"),
//           rect.getRight() / 2, rect.getTop(), 0);
//
//  // TOP RIGHT
//  ColumnText.showTextAligned(pdfWriter.getDirectContent(),
//           Element.ALIGN_CENTER, new Phrase("TOP RIGHT"), rect.getRight(),
//           rect.getTop(), 0);

//}

//public void onEndPage(PdfWriter writer, Document document) {
//document.add(tableFooter);

//PdfPTable footerTable = certificate.certificateFooter(imgFooter, 0);
//System.out.println(footerTable.getTotalHeight() + ":" + footerTable.getTotalHeight());
//document.add(footerTable);

//imgFooter.setAlignment(Element.ALIGN_RIGHT);
//imgFooter.setAbsolutePosition(30, 1);
//imgFooter.scalePercent(15f);
//imgFooter.setBackgroundColor(Color.WHITE);
//PdfPTable footerTable = certificate.certificateFooter(imgFooter, 0);
//footerTable.writeSelectedRows(0, -1, 169, footerTable.getTotalHeight() + 18, writer.getDirectContent());
//writer.getDirectContent().addImage(imgFooter, true);

//ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
//new Phrase("http://www.xxxx-your_example.com/"), 110, 30, 0);
//ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
//new Phrase("page " + document.getPageNumber()), 550, 30, 0);

//Rectangle rect = pdfWriter.getBoxSize("rectangle");
//// BOTTOM LEFT
//ColumnText.showTextAligned(pdfWriter.getDirectContent(),
//   Element.ALIGN_CENTER, new Phrase("BOTTOM LEFT"),
//   rect.getLeft()+15, rect.getBottom(), 0);
//
//// BOTTOM MEDIUM
//ColumnText.showTextAligned(pdfWriter.getDirectContent(),
//   Element.ALIGN_CENTER, new Phrase("BOTTOM MEDIUM"),
//   rect.getRight() / 2, rect.getBottom(), 0);
//
//// BOTTOM RIGHT
//ColumnText.showTextAligned(pdfWriter.getDirectContent(),
//   Element.ALIGN_CENTER, new Phrase("BOTTOM RIGHT"),
//   rect.getRight()-10, rect.getBottom(), 0);
//}

//String path = "src/main/resources/garamond";
//File file = new File(path);
//String absolutePath = file.getAbsolutePath();
//
//BaseFont bf = BaseFont.createFont(absolutePath + File.separator + "Garamond.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//Font f = new Font(bf, 12);
//Paragraph p = new Paragraph("\u2193, \u25BC, \u2191, \u25B2", f);
//document.add(p);
