package com.base.app.domain.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by cahayapangriptaalam on 4/28/17 for MrMontirPOS.
 */

public class CurrencyUtil {
    /**
     * Method to convert value to rupiah format
     * @param nominal unformatted value
     * @return formatted value in rupiah
     */
    public static String ConvertRupiahFormat(String nominal) {
        String rupiah;

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(otherSymbols);
        rupiah = decimalFormat.format(Float.parseFloat(nominal));
        return rupiah;
    }

    public static String ConvertDoubleRupiahFormat(String nominal) {
        String rupiah;

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(otherSymbols);
        rupiah = decimalFormat.format(Double.parseDouble(nominal));
        return rupiah;
    }
}
