package org.cef;

import java.awt.*;

/**
 * This file is a part of Seeking Client.
 */
public class DummyComponent extends Component {
    @Override
    public Point getLocationOnScreen() {
        return new Point(0, 0);
    }
}
