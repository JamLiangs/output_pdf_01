package com.ls.service;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.ls.controller.StudentController;
import com.ls.domain.Student;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface StudentService {

    public Student getAll();

    void generatorAdmissionCard(/*Student student,*/ HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException;

    void addWatermark(PdfReader reader, PdfStamper stamper, String WaterMarkContext) throws DocumentException, IOException;


}
