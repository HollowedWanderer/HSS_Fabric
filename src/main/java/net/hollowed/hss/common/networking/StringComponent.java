package net.hollowed.hss.common.networking;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface StringComponent extends Component {
    String value = "";
    String getValue();
    void setValue(String value);
}