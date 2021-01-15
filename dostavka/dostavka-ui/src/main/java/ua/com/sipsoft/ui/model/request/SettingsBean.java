package ua.com.sipsoft.ui.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class SettingsBean represent a unit of settings
 * 
 * @author Pavlo Degtyaryev
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettingsBean {

	/** The name of the parameter. */
	private String name;

	/** The value of the parameter. */
	private String value;

}
