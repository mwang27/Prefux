/*  
 * Copyright (c) 2004-2013 Regents of the University of California.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of the University nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * Copyright (c) 2014 Martin Stockhammer
 */
/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.action.assignment;

import java.util.logging.Logger;

import prefux.action.EncoderAction;
import prefux.data.expression.Predicate;
import prefux.data.expression.parser.ExpressionParser;
import prefux.util.ColorLib;
import prefux.util.PrefuseLib;
import prefux.visual.VisualItem;


/**
 * <p>Assignment Action that assigns color values to VisualItems for a
 * given color field (e.g., the stroke, text, or fill color).</p>
 * 
 * <p>By default, a ColorAction simply assigns a single default color value
 * to all items (the initial default color is black). Clients can change this 
 * default value to achieve uniform color assignment, or can add any number 
 * of additional rules for color assignment. Rules are specified by a Predicate
 * instance which, if returning true, will trigger that rule, causing either the
 * provided color value or the result of a delegate ColorAction to be
 * applied. Rules are evaluated in the order in which they are added to the
 * ColorAction, so earlier rules will have precedence over rules added later.
 * </p>
 * 
 * <p>In addition, subclasses can simply override {@link #getColor(VisualItem)}
 * to achieve custom color assignment. In some cases, this may be the simplest
 * or most flexible approach.</p>
 * 
 * <p>To automatically assign color values based on varying values of a
 * particular data field, consider using the DataColorAction.</p>
 * 
 * <p>Color values are represented using integers, into which 8-bit values for
 * the red, green, blue, and alpha channels are stored. For more information
 * and utilities for creating and manipulating color values, see the
 * {@link prefux.util.ColorLib} class.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see prefux.util.ColorLib
 * @see DataColorAction
 */
public class ColorAction extends EncoderAction {
    
    protected String m_colorField;
    protected String m_startField;
    protected String m_endField;
    
    protected int m_cidx, m_sidx, m_eidx;
    
    protected int m_defaultColor = ColorLib.gray(0); // initial default = black
    
    /**
     * Constructor, sets the data group and color field for color assignment.
     * Uses an initial default color value of black [RGB value (0,0,0)].
     * @param group the data group processed by this Action
     * @param field the color field assigned by this Action
     */
    public ColorAction(String group, String field) {
        super(group);
        setField(field);
    }

    /**
     * Constructor, sets the data group, color field, and default color value
     * for color assignment.
     * @param group the data group processed by this Action
     * @param field the color field assigned by this Action
     * @param color the default color value assigned by this ColorAction
     */
    public ColorAction(String group, String field, int color) {
        this(group, field);
        m_defaultColor = color;
    }
    
    /**
     * Constructor, sets the data group, filter predicate and color field
     * for color assignment.
     * Uses an initial default color value of black [RGB value (0,0,0)].
     * @param group the data group processed by this Action
     * @param filter the filter predicate
     *  {@link prefux.data.expression.Predicate}
     * @param field the color field assigned by this Action
     */
    public ColorAction(String group, Predicate filter, String field) {
    	super(group, filter);
    	setField(field);
    }
     
    /**
     * Constructor, sets the data group, filter predicate,
     * color field, and default color value for color assignment.
     * @param group the data group processed by this Action
     * @param filter the filter predicate 
     * 	{@link prefux.data.expression.Predicate}
     * @param field the color field assigned by this Action
     * @param color the default color value assigned by this ColorAction
     */
    public ColorAction(String group, Predicate filter, String field, int color)
    {
    	this(group, filter, field);
    	setDefaultColor(color);
    }    
    
    /**
     * Set the color field name that this ColorAction should set. The
     * ColorAction will automatically try to update the start and end
     * values for this field if it is an interpolated field.
     * @param field
     */
    public void setField(String field) {
        m_colorField = field;
        m_startField = PrefuseLib.getStartField(field);
        m_endField = PrefuseLib.getEndField(field);
    }
    
    /**
     * Returns the default color for this ColorAction
     * @return the default color value
     */
    public int getDefaultColor() {
        return m_defaultColor;
    }
    
    /**
     * Sets the default color for this ColorAction. Items will be assigned
     * the default color if they do not match any registered rules.
     * @param color the new default color
     */
    public void setDefaultColor(int color) {
        m_defaultColor = color;
    }
    
    /**
     * Add a color mapping rule to this ColorAction. VisualItems that match
     * the provided predicate will be assigned the given color value (assuming
     * they do not match an earlier rule).
     * @param p the rule Predicate 
     * @param color the color value
     */
    public void add(Predicate p, int color) {
        super.add(p, new Integer(color));
    }

    /**
     * Add a color mapping rule to this ColorAction. VisualItems that match
     * the provided expression will be assigned the given color value (assuming
     * they do not match an earlier rule). The provided expression String will
     * be parsed to generate the needed rule Predicate.
     * @param expr the expression String, should parse to a Predicate. 
     * @param color the color value
     * @throws RuntimeException if the expression does not parse correctly or
     * does not result in a Predicate instance.
     */
    public void add(String expr, int color) {
        Predicate p = (Predicate)ExpressionParser.parse(expr);
        add(p, color);      
    }
    
    /**
     * Add a color mapping rule to this ColorAction. VisualItems that match
     * the provided predicate will be assigned the color value returned by
     * the given ColorAction's getColor() method.
     * @param p the rule Predicate 
     * @param f the delegate ColorAction to use
     */
    public void add(Predicate p, ColorAction f) {
        super.add(p, f);
    }

    /**
     * Add a color mapping rule to this ColorAction. VisualItems that match
     * the provided expression will be assigned the given color value (assuming
     * they do not match an earlier rule). The provided expression String will
     * be parsed to generate the needed rule Predicate.
     * @param expr the expression String, should parse to a Predicate. 
     * @param f the delegate ColorAction to use
     * @throws RuntimeException if the expression does not parse correctly or
     * does not result in a Predicate instance.
     */
    public void add(String expr, ColorAction f) {
        Predicate p = (Predicate)ExpressionParser.parse(expr);
        super.add(p, f);
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.action.ItemAction#process(prefux.visual.VisualItem, double)
     */
    public void process(VisualItem item, double frac) {
        int c = getColor(item);
        int o = item.getInt(m_colorField);
        item.setInt(m_startField, o);
        item.setInt(m_endField, c);
        item.setInt(m_colorField, c);
    }

    /**
     * Returns a color value for the given item. Colors are represented as
     * integers, interpreted as holding values for the red, green, blue, and 
     * alpha channels. This is the same color representation returned by
     * the Color.getRGB() method.
     * @param item the item for which to get the color value
     * @return the color value for the item
     */
    public int getColor(VisualItem item) {
        Object o = lookup(item);
        if ( o != null ) {
            if ( o instanceof ColorAction ) {
                return ((ColorAction)o).getColor(item);
            } else if ( o instanceof Integer ) {
                return ((Integer)o).intValue();
            } else {
                Logger.getLogger(this.getClass().getName())
                    .warning("Unrecognized Object from predicate chain.");
            }
        }
        return m_defaultColor;   
    }
    
} // end of class ColorAction
