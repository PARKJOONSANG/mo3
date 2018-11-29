package com.ex.group.mail.widget;

import com.skt.pe.util.Base64Util;

import java.net.URL;
import java.security.MessageDigest;

public class BasicAuthController {
//    public static void main(String[] args) throws Exception {
//			  System.out.println("#################################################");
//				System.out.println("URL : "+BasicAuthController.getEcmURL());
//        System.out.println("#################################################");
//    }
    
    public static String getEcmURL() throws Exception {

        String userId = "sihyeon";
        String appKey = "5567f7c89d410fac5edd7e78fadc622e";
        String documentId = "0900000a801a6dff";
        String addr = "http://ecm.cj.net/cjecm/downloadAttachments.action";

        URL url = new URL(addr + "?userId=" + userId + "&documentId=" + documentId);

        String auth = BasicAuthController.getAuthorization(userId, appKey);
        return url.toString()+"&credential="+auth;
    }
    
    public static String getAuthorization(final String userId, final String appKey) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(userId).append(':');
        buffer.append(BasicAuthController.generateCredential(userId, appKey));
        String base64Encoded = Base64Util.encodeBinary(buffer.toString().getBytes("UTF-8"));
				// System.out.println("credential='"+base64Encoded+"'");

        return base64Encoded;
    }

    public static String generateCredential(final String userId, final String appKey) throws Exception {
        long nonce = System.currentTimeMillis() / 1000L;
        String credential = nonce + userId + appKey;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(credential.getBytes("UTF-8"));
        
        return nonce + ";" + Base64Util.encodeBinary(md.digest());
    }
}