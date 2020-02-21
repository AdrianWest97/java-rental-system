package operations;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Tenant;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wests
 */
public class GenerateReciept {

    public static String GenerateReciept(Tenant tenant, double amount) {
        Document document = new Document();
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, YYYY");
        GregorianCalendar calendar = new GregorianCalendar();

        String generate_name = "C://TMS/Reciept/" + tenant.getContact().getTRN() + "/Reciept_" + tenant.getContact().getTRN() + ".pdf";
        File f = new File(generate_name);
        DecimalFormat df = new DecimalFormat(".##");
        Font darkBig = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new CMYKColor(73, 75, 0, 0));
        Font darkMedium = FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD);
        Font darkSmall = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD);

        if (!f.exists()) {
            try {
                f.getAbsoluteFile().getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(GenerateReciept.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            try {
                //filename
                ArrayList<String> attri = new ArrayList<>();

                attri.add("Last Payment:    " + format.format(tenant.getPayment().getLastPayDate()));
                attri.add("Amount payed:  $ " + df.format(amount));

                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(f));
                document.open();
                Paragraph p = new Paragraph();
                p.setAlignment(Paragraph.ALIGN_CENTER);
                p.setFont(darkBig);
                p.add("TENANT MANGEMENT SYSTEM");
                document.add(Chunk.NEWLINE);
                document.add(p);
                Paragraph p2 = new Paragraph();
                p.setAlignment(Paragraph.ALIGN_LEFT);
                document.add(Chunk.NEWLINE);
                p2.add("Generated: " + format.format(calendar.getTime()));
                document.add(p2);
                document.add(new Paragraph("Account #: " + tenant.getContact().getTRN()));
                document.add(Chunk.NEWLINE);

                Paragraph p1 = new Paragraph();
                p1.setAlignment(Paragraph.ALIGN_CENTER);
                p1.setFont(darkMedium);
                p1.add("Rent reciept for " + tenant.getFirstName() + " " + tenant.getLastName());
                document.add(Chunk.NEWLINE);
                document.add(p1);
                ListIterator itr = attri.listIterator();
                while (itr.hasNext()) {
                    document.add(new Paragraph(itr.next().toString(), darkSmall));
                }
                document.add(new Paragraph("Change:          $ " + df.format(tenant.getPayment().getChange()), darkMedium));
                document.add(new Paragraph());
                ArrayList<String> attr2 = new ArrayList<>();
                attr2.add("Next Payment date:   " + format.format(tenant.getPayment().getNextPayDate()));
                attr2.add("Oustanding amount:   $ " + df.format(tenant.getPayment().getOutsAmt()));
                attr2.add("Discount:            $ " + df.format(tenant.getPayment().getDiscAmount()));
                attr2.add("Total days late:       " + tenant.getPayment().getTotalDaysLate());
                attr2.add("Total late Fee:      $ " + tenant.getPayment().getLateFee());
                ListIterator itr2 = attr2.listIterator();
                while (itr2.hasNext()) {
                    document.add(new Paragraph(itr2.next().toString(), darkSmall));
                }
                document.add(new Paragraph("Amount Due:  $ " + df.format(tenant.getPayment().getAmountDue()), darkMedium));

                document.addCreationDate();
                document.addSubject("Rent Receipt");
                document.addHeader("Tenant Mangement System", "TMS sys");
                document.close();
                writer.close();
                return generate_name;
            } catch (DocumentException ex) {
                Logger.getLogger(GenerateReciept.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenerateReciept.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
