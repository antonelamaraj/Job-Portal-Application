package amaraj.searchjob.application.entity.exporter;

import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.dto.PageDTO;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;

public class CompanyPdfExporter extends AbstractExporter{


    public void export(PageDTO<CompanyDto> compList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "companies/pdf", ".pdf");

        Document document = new Document(PageSize.A2);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("List of Companies", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        //krijojme tabelen //ose rreshtat
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
     //   table.setWidths(new float[]{1.2f, 2.0f, 2.0f, 3.5f, 1.2f, 1.5f});

        writeTableHeader(table);
        writeTableData(table, compList);

        document.add(table);
        document.close();

    }

    private void writeTableData(PdfPTable table, PageDTO<CompanyDto> employeeList) {
        for (CompanyDto emp: employeeList.getContent()){
            table.addCell(String.valueOf(emp.getId()));
            table.addCell(emp.getNipt());
            table.addCell(emp.getName());
            table.addCell(emp.getDescription());
            table.addCell(emp.getWebsite());
            table.addCell(emp.getContactInformation());
            table.addCell(emp.getCategory().name());
            table.addCell(String.valueOf(emp.getOpenAt()));
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
        cell.setPhrase(new Phrase("Nipt", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Website", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Contact Info", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Category", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Open At", font));
        table.addCell(cell);
    }

}
