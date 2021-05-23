package org.yx.dubbo.listener.event;

import org.yx.dubbo.listener.DubboEventListener;
import org.yx.listener.ListenerGroup;
import org.yx.listener.ListenerGroupImpl;
import org.yx.listener.SumkEvent;

/**
 * @author wjiajun
 */
public final class DubboEventPublisher {

	private static final ListenerGroup<DubboEventListener> group = new ListenerGroupImpl<>();

	public static void publish(SumkEvent event) {
		group.listen(event);
	}

	public static ListenerGroup<DubboEventListener> group() {
		return group;
	}

}