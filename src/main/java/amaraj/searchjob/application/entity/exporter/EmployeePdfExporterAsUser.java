package amaraj.searchjob.application.entity.exporter;

import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
import amaraj.searchjob.application.entity.Employee;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class EmployeePdfExporterAsUser extends AbstractExporter{
     public void export(PageDTO<EmployeeDTO> employeeList, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "application/pdf", ".pdf");

        Document document = new Document(PageSize.A2);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("List of Employees", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        //krijojme tabelen //ose rreshtat
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1.2f, 2.0f, 2.0f, 3.5f, 1.2f});

        writeTableHeader(table);
        writeTableData(table, employeeList);

        document.add(table);
        document.close();

    }

    private void writeTableData(PdfPTable table, PageDTO<EmployeeDTO> employeeList) {
        for (EmployeeDTO emp: employeeList.getContent()){
            table.addCell(String.valueOf(emp.getJobSeekerId()));
            table.addCell(emp.getName());
            table.addCell(emp.getSurname());
            table.addCell(emp.getEmail());
            table.addCell(String.valueOf(emp.getYearsOfExp()));
          //  table.addCell(emp.getFileOfCv());
        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(12);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Years of Exp", font));
        table.addCell(cell);
    //    cell.setPhrase(new Phrase("Cv", font));
     //   table.addCell(cell);
//        cell.setPhrase(new Phrase("City", font));
//        table.addCell(cell);
//
//        cell.setPhrase(new Phrase("Postal Code", font));
//        table.addCell(cell);
    }
}
