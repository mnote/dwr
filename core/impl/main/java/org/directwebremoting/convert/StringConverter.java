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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.JavascriptUtil;

/**
 * An implementation of Converter for Strings.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class StringConverter extends AbstractConverter
{
    private static Gson gson = (new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().setPrettyPrinting().serializeSpecialFloatingPointValues().setLenient()).create();

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        if (data.getFormField().isFile())
        {
            // Data from file uploads is not URL encoded
            return data.getValue();
        }
        else
        {
            return data.urlDecode();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {

        if(outctx.isJsonMode()) {
            return new NonNestedOutboundVariable(gson.toJson(data));
            //return new NonNestedOutboundVariable('\"' + data.toString().replace("\"", "\\\"").replace("\\", "\\\\").replace("\n", "").replace("\b", "").replace("\f", "").replace("\r", "").replace("\t", "") + '\"');
        }

        String escaped = JavascriptUtil.escapeJavaScript(data.toString(), false);
        return new NonNestedOutboundVariable('\"' + escaped + '\"');
    }
}
