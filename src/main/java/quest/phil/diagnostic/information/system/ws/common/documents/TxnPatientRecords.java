package quest.phil.diagnostic.information.system.ws.common.documents;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import quest.phil.diagnostic.information.system.ws.model.entity.QisLaboratoryProcedureService;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionItem;
import quest.phil.diagnostic.information.system.ws.model.entity.QisQualityTransactionLaboratory;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransaction;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionItemLaboratories;
import quest.phil.diagnostic.information.system.ws.model.entity.QisUserPersonel;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionLaboratoryInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.cm.QisTransactionLabUrineChemical;

public class TxnPatientRecords {

	private String applicationName;
	private String applicationHeader;
	private String applicationFooter;
	private AppUtility appUtility;

	public TxnPatientRecords(String applicationName, String applicationHeader, String applicationFooter,
			AppUtility appUtility) {
		super();
		this.applicationName = applicationName;
		this.applicationHeader = applicationHeader;
		this.applicationFooter = applicationFooter;
		this.appUtility = appUtility;
	}

	public Document getPatientTxnRecords(Document document, PdfWriter pdfWriter, List<QisQualityTransaction> mainList,
			boolean withHeaderFooter) throws DocumentException, IOException {

		Certificate certificate = new Certificate(appUtility);

		QisUserPersonel labPersonel = null;
		QisUserPersonel qualityControl = null;
		QisDoctor doctor = null;
		Calendar dateReported = null;

		QisTransaction qisTransaction = new QisTransaction();
		Set<QisQualityTransactionItem> itm = null;
		for (QisQualityTransaction txn : mainList) {
			itm = txn.getTransactionItems();

			qisTransaction.setBranch(txn.getBranch());
			qisTransaction.setBranchId(txn.getBranchId());
			qisTransaction.setCashier(txn.getCashier());
			qisTransaction.setCashierId(txn.getCashierId());
			qisTransaction.setCreatedAt(txn.getCreatedAt());
			qisTransaction.setCreatedBy(txn.getCreatedBy());
			qisTransaction.setPatient(txn.getPatient());
			qisTransaction.setId(txn.getId());
			qisTransaction.setTransactionDate(txn.getTransactionDate());
			qisTransaction.setBiller(txn.getBiller() != null ? txn.getBiller() : null);
			qisTransaction.setTransactionType(txn.getTransactionType());

		}

		for (QisQualityTransactionItem labItem : itm) {
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

		QisHeaderFooterEvent event = new QisHeaderFooterEvent(applicationHeader, applicationFooter, appUtility,
				qisTransaction, labPersonel, qualityControl, doctor, "Pathologist", dateReported, withHeaderFooter,
				true, "cumulative", mainList);

		pdfWriter.setPageEvent(event);

		document.setMargins(10, 10, 10, 150);
		document.open();

		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.BOX);
		cell.setBorderColor(Color.BLACK);
		cell.setNoWrap(false);
		cell.setFixedHeight(20);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPCell cellBorder = new PdfPCell();
		cellBorder.setBorder(Rectangle.BOTTOM);
		cellBorder.setBorderColor(Color.BLUE);
		cellBorder.setVerticalAlignment(Element.ALIGN_BOTTOM);

		PdfPTable tableReport = new PdfPTable(new float[] { 4, 1, 1, 1, 1, 1 });
		tableReport.setSpacingBefore(20);
		tableReport.setWidthPercentage(90f);
		tableReport.setHorizontalAlignment(Element.ALIGN_CENTER);
		tableReport.getDefaultCell().setBorder(0);
		tableReport.getDefaultCell().setPadding(0);

		Font fontTitle = FontFactory.getFont("GARAMOND_BOLD");
		fontTitle.setSize(11);
		fontTitle.setColor(Color.BLACK);
		

		PdfPTable titleLabel = new PdfPTable(new float[] { 1, 4, 4, 4 });
		titleLabel.setWidthPercentage(100f);
		titleLabel.setSpacingBefore(10);
		titleLabel.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable firstResult = new PdfPTable(1);
		firstResult.setWidthPercentage(100f);
		firstResult.setSpacingBefore(10);
		firstResult.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable secondResult = new PdfPTable(1);
		secondResult.setWidthPercentage(100f);
		secondResult.setSpacingBefore(10);
		secondResult.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable thirdResult = new PdfPTable(1);
		thirdResult.setWidthPercentage(100f);
		thirdResult.setSpacingBefore(10);
		thirdResult.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable fourthResult = new PdfPTable(1);
		fourthResult.setWidthPercentage(100f);
		fourthResult.setSpacingBefore(10);
		fourthResult.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable fifthResult = new PdfPTable(1);
		fifthResult.setWidthPercentage(100f);
		fifthResult.setSpacingBefore(10);
		fifthResult.setHorizontalAlignment(Element.ALIGN_LEFT);
		PdfPTable useTable = null;
		int pdfResult = 1;
		int uchemButnotUrine = 0;
		int startUrine = 0;
		for (QisQualityTransaction txn : mainList) {
			
			int numberUchem = 0;
			System.out.println(pdfResult);
			switch (pdfResult) {
			case 1:
				useTable = firstResult;
				break;
			case 2:
				useTable = secondResult;
				break;
			case 3:
				useTable = thirdResult;
				break;
			case 4:
				useTable = fourthResult;
				break;
			case 5:
				useTable = fifthResult;
				break;
			default:
				break;
			}
			// CLINICAL MICROSCOPY
			Set<QisTransactionLaboratoryInfo> cmLabRequests = null;
			for (QisQualityTransactionItem item : txn.getTransactionItems()) {
				QisTransactionItemLaboratories qisTxnItemLab = appUtility
						.getQisTransactionItemLaboratories(item.getTransactionid(), item.getId());

				cmLabRequests = qisTxnItemLab.getTransactionLabRequests().stream()
						.filter(l -> "CM".equals(l.getItemDetails().getItemLaboratoryProcedure()))
						.collect(Collectors.toSet());
				if (!cmLabRequests.isEmpty()) {
					numberUchem++;
					for (QisTransactionLaboratoryInfo cmLab : cmLabRequests) {
						Set<QisLaboratoryProcedureService> services = cmLab.getItemDetails().getServiceRequest();
						QisLaboratoryProcedureService uchem = services.stream()
								.filter(s -> s.getLaboratoryRequest().toString().equals("UCHEM")).findAny()
								.orElse(null);
						QisTransactionLabUrineChemical urine = cmLab.getClinicalMicroscopy().getUrineChemical();
						if (uchem != null) {
							startUrine++;
							if (startUrine == 1) {
								cell.setColspan(4);
								certificate.addToTable(titleLabel, cell, "CLINICAL MICROSCOPY", fontTitle,
										Element.ALIGN_LEFT);
								certificate.addToTable(titleLabel, cell, "Complete Urinalysis",
										certificate.getFontDocValue(), Element.ALIGN_LEFT);

								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Color", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Transparency", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(4);
								certificate.addToTable(titleLabel, cell, " Microscopic", certificate.getFontDocValue(),
										Element.ALIGN_LEFT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " RBC", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "/hpf 0~3", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " WBC", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "/hpf 0~5", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " E.Cells", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " M.Threads", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Amorphous", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " CaOX", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Bacteria", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(4);
								certificate.addToTable(titleLabel, cell, " Urine Chemical",
										certificate.getFontDocValue(), Element.ALIGN_LEFT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " pH", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Specific Gravity",
										certificate.getFontDocLabel(), Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Protein", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Glucose", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Leukocyte Esterase",
										certificate.getFontDocLabel(), Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Nitrite", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Urobilinogen", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Blood", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Ketone", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(3);
								certificate.addToTable(titleLabel, cell, " Bilirubin", certificate.getFontDocLabel(),
										Element.ALIGN_LEFT);
								cell.setColspan(1);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocLabel(),
										Element.ALIGN_RIGHT);
								cell.setColspan(4);
								certificate.addToTable(titleLabel, cell, "", certificate.getFontDocValue(),
										Element.ALIGN_LEFT);
								tableReport.addCell(titleLabel);

							}
							if (cmLab.getClinicalMicroscopy().getUrineChemical() != null) {
								cell.setColspan(1);
								certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
								certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
								certificate.addToTable(useTable, cell, appUtility.getMacroColor(urine.getColor()),
										certificate.getSfontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell,
										appUtility.getMacroTransparency(urine.getTransparency()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, urine.getRBC(), certificate.getFontDocValue(),
										Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, urine.getWBC(), certificate.getFontDocValue(),
										Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, appUtility.getMicroOptions(urine.geteCells()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, appUtility.getMicroOptions(urine.getmThreads()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, appUtility.getMicroOptions(urine.getAmorphous()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, appUtility.getMicroOptions(urine.getCaOX()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, appUtility.getMicroOptions(urine.getBacteria()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(),
										Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell, String.valueOf(urine.getPh()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell,
										appUtility.floatFormat(urine.getSpGravity(), "0.000"),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell,
										appUtility.getUrineChemOptions(urine.getProtien()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								certificate.addToTable(useTable, cell,
										appUtility.getUrineChemOptions(urine.getGlucose()),
										certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								if (urine.getLeukocyteEsterase() != null) {
									certificate.addToTable(useTable, cell,
											appUtility.getUrineChemOptions(urine.getLeukocyteEsterase()),
											certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								} else {
									certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(),
											Element.ALIGN_RIGHT);
								}
								if (urine.getNitrite() != null) {
									certificate.addToTable(useTable, cell,
											appUtility.getPositiveNegative(urine.getNitrite()),
											certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								} else {
									certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(),
											Element.ALIGN_RIGHT);
								}
								if (urine.getUrobilinogen() != null) {
									certificate.addToTable(useTable, cell,
											appUtility.getUrineChemOptions(urine.getUrobilinogen()),
											certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								} else {
									certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(),
											Element.ALIGN_RIGHT);
								}
								if (urine.getBlood() != null) {
									certificate.addToTable(useTable, cell,
											appUtility.getUrineChemOptions(urine.getBlood()),
											certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								} else {
									certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(),
											Element.ALIGN_RIGHT);
								}
								if (urine.getKetone() != null) {
									certificate.addToTable(useTable, cell,
											appUtility.getUrineChemOptions(urine.getKetone()),
											certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								} else {
									certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(),
											Element.ALIGN_RIGHT);
								}
								if (urine.getBilirubin() != null) {
									certificate.addToTable(useTable, cell,
											appUtility.getUrineChemOptions(urine.getBilirubin()),
											certificate.getFontDocValue(), Element.ALIGN_RIGHT);
								} else {
									certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(),
											Element.ALIGN_RIGHT);
								}
								certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(),
										Element.ALIGN_LEFT);
								
								tableReport.addCell(useTable);
							}
						} else {
							if (startUrine == 0) {
								uchemButnotUrine++;
							}
							if (cmLabRequests.size() == 1) {
								if (startUrine != 0) {
									cell.setColspan(1);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									certificate.addToTable(useTable, cell, "", fontTitle, Element.ALIGN_LEFT);
									tableReport.addCell(useTable);
								}
							}
						}
					}
				}
			}

			if (cmLabRequests.isEmpty()) {
				if (startUrine != 0) {
					if (numberUchem == 0) {
						cell.setColspan(1);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
						tableReport.addCell(useTable);
					}
				}
			}
			
			if (startUrine != 0) {				
				pdfResult++;				
			}
		}
		if (pdfResult != 6) {
			for (int x = pdfResult; x < 7; x++) {
				switch (pdfResult) {
				case 1:
					useTable = firstResult;
					break;
				case 2:
					useTable = secondResult;
					break;
				case 3:
					useTable = thirdResult;
					break;
				case 4:
					useTable = fourthResult;
					break;
				case 5:
					useTable = fifthResult;
					break;
				default:
					break;
				}

				pdfResult++;
				cell.setColspan(1);
				if (6 > pdfResult) {
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
					certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				}
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				certificate.addToTable(useTable, cell, "", certificate.getFontDocValue(), Element.ALIGN_LEFT);
				tableReport.addCell(useTable);
			}
		}

		document.add(tableReport);
		document.close();

		return document;
	}

}
