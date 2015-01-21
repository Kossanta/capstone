package org.coursera.capstone.symptom.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.coursera.capstone.symptom.repository.Account;
import org.coursera.capstone.symptom.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * The controller which handles all the general requests that are not redirected
 * to patient controller or doctor controller
 * 
 */
// Tell Spring that this class is a Controller that should
// handle all the general HTTP requests that are not redirected to
// patient controller or doctor controller for the DispatcherServlet
@Controller
public class GeneralController {

	// the registration id parameter
	final String REGID_PARAMETER = "regId";
	// The path to add user
	final String ADD_USER = "/addUser";
	// The path to add the registration id of the device for the push
	// notification to the user account
	final String ADD_REGID = "/addRegId";
	// the path for the test web service to check the authentication of the user
	final String TEST = "/test";
	// the path for population of the tablet account with all the available
	// users which are hard coded
	final String POPULATE_ACCOUNT_TABLE = "/populateUsers";

	// The AccountRepository that we are going to store our accounts
	// in. We don't explicitly construct a AccountRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring.
	//
	@Autowired
	private AccountRepository accountRepo;

	/**
	 * Receives POST requests to /addUser and converts the HTTP request body,
	 * which should contain json, into an Account object before save it to the
	 * database. The @RequestBody annotation on the Account parameter is what
	 * tells Spring to interpret the HTTP request body as JSON and convert it
	 * into a Account object to pass into the method. The
	 * 
	 * @ResponseBody annotation tells Spring to convert the return value from
	 *               the method back into JSON and put it into the body of the
	 *               HTTP response to the client.
	 * 
	 */

	@RequestMapping(value = ADD_USER, method = RequestMethod.POST)
	public @ResponseBody
	Account addUser(@RequestBody Account account, HttpServletResponse response) {
		if (accountRepo.findByUsername(account.getUsername()) == null) {
			accountRepo.save(account);
			response.setStatus(HttpServletResponse.SC_OK);
			return accountRepo.findByUsername(account.getUsername());
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	/**
	 * Receives POST requests to /addRegId and converts the HTTP request body,
	 * which should contain json. The @RequestBody annotation on the Account
	 * parameter is what tells Spring to interpret the HTTP request body as JSON
	 * and convert it into a Account object to pass into the method. The @RequestParam
	 * annotation on the String regId tell Spring to use the "regId" parameter
	 * in the HTTP request's query string as the value for the regId method
	 * parameter. The HttpServletResponse will contain the response of the
	 * request. The @ResponseBody annotation tells Spring to convert the return
	 * value from the method back into JSON and put it into the body of the HTTP
	 * response to the client.
	 * 
	 */
	@RequestMapping(value = ADD_REGID, method = RequestMethod.POST)
	public @ResponseBody
	Account addRegIdTo(@RequestBody Account account,
			@RequestParam(REGID_PARAMETER) String regId,
			HttpServletResponse response) {
		if (accountRepo.findByUsername(account.getUsername()) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			accountRepo.findByUsername(account.getUsername()).setRegId(regId);
			response.setStatus(HttpServletResponse.SC_OK);
			return accountRepo.findByUsername(account.getUsername());
		}
	}

	/**
	 * Receives GET requests to /test and returns void. The main purpose of this
	 * request is to validate the the session that is being created is a valid
	 * one.
	 */
	@RequestMapping(value = TEST, method = RequestMethod.GET)
	public ResponseEntity<Void> getTest() throws IOException {

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Receives POST requests to /populateUsers and converts the HTTP request
	 * body, which should contain json. The @RequestBody annotation on the list
	 * parameter is what tells Spring to interpret the HTTP request body as JSON
	 * and convert it into an ArrayList<Account> array to pass into the method.
	 * The HttpServletResponse will contain the response of the request. If the
	 * first hard coded user doesn't exist in the repository then for all the
	 * objects in the list each object will be saved in the repository. The
	 * request returns void. The reason for this request is to hard code the
	 * user accounts into the repository
	 * 
	 */
	@RequestMapping(value = POPULATE_ACCOUNT_TABLE, method = RequestMethod.POST)
	public ResponseEntity<Void> populateUsers(
			@RequestBody ArrayList<Account> list, HttpServletResponse response) {
		if (accountRepo.findByUsername("drJohn") == null) {
			for (int i = 0; i < list.size(); i++) {
				accountRepo.save(list.get(i));
			}
		} else {

		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
