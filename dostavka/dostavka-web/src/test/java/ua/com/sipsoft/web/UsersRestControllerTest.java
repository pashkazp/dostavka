package ua.com.sipsoft.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.ui.model.request.user.UserRegistrationRequest;
import ua.com.sipsoft.util.AppURL;

//@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@WebMvcTest(UsersRestController.class)
@ContextConfiguration
@Slf4j
@DisplayName("Testing User REST controller UsersRestController")
public class UsersRestControllerTest
//extends DostavkaApplicationTest
{

	@Autowired
	private WebApplicationContext webApplicationContext;

//	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

//	@Autowired
	private XmlMapper xmlMapper;

//	@MockBean
//	private UserServiceImpl userService;
//	@MockBean
//	private FacilitiesServiceToRepoImpl m1;
//	@MockBean
//	private FacilityAddrServiceToRepoImpl m2;
//	@MockBean
//	private ArchivedSheetsServiceImpl m3;
//	@MockBean
//	private NewUserRegistrationListener m4;
//	@MockBean
//	private ArchivedVisitsServiceImpl m5;
//	@MockBean
//	private IssuedCourierVisitServiceImpl m6;
//	@MockBean
//	private IssuedRouteSheetServiceImpl m7;
//	@MockBean
//	private VerificationTokenServiceImpl m8;
//	@MockBean
//	private RememberPasswordListener m9;
//	@MockBean
//	private DraftRouteSheetServiceImpl m10;
//	@MockBean
//	private WebMvcConfigurationSupport m11;
//	@MockBean
//	private IssuedCourierVisitServiceImpl m12;
//	@MockBean
//	private IssuedCourierVisitServiceImpl m13;
//	@MockBean
//	private IssuedCourierVisitServiceImpl m14;
//	@MockBean
//	private IssuedCourierVisitServiceImpl m15;
//	@MockBean
//	private IssuedCourierVisitServiceImpl m16;

	private final static String TEST_USER_ID = "user-id-123";

	@PostConstruct
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		xmlMapper = new XmlMapper();
	}

	@Nested
	@WithMockUser
	@DisplayName("Get user info by REST request")
	class getUserInfo {

		@Test
		@DisplayName("using JSON media type")
		public void test1() throws Exception {
			mockMvc.perform(get(AppURL.API_V1_USERS + "/1")
					.accept(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(jsonPath("$.id").value("1"))
					.andExpect(jsonPath("$.name").value("Admin"))
					.andExpect(jsonPath("$.password").doesNotExist())
					.andExpect(jsonPath("$.confirmPassword").doesNotExist());
		}

		@Test
		@DisplayName("using XML media type")
		public void test2() throws Exception {
			mockMvc.perform(
					get(AppURL.API_V1_USERS + "/1")
							.accept(MediaType.APPLICATION_XML_VALUE))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
					.andExpect(xpath("//id").string("1"))
					.andExpect(xpath("//name").string("Admin"))
					.andExpect(xpath("//password").string(""))
					.andExpect(xpath("//confirmPassword").string(""));
		}

		@Test
		@DisplayName("using default media type")
		public void test3() throws Exception {
			mockMvc.perform(
					get(AppURL.API_V1_USERS + "/1"))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE));
		}
	}

	@Nested
	@WithMockUser
	@DisplayName("Get Users list by REST request")
	class getUserList {
		@Test
		@DisplayName("using JSON media type")
		public void test1() throws Exception {
			mockMvc.perform(get(AppURL.API_V1_USERS)
					.accept(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(jsonPath("$[0].id").value("1"))
					.andExpect(jsonPath("$[0].name").value("Admin"))
					.andExpect(jsonPath("$[0].password").doesNotExist())
					.andExpect(jsonPath("$[0].confirmPassword").doesNotExist());
		}

		@Test
		@WithMockUser
		@DisplayName("using XML media type")
		public void test2() throws Exception {
			mockMvc.perform(get(AppURL.API_V1_USERS)
					.accept(MediaType.APPLICATION_XML_VALUE))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
					.andExpect(xpath("//item[1]/id").string("1"))
					.andExpect(xpath("//item[1]/name").string("Admin"))
					.andExpect(xpath("//item[1]/password").string(""))
					.andExpect(xpath("//item[1]/confirmPassword").string(""));
		}

		@Test
		@WithMockUser
		@DisplayName("using default media type")
		public void test3() throws Exception {
			mockMvc.perform(get(AppURL.API_V1_USERS))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE));
		}
	}

	@Nested
	@WithMockUser
	@DisplayName("Try Create User by sent epty request body")
	class createUserEmptyBody {
		@Test
		@DisplayName("using JSON media type")
		public void testUsers6() throws Exception {
			// ObjectMapper mapper = new ObjectMapper();
			// UserUpdateRequest userCreationRequest = new UserUpdateRequest();
			// userCreationRequest.setEmail("email@email.net");
			mockMvc.perform(post(AppURL.API_V1_USERS)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON_VALUE)
					.content(""))
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@WithMockUser
	@DisplayName("Update User by sent empty REST request")
	class updateUserByEmptyBody {

		@Test
		@DisplayName("using JSON media type")
		public void testUsers7() throws Exception {
			mockMvc.perform(put(AppURL.API_V1_USERS + "/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON_VALUE)
					.content(""))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
		}

		@Test
		@DisplayName("using XML media type")
		public void testUsers8() throws Exception {
			mockMvc.perform(put(AppURL.API_V1_USERS + "/1")
					.contentType(MediaType.APPLICATION_XML)
					.accept(MediaType.APPLICATION_XML_VALUE)
					.content(""))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE));
		}
	}

	@Nested
	@WithMockUser
	@DisplayName("Update User by sent REST request with incorrect email property")
	// @Disabled
	class updateUser {

		@Test
		@DisplayName("using XML media type")
		public void testUsers10() throws Exception {
			xmlMapper = new XmlMapper();
			UserRegistrationRequest userUpdateRequest = new UserRegistrationRequest();
			userUpdateRequest.setEmail("email@email");
			log.warn("UserRegistrationRequest object: {}", xmlMapper.writeValueAsString(userUpdateRequest));
			mockMvc.perform(put(AppURL.API_V1_USERS + "/1")
					.contentType(MediaType.APPLICATION_XML)
					.accept(MediaType.APPLICATION_XML_VALUE)
					.content(xmlMapper.writeValueAsString(userUpdateRequest)))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE));
		}

		@Test
		@DisplayName("using  JSON media type")
		public void testUsers9() throws Exception {
			objectMapper = new ObjectMapper();
			UserRegistrationRequest userUpdateRequest = new UserRegistrationRequest();
			userUpdateRequest.setEmail("email@email");
			log.warn("UserRegistrationRequest object: {}", objectMapper.writeValueAsString(userUpdateRequest));
			mockMvc.perform(put(AppURL.API_V1_USERS + "/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON_VALUE)
					.content(objectMapper.writeValueAsString(userUpdateRequest)))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
		}
	}

//	@Test
//	@WithMockUser
//	@DisplayName("test template")
//	@Disabled
//	public void testUsers60() throws Exception {
//		objectMapper = new ObjectMapper();
//		UserUpdateRequest userCreationRequest = new UserUpdateRequest();
//		// userCreationRequest.setEmail("email@email.net");
//		mockMvc.perform(post(AppURL.API_V1_USERS)
//				.contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON_VALUE)
//				.content(objectMapper.writeValueAsString(userCreationRequest)))
//				.andExpect(status().isBadRequest())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
//				.andExpect(jsonPath("$[0].id").value("1"))
//				.andExpect(jsonPath("$[0].name").value("Admin"))
//				.andExpect(jsonPath("$[0].password").doesNotExist())
//				.andExpect(jsonPath("$[0].confirmPassword").doesNotExist());
//
//	}

}
