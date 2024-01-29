package de.swtpro.factorybuilder.service;


import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ImgConverterService {


    public byte[] dataUrlToByteArray(String dataUrl) {
        String base64Part = dataUrl.split(",")[1];
        return Base64.getDecoder().decode(base64Part);
    }

    public String byteArrayToDataUrl(byte[] byteArray, String mediaType) {
        String base64String = Base64.getEncoder().encodeToString(byteArray);
        return "data:" + mediaType + ";base64," + base64String;
    }


}
