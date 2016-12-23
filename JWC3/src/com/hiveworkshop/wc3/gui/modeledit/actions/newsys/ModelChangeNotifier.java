package com.hiveworkshop.wc3.gui.modeledit.actions.newsys;

import java.util.HashSet;
import java.util.Set;

public class ModelChangeNotifier implements ModelChangeListener {
	private final Set<ModelChangeListener> listeners;

	public ModelChangeNotifier() {
		listeners = new HashSet<>();
	}

	public void subscribe(final ModelChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void modelChanged() {
		for (final ModelChangeListener listener : listeners) {
			listener.modelChanged();
		}
	}
}
