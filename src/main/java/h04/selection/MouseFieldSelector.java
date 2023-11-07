package h04.selection;

import fopbot.Field;
import fopbot.FieldClickEvent;
import fopbot.FieldClickListener;
import fopbot.World;

public class MouseFieldSelector implements FieldSelector, FieldClickListener {

    private Field lastField;

    private FieldSelectionListener listener;

    public MouseFieldSelector() {
        World.addFieldClickListener(this);
    }

    @Override
    public void setFieldSelectionListener(FieldSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onFieldClick(FieldClickEvent fieldClickEvent) {
        listener.onFieldSelection(fieldClickEvent.getField());
    }
}
