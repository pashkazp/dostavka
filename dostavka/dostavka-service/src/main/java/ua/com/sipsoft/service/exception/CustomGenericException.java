package ua.com.sipsoft.service.exception;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomGenericException extends RuntimeException {

	private static final long serialVersionUID = -4902468204270195408L;
	private String errCode = "";
	private String errMsg = "";
	private String errMsgExt = "";

	/**
	 * @param errCode
	 * @param errMsg
	 */
	public CustomGenericException(String errCode, String errMsg) {
		super();
		this.errCode = StringUtils.defaultString(errCode);
		this.errMsg = StringUtils.defaultString(errMsg);
	}

	/**
	 * @param errCode
	 * @param errMsg
	 * @param errMsgExt
	 */
	public CustomGenericException(String errCode, String errMsg, String errMsgExt) {
		super();
		this.errCode = StringUtils.defaultString(errCode);
		this.errMsg = StringUtils.defaultString(errMsg);
		this.errMsgExt = StringUtils.defaultString(errMsgExt);
	}
}
