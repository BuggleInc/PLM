package jlm.core.ui;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Lecture;
import jlm.universe.World;

public class DelayBoundedRangeModel implements BoundedRangeModel, GameListener {

	public static int MIN_DELAY = 0;
	public static int DEFAULT_DELAY = 100;
	public static int MAX_DELAY = 500;

	protected EventListenerList listenerList = new EventListenerList();
	protected transient ChangeEvent changeEvent = null;

	private int extent = 0;
	private int min = MIN_DELAY;
	private int max = MAX_DELAY;
	private boolean isAdjusting = false;

	private Game game;

	public DelayBoundedRangeModel(Game game) {
		this.game = game;
		this.game.addGameListener(this);
	}

	@Override
	public int getMaximum() {
		return this.max;
	}

	@Override
	public void setMaximum(int newMaximum) {
		this.max = newMaximum;
	}

	@Override
	public int getMinimum() {
		return this.min;
	}

	@Override
	public void setMinimum(int newMinimum) {
		this.min = newMinimum;
	}

	@Override
	public int getValue() {
		return game.getSelectedWorld() == null? 0: game.getSelectedWorld().getDelay();
	}

	@Override
	public void setValue(int n) {
		if (game.getSelectedWorld() != null) {
			n = Math.min(n, Integer.MAX_VALUE - extent);

			int newValue = Math.max(n, min);
			if (newValue + extent > max) {
				newValue = max - extent;
			}
			setRangeProperties(newValue, extent, min, max, isAdjusting);
		}
	}

	@Override
	public boolean getValueIsAdjusting() {
		return isAdjusting;
	}

	@Override
	public void setValueIsAdjusting(boolean b) {
		setRangeProperties(game.getSelectedWorld().getDelay(), extent, min, max, b);
	}

	@Override
	public int getExtent() {
		return this.extent;
	}

	@Override
	public void setExtent(int n) {
        int newExtent = Math.max(0, n);
        int value = game.getSelectedWorld().getDelay();
        if(value + newExtent > max) {
            newExtent = max - value;
        }
        setRangeProperties(value, newExtent, min, max, isAdjusting);
	}

	@Override
	public void setRangeProperties(int newValue, int newExtent, int newMin, int newMax, boolean adjusting) {
		if (newMin > newMax) {
			newMin = newMax;
		}
		if (newValue > newMax) {
			newMax = newValue;
		}
		if (newValue < newMin) {
			newMin = newValue;
		}

		/*
		 * Convert the addends to long so that extent can be Integer.MAX_VALUE
		 * without rolling over the sum. A JCK test covers this, see bug
		 * 4097718.
		 */
		if (((long) newExtent + (long) newValue) > newMax) {
			newExtent = newMax - newValue;
		}

		if (newExtent < 0) {
			newExtent = 0;
		}

		boolean isChange = (newValue != game.getSelectedWorld().getDelay()) || (newExtent != extent)
				|| (newMin != min) || (newMax != max) || (adjusting != isAdjusting);

		if (isChange) {
			for (World w:game.getSelectedWorlds())
				w.setDelay(newValue);
			extent = newExtent;
			min = newMin;
			max = newMax;
			isAdjusting = adjusting;

			fireStateChanged();
		}
	}

	@Override
	public void addChangeListener(ChangeListener x) {
		listenerList.add(ChangeListener.class, x);
	}

	@Override
	public void removeChangeListener(ChangeListener x) {
		listenerList.remove(ChangeListener.class, x);
	}

	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	@Override
	public void currentExerciseHasChanged(Lecture lect) { /* don't care */ }

	@Override
	public void currentLessonHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldHasChanged(World w) {
		fireStateChanged();
	}

	@Override
	public void selectedEntityHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldWasUpdated() {
		fireStateChanged();
	}
}
