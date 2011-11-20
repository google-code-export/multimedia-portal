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

package common.beans.propertyEditors;

import common.types.TypesCheckAndCorrect;
import common.types.typesCheck.classes.ITypeChecker;
import common.types.typesCorrect.classes.ITypeCorrecter;
import java.beans.PropertyEditorSupport;
//import org.apache.log4j.Logger;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StringTypesPropertyEditor extends PropertyEditorSupport{
	//private Logger logger = Logger.getLogger(getClass());
	protected ITypeChecker checker;
	protected ITypeCorrecter correcter;
	protected boolean required;

	/**
	 * Create a new StringTypesPropertyEditor.
	 * @param type string type that will be checked
	 * @param tc where to find checker and correcter for this edior
	 * @param required if true rises error if value is null or an empty string
	 */
	public StringTypesPropertyEditor(String type, TypesCheckAndCorrect tc, boolean required) {
		if (type==null||type.equals("")){
			this.checker = null;
			this.correcter = null;
		}else{
			this.checker = tc.getChecker(type);
			this.correcter = tc.getCorrecter(type);
		}
		this.required = required;
	}

	/**
	 * Create a new StringTypesPropertyEditor.
	 * @param checker
	 * @param correcter
	 * @param required if true rises error if value is null or an empty string
	 */
	public StringTypesPropertyEditor(ITypeChecker checker, ITypeCorrecter correcter, boolean required) {
		this.checker = checker;
		this.correcter = correcter;
		this.required = required;
	}

	@Override
	public void setAsText(String text) {
		if (required&&(text==null||text.equals(""))){
			throw new IllegalArgumentException("required");
		}

		if (text == null) {
			//logger.info("setAsText("+text+") null");
			setValue(null);
		}else{
            String tmp = text.trim();
			if (checker!=null&&checker.check(tmp)<0){
				//logger.info("setAsText("+text+") error");
				throw new IllegalArgumentException(tmp);
			}else{
				//logger.info("setAsText("+text+") ok");
				if (correcter==null){
					setValue(tmp);
				}else{
					setValue(correcter.correct(correcter.recorrect(tmp)));
				}
			}
		}
		//logger.info("after("+getValue()+")");
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}

}
