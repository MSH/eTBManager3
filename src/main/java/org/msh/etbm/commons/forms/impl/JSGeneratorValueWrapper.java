package org.msh.etbm.commons.forms.impl;

/**
 * Interface that must be implemented by any object that wants to expose a specific value
 * to be serialized to java script code during java script code generation
 * Created by rmemoria on 3/8/16.
 */
public interface JSGeneratorValueWrapper {

    Object getValueToGenerateJSCode();
}
