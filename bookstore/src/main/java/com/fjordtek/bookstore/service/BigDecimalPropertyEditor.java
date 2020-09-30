// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

/**
 * Converts numbers with custom decimal separators to proper BigDecimal format.
 *
 * @author Pekka Helenius
 */

public class BigDecimalPropertyEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String input) {

		String[] decimalSeparators = {",", "."};

		for (int i = 0; i < decimalSeparators.length; i++) {

			// TODO count of decimal separators must be exactly 1
			if (input.contains(decimalSeparators[i])) {
				input = input.replace(decimalSeparators[i], ".");
				break;
			}
		}

		Number number = Double.parseDouble(input);
		BigDecimal bigDecimal = BigDecimal.valueOf(number.doubleValue());
		setValue(bigDecimal);
	}
}