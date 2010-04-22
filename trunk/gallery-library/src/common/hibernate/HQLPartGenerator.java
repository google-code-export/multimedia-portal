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

package common.hibernate;

import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class HQLPartGenerator {
	/* before first index */
	public static final String PROP_NAME_PREFIX = "prop";
	/* between all indexes */
	public static final String PROP_NAME_DELIMITER = "_";

    /** Creates a new instance of SQLPartGenerator */
    private HQLPartGenerator() {}

    /**
	 * return colNames separated by ','
	 * and appends it to rez
	 * @param colNames column names that will be in select statement
	 * @param rez variable where to store result must not be null
     */
	public static void getValuesList(String colNames[],StringBuilder rez){
		if (colNames!=null&&colNames.length>0){
			rez.append("SELECT ");
			rez.append(colNames[0]);
			for (int i=1;i<colNames.length;i++){
					rez.append(",");
					rez.append(colNames[i]);
			}
		}
    }

    /**
	 * return colNames separated by ',' and starting with SELECT word
	 * and appends it to rez
	 * @param propNames propertie names that will be in select statement
	 * @param colAliases aliases for columns
	 * @param rez variable where to store result must not be null
     */
	public static void getValuesListWithAliases(String propNames[],String colAliases[],StringBuilder rez){
		if (propNames!=null&&propNames.length>0&&colAliases!=null&&propNames.length==colAliases.length){
			rez.append("SELECT ");
			rez.append(propNames[0]);
			rez.append(" as ");
			rez.append(colAliases[0]);
			for (int i=1;i<propNames.length;i++){
				rez.append(", ");
				rez.append(propNames[i]);
				rez.append(" as ");
				rez.append(colAliases[i]);
			}
		}
    }

    /**
	 * appends result to rez
	 *
	 * @param colNames column names that will be in update statement
	 * @param rez variable where to store result must not be null
     */
	public static void getValuesListForUpdate(String colNames[],StringBuilder rez){
		if (colNames!=null&&colNames.length>0){
			rez.append(" SET ");
			rez.append(colNames[0]);
			rez.append(" = :");
			rez.append(colNames[0]);
			for (int i=1;i<colNames.length;i++){
				rez.append(",");
				rez.append(colNames[i]);
				rez.append(" = :");
				rez.append(colNames[i]);
			}
		}
    }

    /**
	 * appends result to rez
	 * parameter values are ? (it means that must be set using setXXX(i,Value))
	 * i- number starting with 0
	 * value - value of parameter
	 * @param colNames column names that will be in update statement
	 * @param rez variable where to store result must not be null
     */
	public static void getValuesListForUpdateNumbers(String colNames[],StringBuilder rez){
		if (colNames!=null&&colNames.length>0){
			rez.append(" SET ");
			rez.append(colNames[0]);
			rez.append(" = ?");
			for (int i=1;i<colNames.length;i++){
				rez.append(",");
				rez.append(colNames[i]);
				rez.append(" = ?");
			}
		}
    }

    /*
	 * and appends it to rez
     * @param orderBy columns by which you want to order result
     * @param orderHow type of order 'asc' or 'desc'
	 * @param rez variable where to store result must not be null
     */
    public static void getOrderBy(String orderBy[],String orderHow[],StringBuilder rez){
        if (orderBy!=null&&orderBy.length>0){
			rez.append(" ORDER BY ");
			if (orderHow!=null&&orderHow.length>0){
				rez.append(orderBy[0]);
				rez.append(" ");
				rez.append(orderHow[0]);
				for (int i=1;i<orderBy.length;i++){
					rez.append(",");
					rez.append(orderBy[i]);
					rez.append(" ");
					rez.append(orderHow[i]);
				}
			}else{
				rez.append(orderBy[0]);
				for (int i=1;i<orderBy.length;i++){
					rez.append(",");
					rez.append(orderBy[i]);
				}
			}
        }
    }

    /**
     * if limitFirst<0 returns limit for selecting limitTotal rows
     * @param limitFirst number of first row that will be selected
     * @param limitTotal total count of rows that may be selected (it may be less)
	 * @param rez variable where to store result must not be null
     */
    public static void getLimit(long limitFirst,long limitTotal,StringBuilder rez){
        if (limitFirst>=0){
            rez.append(" LIMIT ");
			rez.append(limitFirst);
            if (limitTotal>0){
                rez.append(",");
				rez.append(limitTotal);
			}
        }else{
            if (limitTotal>0){
                rez.append(" LIMIT 0,");
				rez.append(limitTotal);
			}
        }
    }

    /**
	 * values must be set using setParameter("prop"+i,o)
	 * where i is number of property and o is its value
	 * warning null parameters must not be set
	 * starting with prop0
	 * you may use -appendWherePropertiesValue- method to set prameters
     * @param whereColumns columns by which you want to select result
     * @param whereValues values of columns by wich you want to select result
	 * @param rez variable where to store result must not be null
     */
    public static void getWhereManyColumns(String[] whereColumns,Object[] whereValues,StringBuilder rez){
        if (whereColumns!=null&&whereValues!=null&&whereColumns.length>0&&whereValues.length>0){
            rez.append(" WHERE ");
			if (whereValues[0]==null){
				rez.append(whereColumns[0]);
				rez.append(" IS NULL");
			}else{
				rez.append(whereColumns[0]);
				rez.append(" = :");
				rez.append(PROP_NAME_PREFIX);
				rez.append("0");
			}
            for (int i=1;i<whereColumns.length;i++){
                rez.append(" AND ");
				if (whereValues[i]==null){
					rez.append(whereColumns[i]);
					rez.append(" IS NULL");
				}else{
					rez.append(whereColumns[i]);
					rez.append(" = :");
					rez.append(PROP_NAME_PREFIX);
					rez.append(i);
				}
            }
        }
    }

    /**
	 * values must be set using setParameter("prop"+i,o)
	 * where i is number of property and o is its value
	 * warning null parameters must not be set
	 * starting with prop0
	 * you may use -appendWherePropertiesValue- method to set prameters
     * @param whereColumn column fow where criteria
     * @param whereValue values of column by wich you want to select result
	 * @param rez variable where to store result must not be null
     */
    public static void getWhereColumnValue(String whereColumn,Object whereValue,StringBuilder rez){
		if (whereColumn!=null){
			rez.append(" WHERE ");
			rez.append(whereColumn);
			if (whereValue==null){
				rez.append(" is null");
			}else{
				rez.append(" = :");
				rez.append(PROP_NAME_PREFIX);
			}
		}
    }

    /**
	 * values must be set using setParameter("prop"+i,o)
	 * where i is number of property and o is its value
	 * warning null parameters must not be set
	 * starting with prop0
	 * you may use -appendWherePropertiesValue- method to set prameters
     * @param whereColumn column fow where criteria
     * @param whereValue values of column by wich you want to select result
	 * @param rez variable where to store result must not be null
     */
    public static void getWhereColumnValueRelation(String whereColumn, Object whereValue, String relation, StringBuilder rez){
		if (whereColumn!=null){
			rez.append(" WHERE ");
			rez.append(whereColumn);
			if (whereValue==null){
				rez.append(" is null");
			}else{
                if (relation==null){
                    rez.append(" = :");
                } else {
                    rez.append(" ");
                    rez.append(relation);
                    rez.append(" :");
                }
                rez.append(PROP_NAME_PREFIX);
            }
		}
    }

    /**
	 * values must be set using setParameter("prop"+i,o)
	 * where i is number of property and o is its value
	 * warning null parameters must not be set
	 * starting with prop0
	 * you may use -appendWherePropertiesValue- method to set prameters
     * @param whereColumn column fow where criteria
     * @param whereValues values of column by wich you want to select result
	 * @param rez variable where to store result must not be null
     */
    public static void getWhereColumnValues(String whereColumn,Object[] whereValues,StringBuilder rez){
        if (whereColumn!=null&&whereValues!=null&&whereValues.length>0){
            rez.append(" WHERE (");
			if (whereValues[0]==null){
				rez.append(whereColumn);
				rez.append(" IS NULL");
			}else{
				rez.append(whereColumn);
				rez.append(" = :");
				rez.append(PROP_NAME_PREFIX);
				rez.append("0");
			}
            for (int i=1;i<whereValues.length;i++){
                rez.append(" OR ");
				if (whereValues[i]==null){
					rez.append(whereColumn);
					rez.append(" IS NULL");
				}else{
					rez.append(whereColumn);
					rez.append(" = :");
					rez.append(PROP_NAME_PREFIX);
					rez.append(i);
				}
            }
            rez.append(")");
        }
    }

    /**
	 * values must be set using setParameter("prop"+i,o)
	 * where i is number of property and o is its value
	 * warning null parameters must not be set
	 * starting with prop0
	 * you may use -appendWherePropertiesValue- method to set prameters
     * @param whereColumn column fow where criteria
     * @param whereValues values of column by wich you want to select result
	 * @param rez variable where to store result must not be null
     */
    public static void getWhereColumnValues(String whereColumn,List<Object> whereValues,StringBuilder rez){
        if (whereColumn!=null&&whereValues!=null&&whereValues.size()>0){
            rez.append(" WHERE (");
			if (whereValues.get(0)==null){
				rez.append(whereColumn);
				rez.append(" IS NULL");
			}else{
				rez.append(whereColumn);
				rez.append(" = :");
				rez.append(PROP_NAME_PREFIX);
				rez.append("0");
			}
            for (int i=1;i<whereValues.size();i++){
                rez.append(" OR ");
				if (whereValues.get(i)==null){
					rez.append(whereColumn);
					rez.append(" IS NULL");
				}else{
					rez.append(whereColumn);
					rez.append(" = :");
					rez.append(PROP_NAME_PREFIX);
					rez.append(i);
				}
            }
            rez.append(")");
        }
    }

    /**
	 * warning null values are only tested for 'is null'
     * @param whereColumns columns by which you want to select result
     * @param whereValues values of columns by wich you want to select result
	 * @param whereRelations relations between columns(>,<,=,!=,like,... default =)
	 * @param rez variable where to store result must not be null
     */
    public static void getWhereManyColumnsRelations(String whereColumns[],Object whereValues[],String whereRelations[],StringBuilder rez){
        if (whereColumns!=null&&whereValues!=null&&whereRelations!=null&&
				whereColumns.length>0&&whereValues.length>0&&whereRelations.length>0){
            rez.append(" WHERE ");
			if (whereValues[0]==null){
				rez.append(whereColumns[0]);
				rez.append(" IS NULL");
			}else{
				rez.append(whereColumns[0]);
				if (whereRelations[0]==null){
					rez.append("=");
				}else{
					rez.append(" ");
					rez.append(whereRelations[0]);
				}
				rez.append(" :");
				rez.append(PROP_NAME_PREFIX);
				rez.append("0");
			}
            for (int i=1;i<whereColumns.length;i++){
				rez.append(" AND ");
				if (whereValues[i]==null){
					rez.append(whereColumns[i]);
					rez.append(" IS NULL");
				}else{
					rez.append(whereColumns[i]);
					if (whereRelations[i]==null){
						rez.append("=");
					}else{
						rez.append(" ");
						rez.append(whereRelations[i]);
					}
					rez.append(" :");
					rez.append(PROP_NAME_PREFIX);
					rez.append(i);
				}
			}
        }
    }

    /**
	 * values must be set using setParameter("prop_"+i+"_"+j,o)
	 * where i is number of property, j number of value in array and o is its value
	 * warning null parameters must not be set
	 * starting with prop_0_0
	 * use appendWherePropertiesValues to set values
     * @param whereColumns columns by wich you want to select result
     * @param whereValues values of columns by wich you want to select result
     *      each array is the array of values that you search by
	 * @param rez variable where to store result must not be null
     */
    public static void getWhereManyColumnsManyValues(String whereColumns[],Object whereValues[][],StringBuilder rez){
        if (whereColumns!=null&&whereValues!=null&&whereColumns.length>0&&whereValues.length>0){
            rez.append(" WHERE ");
			if (whereValues[0]==null){
				rez.append(whereColumns[0]);
				rez.append(" IS NULL");
			}else{
				rez.append(whereColumns[0]);
				rez.append(" in (:");
				rez.append(PROP_NAME_PREFIX);
				rez.append("0)");
			}
            for (int i=1;i<whereColumns.length;i++){
                rez.append(" AND ");
				if (whereValues[i]==null){
					rez.append(whereColumns[i]);
					rez.append(" IS NULL");
				}else{
					rez.append(whereColumns[i]);
					rez.append(" in (:");
					rez.append(PROP_NAME_PREFIX);
					rez.append(i);
					rez.append(")");
				}
            }
        }
    }

    /**
	 * warning null values are only tested for 'is null'
     * @param whereColumns columns by which you want to select result
     * @param whereValues values of columns by wich you want to select result
	 * @param whereRelations relations between columns(>,<,=,!=,like,... default =)
	 * @param rez variable where to store result must not be null
     */
    public static void getWhereManyColumnsManyValuesRelations(String whereColumns[],String whereRelations[][],Object whereValues[][],StringBuilder rez){
        if (whereColumns!=null&&whereValues!=null&&whereRelations!=null&&
				whereColumns.length>0&&whereValues.length>0&&whereRelations.length>0){
            rez.append(" where (");
			String and = "";
			String or;
            for (int i=0;i<whereColumns.length;i++){
				rez.append(and);
				or = "";
				for (int j=0;j<whereValues[i].length;j++){
					rez.append(or);
					rez.append(whereColumns[i]);
					if (whereValues[i][j]==null){
						rez.append(" is null");
					}else{
						if (whereRelations[i][j]==null){
							rez.append("=");
						}else{
							rez.append(" ");
							rez.append(whereRelations[i][j]);
						}
						rez.append(" :");
						rez.append(PROP_NAME_PREFIX);
						rez.append(i);
						rez.append(PROP_NAME_DELIMITER);
						rez.append(j);
					}
					or = " or ";
				}
				and = ")and(";
			}
			rez.append(")");
        }
    }

	/**
	 * setParameter to given query use it if query was created using
	 *  getWhereManyColumnsManyValues function
	 * @param whereValues values to set as parameters
	 * @param q query where to set parameters
	 * @return query with setted parameters
	 */
	public static Query appendWherePropertiesValue(Object whereValues[],Query q){
		if (whereValues==null) return q;
		Query rez = q;
		for (int i=0;i<whereValues.length;i++){
			if (whereValues[i]!=null){
				rez = rez.setParameter(PROP_NAME_PREFIX+i, whereValues[i]);
			}
		}
		return rez;
	}

	/**
	 * setParameter to given query use it if query was created using
	 *  getWhereManyColumnsManyValues function
	 * @param whereValues values to set as parameters
	 * @param q query where to set parameters
	 * @return query with setted parameters
	 */
	public static Query appendWherePropertiesValue(List<Object> whereValues,Query q){
		Query rez = q;
		for (int i=0;i<whereValues.size();i++){
			if (whereValues.get(i)!=null){
				rez = rez.setParameter(PROP_NAME_PREFIX+i, whereValues.get(i));
			}
		}
		return rez;
	}

	/**
	 * setParameter to given query use it if query was created using
	 *  getWhereManyColumnsManyValues function
	 * @param whereValues values to set as parameters
	 * @param q query where to set parameters
	 * @return query with setted parameters
	 */
	public static Query appendWherePropertiesValues(Object whereValues[][],Query q){
		Query rez = q;
        if (whereValues==null) return q;
		for (int i=0;i<whereValues.length;i++){
			if (whereValues[i]!=null){
				rez = rez.setParameterList(PROP_NAME_PREFIX+i, whereValues[i]);
			}
		}
		return rez;
	}

	/**
	 * setParameter to given query use it if query was created using
	 *  getWhereManyColumnsManyValuesRelations function
	 * @param whereValues values to set as parameters
	 * @param q query where to set parameters
	 * @return query with setted parameters
	 */
	public static Query appendtWhereManyColumnsManyValuesRelations(Object whereValues[][],Query q){
		Query rez = q;
        if (whereValues==null) return q;
		for (int i=0;i<whereValues.length;i++){
			for (int j=0;j<whereValues[i].length;j++){
				if (whereValues[i][j]!=null){
					rez = rez.setParameter(PROP_NAME_PREFIX+i+PROP_NAME_DELIMITER+j, whereValues[i][j]);
				}
			}
		}
		return rez;
	}
}
