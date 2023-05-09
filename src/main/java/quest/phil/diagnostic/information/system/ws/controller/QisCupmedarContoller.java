package quest.phil.diagnostic.information.system.ws.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import quest.phil.diagnostic.information.system.ws.model.QisCupmedarExcel;
import quest.phil.diagnostic.information.system.ws.model.classes.QisUserDetails;
import quest.phil.diagnostic.information.system.ws.model.request.QisCupmedarRequest;


@RestController
@RequestMapping("/api/v1/transaction")

public class QisCupmedarContoller {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisCupmedarContoller.class);
	private final String CATEGORY = "TRANSACTION_GENERATE_EXCEL_FILE";
	

	
	@GetMapping("/reports/cupmedar")
	public void downloadCsv(HttpServletResponse response,@AuthenticationPrincipal QisUserDetails authUser) throws IOException {
		response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=cupmedar.xlsx");
        ByteArrayInputStream stream = QisCupmedarRequest.contactListToExcelFile(createTestData());
        IOUtils.copy(stream, response.getOutputStream());
	}
	
	private List<QisCupmedarExcel> createTestData(){
		List<QisCupmedarExcel> qisCupmedarExcel = new ArrayList<QisCupmedarExcel>();
		qisCupmedarExcel.add(new QisCupmedarExcel("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"));
		qisCupmedarExcel.add(new QisCupmedarExcel("2/6/2021 10:09 am", "2", "Natnat, Marvin D", "Quest Staff", "Basic 5", "called", "upload", "printed", "emailed", "encoded", "done", "acknowledge", "recieved", "Send Out", "", ""));
		qisCupmedarExcel.add(new QisCupmedarExcel("2/6/2021 10:10 am", "3", "Jummel", "Quest Staff", "Basic 5", "c", "u", "p", "m", "e", "d", "a", "r", "", "fit to work", ""));
		qisCupmedarExcel.add(new QisCupmedarExcel("2/6/2021 10:11 am", "4", "vhino", "Quest Staff", "Basic 5", "c", "u", "p", "m", "e", "d", "a", "r", "", "", "For pick up"));
		qisCupmedarExcel.add(new QisCupmedarExcel("2/6/2021 10:12 am", "5", "aaron", "Quest Staff", "Basic 5", "c", "u", "p", "m", "e", "d", "a", "r", "", "", ""));
	
		return qisCupmedarExcel;
    	
    }

	
}
