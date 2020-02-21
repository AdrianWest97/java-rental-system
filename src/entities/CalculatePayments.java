package entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import interfaces.ChargeInterface;
import interfaces.PaymentInterface;

public class CalculatePayments implements Serializable, PaymentInterface {

    private double amountDue;
    private Date nextPayDate;
    private Date lastPayDate;
    private double lateFee;
    private int totalDaysLate;
    private double discAmount;
    private double change;
    private double outsAmt;
   // private static final long serialVersionUID = -1165631782062223824L;

    /**
     * @return the amountDue
     */
    @Override
    public double getAmountDue() {
        return amountDue;
    }

    /**
     * @param amountDue the amountDue to set
     */
    @Override
    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    /**
     * @return the nextPayDate
     */
    @Override
    public Date getNextPayDate() {
        return nextPayDate;
    }

    /**
     * @param nextPayDate the nextPayDate to set
     */
    @Override
    public void setNextPayDate(Date nextPayDate) {
        this.nextPayDate = nextPayDate;
    }

    /**
     * @return the lastPayDate
     */
    @Override
    public Date getLastPayDate() {
        return lastPayDate;
    }

    /**
     * @param lastPayDate the lastPayDate to set
     */
    @Override
    public void setLastPayDate(Date lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    /**
     * @return the lateFee
     */
    @Override
    public double getLateFee() {
        return lateFee;
    }

    /**
     * @param lateFee the lateFee to set
     */
    @Override
    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    /**
     * @return the totalDaysLate
     */
    @Override
    public int getTotalDaysLate() {
        return totalDaysLate;
    }

    /**
     * @param totalDaysLate the totalDaysLate to set
     */
    @Override
    public void setTotalDaysLate(int totalDaysLate) {
        this.totalDaysLate = totalDaysLate;
    }

    /**
     * @return the discAmount
     */
    @Override
    public double getDiscAmount() {
        return discAmount;
    }

    /**
     * @param discAmount the discAmount to set
     */
    @Override
    public void setDiscAmount(double discAmount) {
        this.discAmount = discAmount;
    }

    /**
     * @return the change
     */
    @Override
    public double getChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    @Override
    public void setChange(double change) {
        this.change = change;
    }

    /**
     * @return the out standing Amount
     */
    @Override
    public double getOutsAmt() {
        return outsAmt;
    }

    /**
     * @param outsAmt the out standing Amount to set
     */
    @Override
    public void setOutsAmt(double outsAmt) {
        this.outsAmt = outsAmt;
    }

    public Date nextPayDate(Tenant t) {
        GregorianCalendar calendar = new GregorianCalendar();
        Date newDate = null;
        if (t.getPayment().getLastPayDate() != null) {
            calendar.setTime(t.getPayment().getLastPayDate());
        }
        calendar.add(Calendar.DAY_OF_WEEK, ChargeInterface.BILLING_CYCLE);
        newDate = calendar.getTime();
        t.getPayment().setNextPayDate(newDate);
        return newDate;
    }

    public Tenant calAmtDue(Tenant t) {
        calLateFee(t);
          //next paymentDate
        calNumDays days = new calNumDays(nextPayDate(t));
        //calculate late fee
        
        int numWeekDays = days.getCountWkDays();
        int numWeekEnds = days.getCountWKEnds();
        //outstanding amount is included in amount due
        double amtDue = 0;
        double discAllowed;
        double outStandingAmt = t.getPayment().getOutsAmt();
        double weekDayAmt = numWeekDays * ChargeInterface.PER_WEEK_DAY;
        double weekEndAmt = numWeekEnds * ChargeInterface.PER_WEEKEND_DAY;
        amtDue = weekDayAmt + weekEndAmt + outStandingAmt;

        //if female, calculate discount (5%)
        if (t.getGender().equals("female")) {
            discAllowed = amtDue * ChargeInterface.FEMALE_DISC;
            t.getPayment().setDiscAmount(discAllowed);
            //subtract disCount
            amtDue -= discAllowed;
        }

        amtDue += t.getPayment().getLateFee();
        t.getPayment().setAmountDue(amtDue);
        return t;
    }

    //inner class
    private final class calNumDays {

        int countWkDays;
        int countWkEnds;

        //weekdays
        void setCountWkDays(int cwkDays) {
            this.countWkDays = cwkDays;
        }

        //weekEnds
        void setCountWkEnds(int cwkEnds) {
            this.countWkEnds = cwkEnds;
        }

        //get WeekDays
        int getCountWkDays() {
            return this.countWkDays;
        }

        //get weekEndDays
        int getCountWKEnds() {
            return this.countWkEnds;
        }

        public calNumDays(Date nextD) {
            GregorianCalendar startCal = new GregorianCalendar();
            GregorianCalendar endCal = new GregorianCalendar();
            endCal.setTime(nextD);
            int cWkDays = 0;
            int cWkEnds = 0;
            do {
                if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                        || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    ++cWkEnds;
                } else if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                        && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    ++cWkDays;
                }
                //excluding start date
                startCal.add(Calendar.DAY_OF_MONTH, 1);
            } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
            //set inner class weekend days and weekdays late
            setCountWkDays(cWkDays);
            setCountWkEnds(cWkEnds);
        }
    }

    public void calLateFee(Tenant t) {
        //if tenant pays after the payment date a 
        //late fee is applied
        /*if next pay date is not null*/
        if (t.getPayment().getNextPayDate() != null) {

            GregorianCalendar now = new GregorianCalendar();

            GregorianCalendar nextpayDate = new GregorianCalendar();
            nextpayDate.setTime(t.getPayment().getNextPayDate());
            int cWkEnds = 0;
            int cWkDays = 0;
            //if next pay date is before now
            if (nextpayDate.getTime().before(now.getTime())) {
                do {
                    if (nextpayDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                            || nextpayDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        ++cWkEnds;
                    } else if (nextpayDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                            && nextpayDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        ++cWkDays;
                    }
                    //excluding start date
                    nextpayDate.add(Calendar.DAY_OF_WEEK, 1);
                } while (nextpayDate.getTimeInMillis() < now.getTimeInMillis());

            }
            //add .75% to each late dat
            double LateDaysFee = (cWkDays * ChargeInterface.PER_WEEK_DAY)
                    + ((cWkDays * ChargeInterface.PER_WEEK_DAY) * ChargeInterface.LATE_FEE_PERCENTAGE);

            double LateWKendFee = (cWkEnds * ChargeInterface.PER_WEEKEND_DAY)
                    + ((cWkEnds * ChargeInterface.PER_WEEKEND_DAY) * ChargeInterface.LATE_FEE_PERCENTAGE);
            //total late fee
            double totalLateFee = LateDaysFee + LateWKendFee;
            //total days late
            int daysLate = cWkDays + cWkEnds;
            t.getPayment().setLateFee(totalLateFee);
            t.getPayment().setTotalDaysLate(daysLate);
        }
    }
}
