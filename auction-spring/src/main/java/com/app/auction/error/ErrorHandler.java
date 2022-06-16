package com.app.auction.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;


@RestController
public class ErrorHandler implements ErrorController {
	@Autowired
	private ErrorAttributes errorAttributes;

	@RequestMapping("/error") //bütün request türleri için 
	ApiError handleError(WebRequest webRequest) {
		
		//Attributes   errorAttributes in implement edildiği DefaultErrorAttributes daki üstte yazan
		//timestamp status error message .. gibi özellikler biz de onları alıyoruz.
		
		Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest,
				ErrorAttributeOptions.of(Include.MESSAGE, Include.BINDING_ERRORS));
		
		String message = (String) attributes.get("message");
		String path = (String) attributes.get("path");
		int status = (Integer) attributes.get("status");
		ApiError error = new ApiError(status, message, path); //kendi api error classımıza ekliyoruz
		
		//hem valid hem auth errorlari alıyor. authcontrollerdaki exception handlerı kaldırıp alttakini yazdık
		//
		if (attributes.containsKey("errors")) { //null gelebilir onu engellemek icin
			@SuppressWarnings("unchecked")	//unchecked cast from object to listfield uyarısını görmezden gelmek icin
			List<FieldError> fieldErrors = (List<FieldError>) attributes.get("errors");
			Map<String, String> validationErrors = new HashMap<>();
			for (FieldError fieldError : fieldErrors) {
				validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			error.setValidationErrors(validationErrors);
		}
		
		return error;
	}
}
