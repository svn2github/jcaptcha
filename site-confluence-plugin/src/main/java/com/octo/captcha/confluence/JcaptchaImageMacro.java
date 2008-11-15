package com.octo.captcha.confluence;

import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.BaseMacro;
import com.atlassian.renderer.v2.macro.MacroException;
import com.atlassian.renderer.v2.macro.code.formatter.XmlFormatter;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.confluence.renderer.PageContext;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.engine.image.ImageCaptchaEngine;


import java.util.Map;
import java.util.HashMap;
import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.apache.commons.lang.StringEscapeUtils;


/**
 * @author mag
 * @Date 13 nov. 2008
 */
public class JcaptchaImageMacro extends BaseMacro  {

    protected static HashMap engineRegistry = new HashMap();
    private XmlFormatter formater = new XmlFormatter();

    public boolean isInline() {
        return true;
    }

    public boolean hasBody() {
        return true;
    }

    public RenderMode getBodyRenderMode() {
        return RenderMode.NO_RENDER;
    }

    public boolean suppressSurroundingTagDuringWysiwygRenderering() {
        return true;
    }

 	public String execute(Map params, String body, RenderContext renderContext) throws MacroException {
         // firstly, return a very simple version for WYSIWYG
        if (renderContext.isRenderingForWysiwyg())
        {
          return body;
        }

        String config = body.trim();
        Integer configHash = new Integer(config.hashCode());
        config="<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\">\n" +
                "<beans>"+
                config+
                "</beans>";
        if(!engineRegistry.containsKey(configHash)){
            try {
                Resource resource = new ByteArrayResource(config.getBytes());
                XmlBeanFactory bf = new XmlBeanFactory(resource);
                Object factory= bf.getBean("imageCaptchaFactory");
                //GenericCaptchaEngine engine = new GenericCaptchaEngine(new ImageCaptchaFactory[]{factory});
                engineRegistry.put(configHash,factory);
            } catch (Throwable e) {
               throw new MacroException(e);
            }
        }

        try {

            if (renderContext instanceof PageContext) {

            	PageContext pageContext = (PageContext) renderContext;
                params.put("space", pageContext.getSpaceKey());
                params.put("page", pageContext.getPageTitle());
                params.put("body", body);
                params.put("config", formater.format(StringEscapeUtils.escapeXml(body),"xml"));
                params.put("configHash", configHash);

            } else {
                throw new MacroException("Cannot render images without pagecontext");
            }

            Map contextMap = MacroUtils.defaultVelocityContext();
            contextMap.putAll(params);

            return VelocityUtils.getRenderedTemplate("/jcaptchaimage.vm", contextMap);
        } catch (Exception e) {
            throw new MacroException(e.getMessage(), e);
        }
         
    }


}
