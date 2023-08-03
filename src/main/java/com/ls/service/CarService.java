package com.ls.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.ls.domain.Car;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public interface CarService {

    void generatorAdmissionCard(/*Student student,*/ HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException;

    void addWatermark(PdfReader reader, PdfStamper stamper, String WaterMarkContext) throws DocumentException, IOException;
}
