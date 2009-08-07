/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.module.servlet.image;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author mag
 * @Date 14 févr. 2009
 */
public class SimpleImageCaptchaServlet extends HttpServlet implements Servlet
 {
    public static ImageCaptchaService service= new DefaultManageableImageCaptchaService();



     @Override
     protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
              // Set to expire far in the past.
                    httpServletResponse.setDateHeader("Expires", 0);
                    // Set standard HTTP/1.1 no-cache headers.
                    httpServletResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
                    // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
                    httpServletResponse.addHeader("Cache-Control", "post-check=0, pre-check=0");
                    // Set standard HTTP/1.0 no-cache header.
                    httpServletResponse.setHeader("Pragma", "no-cache");

                    // return a jpeg
                    httpServletResponse.setContentType("image/jpeg");


                    // create the image with the text
                    BufferedImage bi = service.getImageChallengeForID(httpServletRequest.getSession(true).getId());

                    ServletOutputStream out = httpServletResponse.getOutputStream();

                    // write the data out
                    ImageIO.write(bi, "jpg", out);
                    try
                    {
                            out.flush();
                    }
                    finally
                    {
                            out.close();
                    }
    }


     public static boolean validateResponse(HttpServletRequest request, String userCaptchaResponse){
         //if no session found
         if(request.getSession(false)==null)return false;
         //else use service and session id to validate
         boolean validated = false;
         try {
             validated = service.validateResponseForID(request.getSession().getId(),userCaptchaResponse);
         } catch (CaptchaServiceException e) {
           //do nothing.. false
         }
         return validated;
     }
 }
