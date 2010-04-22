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

package common.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import security.beans.User;


/**
 * this class simply cashes setters and getters for a class
 * fields must be first set using addProperties() method
 * @author demchuck.dima@gmail.com
 */
public class BeanManager {
	protected Logger logger = Logger.getLogger(getClass());
    private HashMap<String,Method> setMethods;
    private HashMap<String,Method> getMethods;
	private final Class klass;

	/**
	 * an instance with no methods will be created
	 * @param klass class(for the bean) that will be managed
	 */
	public BeanManager(Class klass){
		this.klass = klass;
		this.setMethods = new HashMap<String,Method>();
		this.getMethods = new HashMap<String,Method>();
	}

	/** @return class of managed beans */
	public Class getKlass(){return this.klass;}

	/**
	 * registers set and get methods for properties
	 * @param properties names of bean properties to be registered
	 */
	public void addProperties(String[] properties){
        try {
			PropertyDescriptor[] pd = Introspector.getBeanInfo(User.class).getPropertyDescriptors();
			for (String s:properties){
				if (!setMethods.containsKey(s)||!getMethods.containsKey(s)){
					for (PropertyDescriptor descr:pd){
						if (descr.getName().equals(s)){
							setMethods.put(s,descr.getWriteMethod());
							getMethods.put(s,descr.getReadMethod());
						}
					}
				}
			}
		} catch (IntrospectionException ex) {
			logger.error(ex);
        } catch (SecurityException ex) {
			logger.error(ex);
        } catch (IllegalArgumentException ex) {
			logger.error(ex);
        }
	}

	/**
	 * registers set and get methods for properties
	 * @param properties names of bean properties to be registered
	 */
	public void addProperties(Collection<String> properties){
        try {
			PropertyDescriptor[] pd = Introspector.getBeanInfo(User.class).getPropertyDescriptors();
			for (String s:properties){
				if (!setMethods.containsKey(s)||!getMethods.containsKey(s)){
					for (PropertyDescriptor descr:pd){
						if (descr.getName().equals(s)){
							setMethods.put(s,descr.getWriteMethod());
							getMethods.put(s,descr.getReadMethod());
						}
					}
				}
			}
		} catch (IntrospectionException ex) {
			logger.error(ex);
        } catch (SecurityException ex) {
			logger.error(ex);
        } catch (IllegalArgumentException ex) {
			logger.error(ex);
        }
	}

	/**
	 * sets given property of target object to a value
	 * @param target object that property will be set
	 * @param property name of property to set
	 * @param value value to set
	 */
	public void setProperty(Object target, String property, Object value){
		try {
			setMethods.get(property).invoke(target, value);
			return;
		} catch (Exception ex) {
            logger.error(ex);
		}
		throw new NullPointerException("error setting bean property: "+property+" of class: "+klass.getName());
	}

	/**
	 * sets given property of target object to a value
	 * @param target object that property will be set
	 * @param property name of property to set
	 * @return value of a field
	 */
	public Object getProperty(Object target, String property){
		try {
			return getMethods.get(property).invoke(target);
		} catch (Exception ex) {
            logger.error(ex);
		}
		throw new NullPointerException("error getting bean property: "+property+" of class: "+klass.getName());
	}


}
