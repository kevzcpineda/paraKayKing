package quest.phil.diagnostic.information.system.ws.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quest.phil.diagnostic.information.system.ws.model.entity.QisLog;
import quest.phil.diagnostic.information.system.ws.repository.QisLogRepository;

@Service
public class QisLogService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QisLogService.class);
	private HttpServletRequest httpRequest;

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.httpRequest = request;
	}

	@Autowired
	private QisLogRepository qisLogRepository;

	public void log(Long userId, String logger, String level, String action, String message, Long referenceId,
			String reference) {
		try {
			LOGGER.trace(formatMessage(userId, action, reference, message));
			logger(userId, logger, level, action, message, referenceId, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void debug(Long userId, String logger, String action, String message, Long referenceId, String reference) {
		try {
			LOGGER.debug(formatMessage(userId, action, reference, message));
			logger(userId, logger, "DEBUG", action, message, referenceId, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void error(Long userId, String logger, String action, String message, Long referenceId, String reference) {
		try {
			LOGGER.error(formatMessage(userId, action, reference, message));
			logger(userId, logger, "ERROR", action, message, referenceId, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void info(Long userId, String logger, String action, String message, Long referenceId, String reference) {
		try {
			LOGGER.info(formatMessage(userId, action, reference, message));
			logger(userId, logger, "INFO", action, message, referenceId, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void warn(Long userId, String logger, String action, String message, Long referenceId, String reference) {
		try {
			LOGGER.warn(formatMessage(userId, action, reference, message));
			logger(userId, logger, "WARN", action, message, referenceId, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	/*
	 * No Reference
	 */
	public void log(Long userId, String logger, String level, String action, String message, String reference) {
		try {
			LOGGER.trace(formatMessage(userId, action, reference, message));
			logger(userId, logger, level, action, message, null, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void debug(Long userId, String logger, String action, String message, String reference) {
		try {
			LOGGER.debug(formatMessage(userId, action, reference, message));
			logger(userId, logger, "DEBUG", action, message, null, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void error(Long userId, String logger, String action, String message, String reference) {
		try {
			LOGGER.error(formatMessage(userId, action, reference, message));
			logger(userId, logger, "ERROR", action, message, null, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void info(Long userId, String logger, String action, String message, String reference) {
		try {
			LOGGER.info(formatMessage(userId, action, reference, message));
			logger(userId, logger, "INFO", action, message, null, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void warn(Long userId, String logger, String action, String message, String reference) {
		try {
			LOGGER.warn(formatMessage(userId, action, reference, message));
			logger(userId, logger, "WARN", action, message, null, reference);
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	/*
	 * Logger Entity
	 */

	public void logger(Long userId, String logger, String level, String action, String message, Long referenceId,
			String reference) throws Exception {
		String clientSource = getClientSource();
		QisLog log = new QisLog(userId, logger, level, action, message, referenceId, reference, clientSource);
		qisLogRepository.save(log);
	}

	private String formatMessage(Long userId, String action, String reference, String message) {
		return userId + ":" + action + ":" + reference + "=>" + message;
	}

	private String getClientSource() {

		String clientSource = "";

		if (httpRequest != null) {

			String remoteAddr = httpRequest.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = httpRequest.getRemoteAddr();
			}

			clientSource = "x-forwarded-for:" + remoteAddr;

			Enumeration<String> headerNames = httpRequest.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();

				switch (key) {
				case "x-real-ip":
				case "cf-connecting-ip":
				case "user-agent":
					String value = httpRequest.getHeader(key);
					if ("".equals(clientSource)) {
						clientSource = key + ":" + value;
					} else {
						clientSource += ", " + key + ":" + value;
					}
				}
			}
		}

		return clientSource;
	}
}
