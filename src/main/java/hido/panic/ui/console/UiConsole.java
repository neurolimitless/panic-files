package hido.panic.ui.console;


import javafx.scene.Node;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Plugin(name = "UiConsoleAppender", category = "Core", elementType = "appender", printObject = true)
public final class UiConsole extends AbstractAppender {

    private static volatile TextArea consoleTextArea;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();

    protected UiConsole(String name, Filter filter,
                        Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void append(LogEvent event) {
        if (consoleTextArea == null) {
            return;
        }
        readLock.lock();
        try {
            appendToConsole(new String(getLayout().toByteArray(event)));
        } catch (Exception ex) {
            if (!ignoreExceptions()) {
                throw new AppenderLoggingException(ex);
            }
        } finally {
            readLock.unlock();
        }
    }

    @PluginFactory
    public static UiConsole createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("otherAttribute") String otherAttribute) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new UiConsole(name, filter, layout, true);
    }

    private void appendToConsole(String line) {
        synchronized (consoleTextArea) {
            consoleTextArea.appendText(line);
        }
    }

    public static void clearConsole() {
        if (consoleTextArea != null) {
            synchronized (consoleTextArea) {
                consoleTextArea.clear();
            }
        }
    }

    public static Node getConsole() {
        return getInstance();
    }

    private static Node getInstance() {
        TextArea localInstance = consoleTextArea;
        if (localInstance == null) {
            synchronized (UiConsole.class) {
                localInstance = consoleTextArea;
                if (localInstance == null) {
                    consoleTextArea = localInstance = new TextArea();
                }
                consoleTextArea.setWrapText(true);
            }
        }
        return localInstance;
    }
}
