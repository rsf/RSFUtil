/*
 * Created on 7 Mar 2008
 */
package uk.org.ponder.rsf.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.conversion.GeneralConverter;
import uk.org.ponder.conversion.TypeDecodable;
import uk.org.ponder.json.support.JSONProvider;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.rsf.content.ContentTypeInfo;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.view.DataInputHandler;
import uk.org.ponder.rsf.view.DataView;
import uk.org.ponder.rsf.view.DataViewHandler;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.streamutil.StreamCopyUtil;
import uk.org.ponder.streamutil.read.StringRIS;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.UniversalRuntimeException;

public class DataViewHandlerImpl implements DataViewHandler {
  private HttpServletRequest requestProxy;
  private HttpServletResponse responseProxy;

  private GeneralConverter generalConverter;
  
  private JSONProvider jsonprovider;

  private ContentTypeInfo contentTypeInfoProxy;

  public void setRequestProxy(HttpServletRequest requestProxy) {
    this.requestProxy = requestProxy;
  }

  public void setResponseProxy(HttpServletResponse responseProxy) {
    this.responseProxy = responseProxy;
  }


  public void setGeneralConverter(GeneralConverter generalConverter) {
    this.generalConverter = generalConverter;
  }

  public void setJSONProvider(JSONProvider jsonprovider) {
    this.jsonprovider = jsonprovider;
  }
  
  public void setContentTypeInfoProxy(ContentTypeInfo contentTypeInfoProxy) {
    this.contentTypeInfoProxy = contentTypeInfoProxy;
  }

  public void handleView(DataView view, ViewParameters viewparams) {
    ContentTypeInfo cti = contentTypeInfoProxy.get();
    if (!cti.typename.equals(ContentTypeInfoRegistry.CUSTOM)) {
      responseProxy.setContentType(cti.contentTypeHeader);
    }
    Object data = view.getData(viewparams);

    try {
      OutputStream os = responseProxy.getOutputStream();
      String rendered = null;
      byte[] bytes = null;
      InputStream is = null;
      if (cti.typename.equals(ContentTypeInfoRegistry.CUSTOM)) {
        if (data instanceof InputStream) {
          is = (InputStream) data;
        }
        else if (data instanceof String) {
          rendered = (String) data;
        }
        else if (data instanceof byte[]) {
          bytes = (byte[]) data;
        }
      }
      else if (cti.typename.equals(ContentTypeInfoRegistry.AJAX)) {
        rendered = generalConverter.render(data, DataAlterationRequest.XML_ENCODING);
      }
      else if (cti.typename.equals(ContentTypeInfoRegistry.JSON)) {
        rendered = generalConverter.render(data, DataAlterationRequest.JSON_ENCODING);
      }

      if (rendered != null) {
        bytes = rendered.getBytes("UTF-8");
      }
      if (bytes != null) {
        is = new ByteArrayInputStream(bytes);
      }
      if (is == null) {
        throw new UnsupportedOperationException("Unrecognised data from dataView of "
            + data.getClass());
      }
      StreamCopyUtil.inputToOutput(is, os, new byte[4096]);
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e, "Error rendering data view");
    }
  }


  public void handleInput(DataInputHandler handler, ViewParameters viewparams) {
    String method = requestProxy.getMethod();
    String[] handled = StringList.fromString(handler.getHandledMethods().toUpperCase()).toStringArray();
    try {
    if (!ArrayUtil.contains(handled, method)) {

        responseProxy.sendError(405, "Method not allowed");
        responseProxy.addHeader("Allow", handler.getHandledMethods());
      }
    else {
      String ct = handler.getContentType();
      InputStream is = requestProxy.getInputStream();
      //String is = ((String [])requestProxy.getParameterMap().get("q"))[0];
      if (ct.equals(ContentTypeInfoRegistry.CUSTOM)) {
        handler.handleInput(viewparams, method, is);
      }
      else if (ct.equals(ContentTypeInfoRegistry.JSON)) {
        Object classorobj = null;
        if (handler instanceof TypeDecodable) {
          classorobj = ((TypeDecodable)handler).getDecodeTarget();
        }
        if (classorobj == null) {
          classorobj = new HashMap();
        }
        Object decode = jsonprovider.readObject(classorobj, is);
        handler.handleInput(viewparams, method, decode);
      }
    }
  }
    catch (IOException e) {
      throw UniversalRuntimeException.accumulate(e, "Error handling data input handler");
    }
    
  }
}
