package org.poo.main.utils;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class CustomPrettyPrinter extends DefaultPrettyPrinter {

    public CustomPrettyPrinter() {
        _objectFieldValueSeparatorWithSpaces = ": ";

        this.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
    }
    /** I applied Overriding to make
     * my Json output be similar to ref, for easier debugging
     */
    @Override
    public CustomPrettyPrinter createInstance() {
        return new CustomPrettyPrinter();
    }
}
