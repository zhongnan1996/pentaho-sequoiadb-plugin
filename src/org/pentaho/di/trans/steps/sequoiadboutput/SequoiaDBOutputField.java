/**
 *      Copyright (C) 2012 SequoiaDB Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.pentaho.di.trans.steps.sequoiadboutput;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.i18n.BaseMessages;

public class SequoiaDBOutputField{

   private static Class<?> PKG = SequoiaDBOutputMeta.class;

   public String m_fieldName = "" ;

   public String m_path = "" ;
   
   public List<String> m_pathField = null;

   public Object getBsonValue( Object input, ValueMetaInterface vmi ) throws KettleValueException {
      if ( vmi.isNull( input ) ) {
         return null ;
      }
      Object retObj ;
      if ( vmi.isString() ) {
         retObj = vmi.getString( input ) ;
         return retObj ;
      }
      if ( vmi.isBoolean() ) {
         retObj = vmi.getBoolean( input );
         return retObj ;
      }
      if ( vmi.isInteger() ) {
         retObj = vmi.getInteger( input ) ;
         return retObj ;
      }
      if ( vmi.isDate() ) {
         retObj = vmi.getDate( input ) ;
         return retObj ;
      }
      if ( vmi.isNumber() ) {
         retObj = vmi.getNumber( input ) ;
         return retObj ;
      }
      if ( vmi.isBigNumber() ) {
         retObj = vmi.getString( input ) ;
         return retObj ;
      }
      if ( vmi.isBinary() ) {
         retObj = vmi.getBinary( input ) ;
         return retObj ;
      }

      throw new KettleValueException( BaseMessages.getString( PKG,
            "SequoiaDBOutput.Msg.Err.FailedToGetTheFieldVal" ) );
   }
   
   public void splitPath() {

      m_pathField = new ArrayList<String>();
      int begin = 0;
      int dotPos = 0;
      if( null == m_path ) {
         return ;
      }     
      m_pathField = new ArrayList<String>();
      int length = m_path.length();
      while (begin < length ) {

         dotPos = m_path.indexOf('.', begin);
         if ( -1 == dotPos ) {
            m_pathField.add(m_path.substring(begin));
            break;
         }
         else {
            String str = m_path.substring(begin, dotPos);
            m_pathField.add(str);
            begin = dotPos + 1;
         }
      }
   }
}
