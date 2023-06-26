package com.effi.EffiApp.utils.pdfs;

import com.effi.EffiApp.registration.employee.EmployeeRegistrationObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PdfGeneration {
    private static String[] headTitles = {"First name", "Last name", "Email", "Password"};
    public static ByteArrayInputStream generateNewEmployeeAccountInfo(EmployeeRegistrationObject employee){
        Document doc = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //create table with 4 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(90);

        //get fonts for table cells
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA);

        //get employee info for the file
        String[] employeeInfo = {employee.getFirstName(), employee.getLastName(), employee.getEmail(),
                employee.getPassword()};

        PdfPCell cell;

        //add column titles
        for(String s: headTitles){
            cell = new PdfPCell(new Phrase(s, headFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        //add one row with employee info
        for(String s: employeeInfo){
            cell = new PdfPCell(new Phrase(s, normalFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        try {
            PdfWriter.getInstance(doc, out);
            doc.open();
            doc.add(table);
            doc.close();
        } catch (DocumentException e){
            return null;
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
