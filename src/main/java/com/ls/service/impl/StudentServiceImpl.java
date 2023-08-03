package com.ls.service.impl;

import com.google.common.base.Joiner;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.Shape;
import com.ls.dao.StudentDao;
import com.ls.domain.Student;
import com.ls.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.CheckedOutputStream;

//@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    private Logger logger = Logger.getLogger(StudentServiceImpl.class);


    @Override
    public Student getAll() {
        return null;
    }

    @Override
    public void generatorAdmissionCard(/*Student student,*/ HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException {
        //模板名称
        String templateName ="toPDF.pdf";
        String path = "";
        String systemName = System.getProperty("os.name");
        if (systemName.toUpperCase().startsWith("WIN")){
            path = "D:/Java/code/output_pdf_01/src/main/resources/file/";
        }else {
            path = "src/main/resources/file/";
        }
        //生成导出PDF的文件名称
//        String encode = URLEncoder.encode(student.getName());

        String fileName = /*student.getName() +*/ "-学生信息.pdf";

        fileName = URLEncoder.encode(fileName,"UTF-8");
        //设置响应头
//        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + fileName);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/pdf");

        OutputStream out = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper = null;
        PdfReader reader = null;
        try {
            //保存到本地
//            out = new FileOutputStream(fileName);
            //输出到浏览器端
            out = response.getOutputStream();
            //读取PDF模板表单
            reader = new PdfReader(path + templateName);
            //字节数组流，用来缓存文件流
            bos = new ByteArrayOutputStream();
            //根据模板表单生成一个新的PDF
            stamper = new PdfStamper(reader,bos);
            //获取新生成的PDF表单
            AcroFields form = stamper.getAcroFields();
            BaseFont font = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            form.addSubstitutionFont(font);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String time = simpleDateFormat.format(System.currentTimeMillis());
            System.out.println(time);
            String text= "JamL" +"/"+ time;


            addWatermark(reader,stamper,text);


            //装配数据
            List<Student> data1 = studentDao.getAll();
            System.out.println(data1);
//            List<String> nameList = new ArrayList<>();

            /*List<String> genderList = new ArrayList<>();
            List<String> studentIDList = new ArrayList<>();
            List<String> phoneList = new ArrayList<>();
            List<String> psList = new ArrayList<>();*/
            /*for (Student student : data1) {
                nameList.add(student.getName());
                genderList.add(student.getGender());
                studentIDList.add(student.getStudentID());
                phoneList.add(student.getPhone());
                psList.add(student.getPs());
            }*/
//            String join = Joiner.on(",").join(nameList);

            HashMap<String, Object> data = new HashMap<>(15);
            LinkedHashMap<String,Object> data11 = new LinkedHashMap<>();
            for (Student student : data1) {
                data.put("name",student.getName());
                data.put("gender",student.getGender());
                data.put("studentID",student.getStudentID());
                data.put("phone",student.getPhone());
                data.put("ps",student.getPs());
            }
            /*data.put("name",student.getName());
            data.put("gender",student.getGender());
            data.put("studentID",student.getStudentID());
            data.put("phone",student.getPhone());
            data.put("ps",student.getPs());*/
            /*for (Student student1 : data1) {
                data.put("name",student1.getName());
                data.put("gender",student1.getGender());
                data.put("studentID",student1.getStudentID());
                data.put("phone",student1.getPhone());
                data.put("ps",student1.getPs());
            }*/
            /*data.put("name",nameList);
            data.put("gender",genderList);
            data.put("studentID",studentIDList);
            data.put("phone",phoneList);
            data.put("ps",psList);*/


            //遍历data，给pdf表单赋值
            for (String key:data.keySet() ){
                System.out.println(key + data.get(key) + "\n");
                form.setField(key,data.get(key).toString(),true);
            }
            /*for (String key: data11.keySet()) {
                form.setField(key,data11.get(key).toString());
            }*/
            /*for (Map.Entry<String, Object> entry : data.entrySet()) {
                System.out.println(entry.getKey() +  "=============" + entry.getValue());
            }*/

//            表明该pdf不可修改
            stamper.setFormFlattening(true);

            //关闭资源
            stamper.close();
            reader.close();

            //将ByteArray字节数组中的流输出到out中(浏览器)
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importedPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importedPage);


            doc.close();
            logger.info("***************************PDF导出成功*******************************");


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if (out !=null){
                    out.flush();
                    out.close();
                }
                if (reader != null){
                    reader.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    /**
     * 添加水印
     * @param reader
     * @param stamper
     * @param text 水印内容
     * @throws DocumentException
     * @throws IOException
     */
    @Override
    public void addWatermark(PdfReader reader,PdfStamper stamper,String text) throws DocumentException, IOException {
        //加水印
            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content;
            //设置透明度
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f);
            gs.setTextKnockout(false);
            //设置字体
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            //循环对每页插入水印
            for (int i = 1; i < total; i++) {
                //水印的起始
                content = stamper.getOverContent(1);
                content.setGState(gs);
                content.setFontAndSize(base, 20);
                //开始
                content.beginText();
                //设置颜色 默认为黑色
                content.setColorFill(BaseColor.BLACK);
                //开始写入水印
                content.showTextAligned(Element.ALIGN_MIDDLE, text, 198, 400, 30);
                content.showTextAligned(Element.ALIGN_MIDDLE, text, 100, 400, 30);
                content.showTextAligned(Element.ALIGN_MIDDLE, text, 50, 421, 30);
                content.showTextAligned(Element.ALIGN_MIDDLE, text, 80, 500, 30);
                content.showTextAligned(Element.ALIGN_MIDDLE, text, 100, 550, 30);
                content.showTextAligned(Element.ALIGN_MIDDLE, text, 150, 221, 30);
                content.endText();
            }


    }
}
