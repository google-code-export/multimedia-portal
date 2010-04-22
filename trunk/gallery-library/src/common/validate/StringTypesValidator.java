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

package common.validate;

import common.beans.BeanManager;
import common.types.typesCheck.TypesChecker;
import common.types.typesCorrect.TypesCorrecter;
import java.util.Iterator;
import java.util.Map;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * validates string parameters for being of specified type
 * NOTE onty working on a String properties
 * @author demchuck.dima@gmail.com
 */
public class StringTypesValidator implements Validator{

	private String[] names;
	private String[] types;

	private BeanManager manager;

    private TypesChecker checker=TypesChecker.getTypesChecker();
	private TypesCorrecter correcter=TypesCorrecter.getTypesCorrecter();


	/**
	 * @param names names of properties
	 * @param types types of properties
	 * @param manager to access bean properties
	 */
	public StringTypesValidator(BeanManager manager, String[] names, String[] types){
            this.names = names;
			this.types = types;
			this.manager = manager;
			this.manager.addProperties(this.names);
	}

	/**
	 * initializes StringTypesValidator using a class and map of fields types
	 * @param types map that contains (property name - property type) pairs
	 * @param manager
	 */
	public StringTypesValidator(BeanManager manager, Map<String,String> types){
		this.names = new String[types.size()];
		this.types = new String[types.size()];
		Iterator<String> i = types.keySet().iterator();
		int k=0;
		while (i.hasNext()){
			this.names[k]=i.next();
			this.types[k]=types.get(names[k]);
			k++;
		}
		this.manager = manager;
		this.manager.addProperties(this.names);
	}

	@Override
	public void validate(Object command, Errors err) {
		String tmp;
		for (int i=0;i<names.length;i++){
			//getting property from bean
			String s = (String) manager.getProperty(command,names[i]);
			if (s!=null&&!s.equals("")){
				//reverting modifications by calling recorrect method
				tmp = correcter.recorrect(s, types[i]);
				if (tmp!=null) s = tmp;
				int rez = checker.check(s, types[i]);
				if (rez<0){
					err.rejectValue(names[i], "typeMismatch."+names[i]);
				}else if (rez>0){
					tmp = correcter.correct(s, types[i]);
					if (tmp!=null) manager.setProperty(command, names[i], tmp);
				}
			}
		}
	}

	@Override
	public boolean supports(Class arg0) {return manager.getKlass().isAssignableFrom(arg0);}

}
