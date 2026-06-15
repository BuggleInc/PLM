package plm.test.gui;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import plm.core.model.Game;
import plm.core.ui.MainFrame;

/**
 * GUI smoke test: launch PLM's main window and check that it actually comes up.
 *
 * Run under a display (the CI test job uses xvfb on Linux and the native display
 * on Windows/macOS), this drives the full Swing stack -- not just the headless
 * engine initialisation the other tests do.
 */
public class MainFrameSmokeTest {

    private FrameFixture window;

    @Before
    public void setUp() {
        Game.getInstance(); // build the model (also initialises the languages)
        MainFrame frame = GuiActionRunner.execute(() -> MainFrame.getInstance());
        window = new FrameFixture(frame);
        window.show();
    }

    @Test
    public void theMainWindowComesUp() {
        window.requireVisible();
    }

    @After
    public void tearDown() {
        if (window != null)
            window.cleanUp();
    }
}
