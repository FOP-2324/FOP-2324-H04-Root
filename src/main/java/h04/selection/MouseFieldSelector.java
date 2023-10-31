package h04.selection;

import fopbot.Field;
import fopbot.FieldClickEvent;
import fopbot.FieldClickListener;
import fopbot.InputHandler;

import java.awt.*;

public class MouseFieldSelector implements FieldSelector, FieldClickListener {

    private Field lastField;

    private FieldSelectionListener listener;

    public MouseFieldSelector() {
        InputHandler.getInputHandler().addFieldClickListener(this);
    }

    @Override
    public void setFieldSelectionListener(FieldSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onFieldClick(FieldClickEvent fieldClickEvent) {
        Field field = fieldClickEvent.getField();
        if (lastField != null) {
            lastField.setFieldColor(null);
        }
        field.setFieldColor(Color.BLUE);
        if (lastField == field) {
            listener.onFieldSelection(field);
        }
        lastField = field;

        return;


    }
}
