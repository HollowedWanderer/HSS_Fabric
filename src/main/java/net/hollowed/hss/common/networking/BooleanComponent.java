package net.hollowed.hss.common.networking;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface BooleanComponent extends Component {
    boolean value = false;
    boolean getValue();
    void setValue(boolean value);
}