package com.drx.leave_manager.service;

import com.drx.leave_manager.model.LeaveRequest;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdfExportService {

    public void exportLeaveRequestToPdf(LeaveRequest request, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("CERERE DE CONCEDIU", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

        Font fontContent = FontFactory.getFont(FontFactory.HELVETICA, 12);

        String employeeName = (request.getEmployee() != null) ? request.getEmployee().getName() : "N/A";
        Integer employeeId = (request.getEmployee() != null) ? request.getEmployee().getEmplId() : null;
        String leaveTypeName = (request.getLeaveType() != null) ? request.getLeaveType().getName() : "N/A";

        document.add(new Paragraph("Nume Angajat: " + employeeName, fontContent));
        document.add(new Paragraph("ID Angajat: " + employeeId, fontContent));
        document.add(new Paragraph("Tip Concediu: " + leaveTypeName, fontContent));
        document.add(new Paragraph("Data Inceput: " + request.getStartDate(), fontContent));
        document.add(new Paragraph("Data Sfarsit: " + request.getEndDate(), fontContent));
        document.add(new Paragraph("Zile Lucratoare: " + request.getWorkingDays(), fontContent));
        document.add(new Paragraph("Status Cerere: " + request.getStatus(), fontContent));

        document.close();
    }
}