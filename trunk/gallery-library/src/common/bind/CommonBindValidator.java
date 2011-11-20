/*
 *  Copyright 2010 demchuck.dima@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package common.bind;

import common.beans.propertyEditors.StringTypesPropertyEditor;
import common.types.TypesCheckAndCorrect;
import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 * it enables checking of required fields and types checking and antispam code
 * if you want to add some extra functionality do not forget to call method of a superclass<br>
 * @author demchuck.dima@gmail.com
 */
public class CommonBindValidator extends ABindValidator{
	protected Map<String,PropertyEditor> editors;
	protected String[] required_fields;
    protected String[] allowed_fields;

	protected String anti_spam_code;

	protected String required_field_arrays_marker;
	protected String[] required_field_arrays;
	protected String required_vector_name;
	protected boolean required_field_vector = false;

	/*
	 * Unused
	 *  if want to use you must unkomment some lines below
	 * types may not contain only a type name
	 * [typeName][,][required]<br>
	 * required - means that given field must be of type [typeName] and may not be null of an empty string
	 * this is made for checking array types and collections ...<br>
	 */

	/**
	 * an empty method
	 * @param binder
	 */
	@Override
	protected void initBinder(ServletRequestDataBinder binder){
		binder.setBindEmptyMultipartFiles(false);
		binder.setRequiredFields(required_fields);
        binder.setAllowedFields(allowed_fields);
		binder.setFieldMarkerPrefix("_");
		binder.setFieldDefaultPrefix("!");
        if (editors!=null){
            Iterator<Entry<String,PropertyEditor>> i = editors.entrySet().iterator();
            while(i.hasNext()){
                Entry<String,PropertyEditor> e = i.next();
                binder.registerCustomEditor(String.class, e.getKey(), e.getValue());
            }
        }

        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}

	@Override
	protected void validate(Object command, Errors err, HttpServletRequest request) {
		if (required_field_arrays_marker!=null&&required_field_arrays!=null){
			String[] marker = request.getParameterValues(required_field_arrays_marker);
			if (marker!=null){
				if (required_field_vector){
					for (int i=0;i<required_field_arrays.length;i++){
						for (int j=0;j<marker.length;j++){
							String s = request.getParameter(required_vector_name+"["+marker[j]+"]."+required_field_arrays[i]);
							if (s==null||s.equals("")){
								err.rejectValue(required_vector_name+"["+marker[j]+"]", "required");
							}
						}
					}
				}else{
					for (int i=0;i<required_field_arrays.length;i++){
						for (int j=0;j<marker.length;j++){
							String s = request.getParameter(required_field_arrays[i]+"["+j+"]");
							if (s==null||s.equals("")){
								err.rejectValue(required_field_arrays[i], "required");
							}
						}
					}
				}
			}
		}
		if (anti_spam_code!=null){
			String error_code = common.web.filters.Antispam.canAccess(request, request.getParameter(anti_spam_code), true);
			if (error_code!=null){
                err.reject(error_code);
				common.CommonAttributes.addErrorMessage(error_code, request);
			}
		}
	}

	public void setRequiredFields(String[] value){this.required_fields = value;}
	public void setAllowedFields(String[] value){this.allowed_fields = value;}
	/**
	 * name of parameter with antispam code
	 * @param name parameter
	 */
	public void setAntiSpamCodeParam(String name){this.anti_spam_code = name;}
	/**
	 * is used with setRequiredFieldId
	 * prefixes to check suffix is its number in array [i]
	 * an array of field values that might not be null
	 * field[0], field[1], ...
	 * @param value parameters prefixes to check
	 */
	public void setRequiredFieldArrays(String[] value){this.required_field_arrays = value;}
	/**
	 * name of parameter that is actually checked for existence
	 * and its length is lenght of array in setRequiredFieldArrays
	 * @param value parameter
	 */
	public void setRequiredFieldArraysMarker(String value){this.required_field_arrays_marker = value;}
	/**
	 * if true the required field arrays is vector of beans
	 * and is binded in form
	 * required_vector_name[number].required_field_arrays[x]
	 * default is false
	 * @param value
	 */
	public void setRequiredFieldArraysVector(boolean value){this.required_field_vector = value;}
	/**
	 * name of property with vector
	 * @param value
	 */
	public void setRequiredVectorName(String value){this.required_vector_name = value;}

	//public static final String TYPES_SEPARATOR = ",";
	//public static final String REQUIRED_TYPE = "required";

	public void setFieldTypes(Map<String,String> types){
		TypesCheckAndCorrect tc = TypesCheckAndCorrect.get();
		Iterator<Entry<String,String>> i = types.entrySet().iterator();
		editors = new HashMap<String,PropertyEditor>();
		while(i.hasNext()){
			Entry<String,String> e = i.next();
			//String[] val = e.getValue().split(TYPES_SEPARATOR);
			//if (val.length<2){
				editors.put(e.getKey(), new StringTypesPropertyEditor(e.getValue(), tc, false));
			//}else{
			//	editors.put(e.getKey(), new StringTypesPropertyEditor(val[0], tc, val[1].equals(REQUIRED_TYPE)));
			//}
		}
	}

}
