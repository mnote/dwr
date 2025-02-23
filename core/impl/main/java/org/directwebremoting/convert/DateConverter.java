/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.convert;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.ProtocolConstants;

/**
 * An implementation of Converter for Dates.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DateConverter extends AbstractConverter
{

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        String value = data.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
        {
            return null;
        }

        try
        {
            long millis = 0;
            if (value.length() > 0)
            {

                value = value.replace("%20", " ");
                value = value.replace("%3A", ":");

                if(value.length() == 23 && !value.contains("T")){
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        Date time = dateFormat.parse(value);
                        millis = time.getTime();
                    }catch (Exception e){

                    }
                }

                if(value.length() == 23 && value.contains("T")){
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        Date time = dateFormat.parse(value);
                        millis = time.getTime();
                    }catch (Exception e){

                    }
                }

                if(value.length() == 19){
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date time = dateFormat.parse(value);
                        millis = time.getTime();
                    }catch (Exception e){

                    }
                }

                if(value.length() == 17){
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
                        Date time = dateFormat.parse(value);
                        millis = time.getTime();
                    }catch (Exception e){

                    }
                }

                if(value.length() == 21){
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss.SSS");
                        Date time = dateFormat.parse(value);
                        millis = time.getTime();
                    }catch (Exception e){

                    }
                }

                if(millis == 0) {
                    millis = Long.parseLong(value);
                }
            }

            Date date = new Date(millis);
            if (paramType == Date.class)
            {
                return date;
            }
            else if (paramType == java.sql.Date.class)
            {
                return new java.sql.Date(date.getTime());
            }
            else if (paramType == Time.class)
            {
                return new Time(date.getTime());
            }
            else if (paramType == Timestamp.class)
            {
                return new Timestamp(date.getTime());
            }
            else if (paramType == Calendar.class)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal;
            }
            else
            {
                throw new ConversionException(paramType);
            }
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(paramType, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {

        long millis;

        if (data instanceof Calendar)
        {
            Calendar cal = (Calendar) data;
            millis = cal.getTime().getTime();
        }
        else if (data instanceof Date)
        {
            Date date = (Date) data;
            millis = date.getTime();
        }
        else
        {
            throw new ConversionException(data.getClass());
        }

        if(outctx.isJsonMode()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return new NonNestedOutboundVariable("\"" + dateFormat.format(new Date(millis)) + "\"");
        }

        return new NonNestedOutboundVariable("new Date(" + millis + ")");
    }
}
