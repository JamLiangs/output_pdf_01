package com.ls.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.ls.dao.CarDao;
import com.ls.domain.Car;
import com.ls.service.CarService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarDao carDao;


    private Logger logger = Logger.getLogger(CarServiceImpl.class);

    private static int interval = 10;


    @Override
    public void generatorAdmissionCard(HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException {
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
        String fileName = "car.pdf";

        fileName = URLEncoder.encode(fileName,"UTF-8");
        //设置响应头
        response.setHeader("Content-Disposition","attachment;fileName=" + fileName);
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
//            BaseFont font = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            form.addSubstitutionFont(base);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String time = simpleDateFormat.format(System.currentTimeMillis());
            System.out.println(time);
            String text= "JamL" +"/"+ time;

            //添加水印
            addWatermark(reader,stamper,text);

            //装配数据
            List<Car> data = carDao.getAll();
            System.out.println(data);
            //遍历data，给表单赋值
            for (Car car : data) {
                form.setField("SeagullsRow" + car.getId(),car.getSeagullsRow());
                form.setField("DefinitionRow" + car.getId(),car.getDefinitionRow());
                form.setField("DataphinRow" + car.getId(),car.getDataphinRow());
                form.setField("fill" + car.getId(),car.getFill());
            }


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

            logger.info("***************************PDF导出成功*******************************");
            doc.close();


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

    @Override
    public void addWatermark(PdfReader reader, PdfStamper stamper, String text) throws DocumentException, IOException {
        //加水印
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        Rectangle pageRect = null;
        //设置透明度
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.3f);
        gs.setTextKnockout(false);
        //设置字体
        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        //循环对每页插入水印
        for (int i = 1; i < total; i++) {
            //水印的起始
            // 获得PDF最顶层
            content = stamper.getOverContent(i);
            content.saveState();
            content.setGState(gs);

            //开始
            content.beginText();
            //设置字体和大小
            content.setFontAndSize(base, 15);
            //设置颜色 默认为黑色
            content.setColorFill(BaseColor.BLACK);
            pageRect = reader.getPageSizeWithRotation(i);

            JLabel label = new JLabel();
            FontMetrics metrics;
            //设置偏移量
            int textH = 0;
            int textW = 0;
            label.setText(text);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());
            //循环写入水印
            for (int height = interval + textH; height < pageRect.getHeight();height = height + textH*14) {
                for (int width = interval + textW; width < pageRect.getWidth() + textW; width = width + textW*2) {
                    content.showTextAligned(Element.ALIGN_LEFT, text, width - textW, height - textH, 30);
                }
            }
            //结束写入
            content.endText();
        }

    }
}
