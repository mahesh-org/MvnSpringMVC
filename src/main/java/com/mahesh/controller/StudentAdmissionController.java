package com.mahesh.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mahesh.bean.Student;
import com.mahesh.exception.StudentInvalidNameException;
import com.mahesh.property.CustomNamePropertyEditor;

@Controller
public class StudentAdmissionController {
	
	@RequestMapping(value = "/empty", 
			consumes="text/html",
			produces={"application/json","application/xml"},
			headers={"name=pankaj", "id=1"},
			method = {RequestMethod.GET, RequestMethod.POST}
			)
	public void empty() {
		
	}
	
	@RequestMapping(value = "/admissionForm.html", method = RequestMethod.GET)
	public ModelAndView getAdmissionForm() {
		ModelAndView model = new ModelAndView("AdmissionForm");
		
		return model;
	}
	
	/**
	 * 3
	 */
	/*@RequestMapping(value = "/submitAdmissionForm.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(
			@RequestParam(value = "studentName", defaultValue = "Mr. ABC") String name,
			@RequestParam("studentHobby") String hobby) {
		
		ModelAndView model = new ModelAndView("AdmissionSuccess");
		model.addObject("message", "Details submitted by you: Name:" + name + " Hobby:" + hobby);
		
		return model;
	}*/
	
	/**
	 * 4
	 * Using MAP as we tested in @PathVarible
	 * @param name
	 * @param hobby
	 * @return
	 */
	/*@RequestMapping(value = "/submitAdmissionForm.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(@RequestParam Map<String, String> reqPar) {
		
		final String name = reqPar.get("studentName");
		final String hobby = reqPar.get("studentHobby");
		
		ModelAndView model = new ModelAndView("AdmissionSuccess");
		model.addObject("message", "Details submitted by you: Name:" + name + " Hobby:" + hobby);
		
		return model;
	}*/
	
	/**
	 * 5  - not using using ModelAttribute
	 * @param reqPar
	 * @return
	 */
	/*@RequestMapping(value = "/submitAdmissionForm.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(@RequestParam Map<String, String> reqPar) {
		
		final String name = reqPar.get("studentName");
		final String hobby = reqPar.get("studentHobby");
		
		Student student = new Student();
		student.setStudentName(name);;
		student.setStudentHobby(hobby);
		
		ModelAndView model = new ModelAndView("AdmissionSuccess");
		model.addObject("message", "IBM INDIA PVT LTD");
		model.addObject("student", student);
		
		return model;
	}*/
	
	/**
	 * 6  - using ModelAttribute as method argument - Make sure that it matches with the bean attribute name in jsp.
	 * @param reqPar
	 * @return
	 */
	/*@RequestMapping(value = "/submitAdmissionForm.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(@ModelAttribute("student") final Student student) {
		
		ModelAndView model = new ModelAndView("AdmissionSuccess");
		model.addObject("message", "IBM INDIA PVT LTD");
		
		return model;
	}*/
	
	/**
	 * 7  - using ModelAttribute at method level - Check addingCommonObject() method and add model every time.
	 */
	/*@RequestMapping(value = "/submitAdmissionForm.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(@ModelAttribute("student") final Student student) {
		
		ModelAndView model = new ModelAndView("AdmissionSuccess");
		
		return model;
	}*/
	
	/**
	 * 8 - Data binding with Date, Collections(ArrayList), Long etc
	 * In the student class, we have added - Mobile, DOB, skills.
	 */
	/*@RequestMapping(value = "/submitAdmissionForm.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(@ModelAttribute("student") final Student student) {
		ModelAndView model = new ModelAndView("AdmissionSuccess");		
		return model;
	}*/
	
	/**
	 * 9. Data Binding with a User-Defined Type, BindingResult (concept)
	 *  remember that we have addedd Address userType in the student  class
	 *  and also understand about the BindingResult (this is mainly when there is an error in the jsp fields.
	 */
	@RequestMapping(value = "/submitAdmissionForm.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(@Valid @ModelAttribute("student") final Student student, 
			BindingResult result) throws Exception {
		
		if(result.hasErrors()) {
			ModelAndView model = new ModelAndView("AdmissionForm");
			return model;
		}
		if("Mr.Mahesh".equals(student.getStudentName())) {
			throw new StudentInvalidNameException(student.getStudentName());
		}
		ModelAndView model = new ModelAndView("AdmissionSuccess");		
		return model;
	}
		
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//binder.setDisallowedFields("studentMobile");
		SimpleDateFormat format = new SimpleDateFormat("DD-MM-YYYY");
		
		//These things called as spring builtin property editor classes (customer Date editor, number, boolean etc) 
		binder.registerCustomEditor(Date.class, "studentDOB", new CustomDateEditor(format, false));
		binder.registerCustomEditor(String.class, "studentName", new CustomNamePropertyEditor());
	}
	
	//This method will be called before any of the spring model method
	@ModelAttribute
	public void addingCommonObject(Model model) {
		model.addAttribute("message", "IBM INDIA PVT LTD");
	}
	
	/**
	 * 
	 * Notice that for EmployeeNotFoundException handler, I am returning ModelAndView and hence http status code will be sent as OK (200).
	 */
	@ExceptionHandler(StudentInvalidNameException.class)
	public ModelAndView handleInvalidStudentNameException(final HttpServletRequest request, Exception ex) {
		System.out.println("handleInvalidStudentNameException()...");
		ModelAndView model = new ModelAndView();
		model.addObject("exception", ex);
		model.addObject("url", request.getRequestURL());
		
		model.setViewName("error");
		return model;
		
	}
}
