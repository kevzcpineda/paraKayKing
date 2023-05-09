package quest.phil.diagnostic.information.system.ws.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quest.phil.diagnostic.information.system.ws.common.AppUtility;
import quest.phil.diagnostic.information.system.ws.model.QisBankType;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.entity.QisBranch;
import quest.phil.diagnostic.information.system.ws.model.request.QisBranchRequest;
import quest.phil.diagnostic.information.system.ws.model.response.QisBranchResponse;
import quest.phil.diagnostic.information.system.ws.model.response.QisOptionResponse;
import quest.phil.diagnostic.information.system.ws.repository.QisBranchRepository;
import quest.phil.diagnostic.information.system.ws.service.QisLogService;

@RestController
@RequestMapping("/api/v1/")
public class QisBranchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisDoctorController.class);
	private final String CATEGORY = "BRANCH";

	@Autowired
	private QisBranchRepository qisBranchRepository;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private QisLogService qisLogService;

	// CREATE BRANCH
	@PostMapping("branch")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisBranch createBranch(@Valid @RequestBody QisBranchRequest branchRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Create Branch");

		String branchId = appUtility.generateUserId(6);
		if (qisBranchRepository.existsByBranchCode(branchRequest.getBranchCode()))
			throw new RuntimeException("Duplicate branch code.", new Throwable("branchCode: Duplicate branch code."));
		if (qisBranchRepository.existsByBranchid(branchId))
			throw new RuntimeException("Duplicate branch id.", new Throwable("branchid: Duplicate branch id."));

		QisBranch qisBranch = new QisBranch();
		BeanUtils.copyProperties(branchRequest, qisBranch);

		if (branchRequest.isActive() == null) {
			qisBranch.setActive(true);
		} else {
			qisBranch.setActive(branchRequest.isActive());
		}

		qisBranch.setBranchid(branchId);
		qisBranch.setCreatedBy(authUser.getId());
		qisBranch.setUpdatedBy(authUser.getId());

		QisBranchResponse qisBranchResponse = new QisBranchResponse();
		BeanUtils.copyProperties(qisBranch, qisBranchResponse);
		qisBranchResponse.setActive(qisBranch.isActive());

		qisBranchRepository.save(qisBranch);

		qisLogService.info(authUser.getId(), QisBranchController.class.getSimpleName(), "CREATE",
				qisBranchResponse.toString(), qisBranch.getId(), CATEGORY);

		return qisBranch;
	}

	// READ BRANCH
	@GetMapping("branch/{branchId}")
	public QisBranch getDoctor(@PathVariable String branchId)
			throws Exception {
//		LOGGER.info(authUser.getId() + "-View Branch:" + branchId);
		QisBranch qisBranch = qisBranchRepository.findByBranchid(branchId);
		if (qisBranch == null) {
			throw new RuntimeException("Record not found.", new Throwable("branchCode: Record not found."));
		}

		return qisBranch;
	}

	// UPDATE BRANCH
	@PutMapping("branch/{branchId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
	public QisBranch updateBranch(@PathVariable String branchId, @Valid @RequestBody QisBranchRequest branchRequest,
			@AuthenticationPrincipal QisUserDetails authUser) throws Exception {
		LOGGER.info(authUser.getId() + "-Update Branch");

		QisBranch qisBranch = qisBranchRepository.findByBranchid(branchId);
		if (qisBranch == null) {
			throw new RuntimeException("Record not found.", new Throwable("branchCode: Record not found."));
		}

		if (qisBranchRepository.findBranchCodeOnOtherBranch(branchRequest.getBranchCode(), qisBranch.getId()) != null) {
			throw new RuntimeException("Duplicate branch code.", new Throwable("branchCode: Duplicate branch code."));
		}

		boolean isUpdate = false;
		String updateData = "";
		if (!qisBranch.getBranchCode().equals(branchRequest.getBranchCode())) {
			updateData = appUtility.formatUpdateData(updateData, "Branch Code", qisBranch.getBranchCode(),
					branchRequest.getBranchCode());
			isUpdate = true;
			qisBranch.setBranchCode(branchRequest.getBranchCode());
		}

		if (!qisBranch.getBranchName().equals(branchRequest.getBranchName())) {
			updateData = appUtility.formatUpdateData(updateData, "Branch Name", qisBranch.getBranchName(),
					branchRequest.getBranchName());
			isUpdate = true;
			qisBranch.setBranchName(branchRequest.getBranchName());
		}

		if (branchRequest.getAddress() != null) {
			if (!qisBranch.getAddress().equals(branchRequest.getAddress())) {
				updateData = appUtility.formatUpdateData(updateData, "Address", qisBranch.getAddress(),
						branchRequest.getAddress());
				isUpdate = true;
				qisBranch.setAddress(branchRequest.getAddress());
			}
		} else {
			if (qisBranch.getAddress() != null) {
				updateData = appUtility.formatUpdateData(updateData, "Address", qisBranch.getBranchName(), null);
				isUpdate = true;
				qisBranch.setBranchName(null);

			}
		}

		if (branchRequest.getContactNumber() != null) {
			if (!qisBranch.getAddress().equals(branchRequest.getContactNumber())) {
				updateData = appUtility.formatUpdateData(updateData, "Contact", qisBranch.getContactNumber(),
						branchRequest.getContactNumber());
				isUpdate = true;
				qisBranch.setContactNumber(branchRequest.getContactNumber());
			}
		} else {
			if (qisBranch.getContactNumber() != null) {
				updateData = appUtility.formatUpdateData(updateData, "Contact", qisBranch.getContactNumber(), null);
				isUpdate = true;
				qisBranch.setContactNumber(null);

			}
		}

		if (branchRequest.isActive() != null && qisBranch.isActive() != branchRequest.isActive()) {
			updateData = appUtility.formatUpdateData(updateData, "Active", String.valueOf(qisBranch.isActive()),
					String.valueOf(branchRequest.isActive()));
			isUpdate = true;
			qisBranch.setActive(branchRequest.isActive());
		}

		if (isUpdate) {
			qisBranch.setUpdatedBy(authUser.getId());
			qisBranch.setUpdatedAt(Calendar.getInstance());
			qisBranchRepository.save(qisBranch);
			qisLogService.warn(authUser.getId(), QisBranchController.class.getSimpleName(), "UPDATE", updateData,
					qisBranch.getId(), CATEGORY);
		}

		return qisBranch;
	}
	
	// DELETE BRANCE
	@DeleteMapping("branch/{branchId}")
	public String deleteBranch(@PathVariable String branchId, @AuthenticationPrincipal QisUserDetails authUser) {
		LOGGER.warn(authUser.getId() + "-Delete Branch:" + branchId);
		return "not implemented";
	}

	// LIST ALL BRANCH
	@GetMapping("branches")
	public List<QisBranch> getAllBranches() {
		return qisBranchRepository.findAll();
	}
	
	// LIST ALL PAYMENT BANKS
	@GetMapping("payment_banks")
	public List<QisOptionResponse> getAllBankTypes() {
		Map<String, String> pros = QisBankType.getBankTypes();

		List<QisOptionResponse> list = new ArrayList<QisOptionResponse>();
		for (Map.Entry<String, String> entry : pros.entrySet()) {
			list.add(new QisOptionResponse(entry.getKey(), entry.getValue()));
		}

		return list;
	}
}
