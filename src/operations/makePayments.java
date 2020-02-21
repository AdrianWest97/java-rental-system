package operations;

import entities.Tenant;
import entities.CalculatePayments;
import interfaces.ChargeInterface;
import java.util.Calendar;
import java.util.GregorianCalendar;
import interfaces.ProgramFiles;

/**
 *
 * @author wests
 */
public class makePayments extends CalculatePayments {

    public boolean makePayments(Tenant tNew, double pay) {
        //search firts
        CrudOperations crud = new CrudOperations();

        GregorianCalendar gc = new GregorianCalendar();

        GregorianCalendar now = new GregorianCalendar();

        if (tNew != null) {
            if (crud.checkIFExists(tNew.getContact().getEmail(), ProgramFiles.TENANT_FILE)) {
                //user found, make pyements
                //get current amount due
                double amtDue = tNew.getPayment().getAmountDue();
                //get outstanding amount
                double ouStandAmt = tNew.getPayment().getOutsAmt();

                double outTemp = 0;

                double changeTemp = 0;

                Tenant nTenant = null;

                CalculatePayments p = new CalculatePayments();

                gc.setTime(tNew.getPayment().getNextPayDate());

                boolean onTime = false;
                boolean before = false;

                //if the user pays on time
                if (gc.get(GregorianCalendar.DAY_OF_WEEK) == now.get(GregorianCalendar.DAY_OF_WEEK)
                        && gc.get(GregorianCalendar.MONTH) == now.get(GregorianCalendar.MONTH)) {
                    if (gc.get(GregorianCalendar.YEAR) == now.get(GregorianCalendar.YEAR)) {
                        onTime = true;
                    }
                }

                //pay before due date
                if (now.getTime().before(gc.getTime())) {
                    before = true;
                }

                if (onTime) {
                    if (pay >= amtDue) {
                        changeTemp = pay - amtDue;
                        tNew.getPayment().setChange(changeTemp);
                        tNew.getPayment().setOutsAmt(0);
                    } else if (pay < amtDue) {
                        outTemp = amtDue - pay;
                        tNew.getPayment().setOutsAmt(outTemp);
                        tNew.getPayment().setChange(0);
                    }
                    tNew.getPayment().setTotalDaysLate(0);
                    tNew.getPayment().setLateFee(0);
                    tNew.getPayment().setLastPayDate(tNew.getPayment().getNextPayDate());
                    nTenant = p.calAmtDue(tNew);
                    nTenant.getPayment().setTotalDaysLate(0);
                    nTenant.getPayment().setLateFee(0);
                    //update
                    return crud.updateFile(nTenant, nTenant.getContact().getEmail());
                } else if (before) {
                    if (pay >= amtDue) {
                        changeTemp = pay - amtDue;
                        tNew.getPayment().setChange(changeTemp);
                        tNew.getPayment().setOutsAmt(0);
                        tNew.getPayment().setAmountDue(0);
                    } else if (pay < amtDue) {
                        outTemp = amtDue - pay;
                        tNew.getPayment().setOutsAmt(outTemp);
                        tNew.getPayment().setChange(0);
                        tNew.getPayment().setAmountDue(ouStandAmt + outTemp);
                    }
                    tNew = p.calAmtDue(tNew);
                    tNew.getPayment().setLastPayDate(now.getTime());
                    gc.add(Calendar.DAY_OF_WEEK, ChargeInterface.BILLING_CYCLE);
                    tNew.getPayment().setNextPayDate(gc.getTime());
                    tNew.getPayment().setTotalDaysLate(0);
                    tNew.getPayment().setLateFee(0);
                    //update
                   return crud.updateFile(tNew, tNew.getContact().getEmail());
                } else if (!before && !onTime) {
                    //late payment
                    if (pay >= amtDue) {
                        changeTemp = pay - amtDue;
                        tNew.getPayment().setChange(changeTemp);
                        tNew.getPayment().setOutsAmt(0);
                        tNew.getPayment().setAmountDue(0);
                    } else if (pay < amtDue) {
                        outTemp = amtDue - pay;
                        tNew.getPayment().setOutsAmt(outTemp);
                        tNew.getPayment().setChange(0);
                        tNew.getPayment().setAmountDue(ouStandAmt + outTemp);
                    }
                   
                    return crud.updateFile(p.calAmtDue(tNew), tNew.getContact().getEmail());

                }
            }
        }
        return false;
    }
}
