package quest.phil.diagnostic.information.system.ws.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisTransactionInfo;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.QisTransactionXRay;
import quest.phil.diagnostic.information.system.ws.model.entity.laboratory.ecg.QisTransactionLabEcg;
import quest.phil.diagnostic.information.system.ws.model.request.laboratory.QisTransactionLabEcgRequest;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionInfoRepository;
import quest.phil.diagnostic.information.system.ws.repository.QisTransactionXRayRepository;
import quest.phil.diagnostic.information.system.ws.repository.ecg.QisTransactionLabEcgRepository;

@RestController
@RequestMapping("/api/v1/transaction/{transactionId}/laboratory/{laboratoryId}")
public class QisTransactionLaboratoryEcgController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisTransactionLaboratoryEcgController.class);
	private final String CATEGORY = "ECG";

	@Autowired
	private QisTransactionLabEcgRepository qisTransactionLabEcgRepository;

	@Autowired
	private QisTransactionInfoRepository qisTransactionInfoRepository;

	@Autowired
	private QisTransactionXRayRepository qisTransactionXRayRepository;
	
	// Create Ecg
	@PostMapping("/ecg")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
	public ResponseEntity<String> saveTransactionLaboratoryEcg(@PathVariable String transactionId,
			@PathVariable Long laboratoryId, @Valid @RequestBody QisTransactionLabEcgRequest ecgRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {

		LOGGER.info(authUser.getId() + "-Save ECG:" + transactionId);
		QisTransactionInfo qisTransaction = qisTransactionInfoRepository.findByTransactionid(transactionId);
		if (qisTransaction == null) {
			throw new RuntimeException("Transaction not found.");
		}
		String message = "";
		QisTransactionXRay qisTransactionXRay = qisTransactionXRayRepository
				.getTransactionLaboratoryRequestById(qisTransaction.getId(), laboratoryId);
		QisTransactionLabEcg qisTransactionEcg = qisTransactionLabEcgRepository.getTransactionLabReqId(laboratoryId);
		QisTransactionLabEcg ecgNew = new QisTransactionLabEcg();
		if (qisTransactionEcg == null) {
			ecgNew.setId(laboratoryId);
			ecgNew.setCreatedBy(authUser.getId());
			ecgNew.setUpdatedBy(authUser.getId());
			
			if(ecgRequest.getRhythm() != null || !"".equals(ecgRequest.getRhythm())) {				
				ecgNew.setRhythm(ecgRequest.getRhythm());
			}else {
				ecgNew.setRhythm(null);
			}
			if(ecgRequest.getVentricular() != null || !"".equals(ecgRequest.getVentricular())) {
				ecgNew.setVentricular(ecgRequest.getVentricular());
			}
			if(ecgRequest.getPr_interval() != null || !"".equals(ecgRequest.getPr_interval())) {
				ecgNew.setPr_interval(ecgRequest.getPr_interval());
			}
			if(ecgRequest.getVentricular() != null) {
				ecgNew.setRate_atrial(ecgRequest.getVentricular());
			}
			if(ecgRequest.getAxis() != null) {
				ecgNew.setAxis(ecgRequest.getAxis());
			}
			if(ecgRequest.getP_wave() != null) {
				ecgNew.setP_wave(ecgRequest.getP_wave());
			}
			qisTransactionXRay.setStatus(2);
			
			if(ecgRequest.getInterpretation() != null) {
				ecgNew.setInterpretation(ecgRequest.getInterpretation());
			}
			message = "Added Successfully";
			qisTransactionXRayRepository.save(qisTransactionXRay);
			qisTransactionLabEcgRepository.save(ecgNew);
		} else {

			qisTransactionEcg.setRhythm(ecgRequest.getRhythm());

			qisTransactionEcg.setVentricular(ecgRequest.getVentricular());
			qisTransactionEcg.setPr_interval(ecgRequest.getPr_interval());
			qisTransactionEcg.setRate_atrial(ecgRequest.getVentricular());
			qisTransactionEcg.setAxis(ecgRequest.getAxis());
			qisTransactionEcg.setP_wave(ecgRequest.getP_wave());
			qisTransactionEcg.setInterpretation(ecgRequest.getInterpretation());
			qisTransactionEcg.setUpdatedBy(authUser.getId());
			message = "Updated Successfully";
			qisTransactionLabEcgRepository.save(qisTransactionEcg);
//			qisTransactionXRay.setStatus(2);
//			qisTransactionXRayRepository.save(qisTransactionXRay);
		}
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	public ResponseEntity<String> printTransactionEcg() {
		
		 return new ResponseEntity<>(HttpStatus.OK);
		
	}
}
