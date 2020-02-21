/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.Date;

/**
 *
 * @author wests
 */
public interface PaymentInterface {

    double getAmountDue();

    void setAmountDue(double amountDue);

    Date getNextPayDate();

    void setNextPayDate(Date nextPayDate);

    Date getLastPayDate();

    void setLastPayDate(Date lastPayDate);

    double getLateFee();

    void setLateFee(double lateFee);

    int getTotalDaysLate();

    void setTotalDaysLate(int totalDaysLate);

    double getDiscAmount();

    void setDiscAmount(double discAmount);

    double getChange();

    void setChange(double change);

    double getOutsAmt();

    void setOutsAmt(double outsAmt);
}
